import ch.qos.logback.classic.Logger;
import commands.Command;
import model.MyCollection;
import org.slf4j.LoggerFactory;
import utils.Converter;
import utils.PropertiesManager;

import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class Server {
    private static String fileName;
    private static final Properties property = PropertiesManager.getProperties();

    private static MyCollection myCollection;
    private static Selector selector;
    private static ByteBuffer buffer;
    private static ServerSocketChannel serverSocket;
    private static final Logger logger;

    static {
        logger = (Logger) LoggerFactory.getLogger(Server.class);
        try {
            fileName = property.getProperty("fileName");
            myCollection = Converter.fromJson(fileName);
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(property.getProperty("host"), Integer.parseInt(property.getProperty("port"))));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            buffer = ByteBuffer.allocate(256000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            acceptingConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void acceptingConnections() throws IOException {
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

        ByteArrayInputStream baos = new ByteArrayInputStream(buffer.array());
        ObjectInputStream oos = new ObjectInputStream(baos);
        Command command;
        try {
            command = (Command) oos.readObject();
            logger.info("get a command: " + command.toString());

            String res = command.execute(myCollection);

            buffer.clear();
            byte[] aaa = res.getBytes(StandardCharsets.UTF_8);
            buffer.put(aaa);

            buffer.flip();
            client.write(buffer);
            buffer.clear();


            if (command.getName().equals(property.getProperty("exitCommandName"))) {
                logger.info("client disconnected " + client);
                Converter.toJson(myCollection, fileName);
                client.close();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        logger.info("new connection: " + client.toString());
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

}