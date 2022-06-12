import commands.Command;
import model.MyCollection;
import utils.Converter;

import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static final String POISON_PILL = "POISON_PILL";

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer buffer = ByteBuffer.allocate(256000);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                    answer(buffer, key);
                }
                iter.remove();
            }
        }
    }

    private static void answer(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);

        if (new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        } else {

            MyCollection myCollection = Converter.fromJson("db.json");

            ByteArrayInputStream baos = new ByteArrayInputStream(buffer.array());
            ObjectInputStream oos = new ObjectInputStream(baos);
            Command command = null;
            try {
                command = (Command) oos.readObject();
                String res = command.execute(myCollection);

                buffer.clear();
                byte[] aaa = res.getBytes(StandardCharsets.UTF_8);
                buffer.put(aaa);

                buffer.flip();
                client.write(buffer);
                buffer.clear();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }


}