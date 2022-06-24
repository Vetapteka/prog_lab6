import ch.qos.logback.classic.Logger;
import commands.Command;
import model.MyCollection;
import org.slf4j.LoggerFactory;
import utils.DatabaseManager;
import utils.PropertiesManager;

import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;


public class Server {
    private static final Properties property = PropertiesManager.getProperties();
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Server.class);

    private static MyCollection myCollection;


    private static Selector selector;
    private static ServerSocketChannel serverSocket;

    private static class Request {
        private final SelectionKey key;
        private final Command command;

        public Request(SelectionKey key, Command command) {
            this.key = key;
            this.command = command;
        }

        public SelectionKey getKey() {
            return key;
        }

        public Command getCommand() {
            return command;
        }

    }

    private static final LinkedBlockingDeque<SelectionKey> clients = new LinkedBlockingDeque<>(6);
    private static final LinkedBlockingDeque<Request> req = new LinkedBlockingDeque<>(6);

    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(2);


    static {
        try {
            DatabaseManager.connectionToDataBase();
            logger.info("successful database connection");
            myCollection = new MyCollection(DatabaseManager.getCollection());
            logger.info("collection loaded");

            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(property.getProperty("host"),
                    Integer.parseInt(property.getProperty("port"))));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);


        } catch (IOException e) {
            logger.error("unable to connect");
            e.printStackTrace();

        } catch (SQLException e) {
            logger.error("fail connecting to database");
        }
    }

    public static void main(String[] args) {
        //два потока собирают клиентов в очередь
        fixedThreadPool.submit(() ->
        {
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocket.accept();
                        logger.info("new connection: " + client);
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        if (clients.offer(key)) {
                            logger.info("new request from: " + client);
                            client.register(selector, SelectionKey.OP_WRITE);
                        } else {
                            logger.warn("clients limit exceeded!");
                            client.close();
                        }
                    }
                    iter.remove();
                }
            }

        });
        //вынимает из очереди клиента и читает его запрос
        // складывает запрос в другую очередь
        cachedThreadPool.submit(() ->
        {
            while (true) {
                SelectionKey key;
                try {
                    key = clients.poll(10, TimeUnit.MINUTES);

                    SocketChannel client = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(256000);
                    client.read(buffer);

                    ByteArrayInputStream baos = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream oos = new ObjectInputStream(baos);
                    Command command;
                    command = (Command) oos.readObject();

                    if (req.offer(new Request(key, command), 4, TimeUnit.SECONDS)) {
                        logger.info("get a command: " + command.toString() + " ---client---" + client);
                    } else {
                        logger.warn("requests limit exceeded!");
                        client.close();
                    }


                    oos.close();
                    baos.close();


                } catch (InterruptedException e) {
                    logger.error("problem with multithreading");
                } catch (ClassNotFoundException | IOException e) {
                    logger.error("unable to read command!");
                }
            }
        });

// вынимает  из очереди запрос и отправить ответ клиенту
        forkJoinPool.execute(() ->
        {
            while (true) {
                try {
                    Request request = req.poll(10, TimeUnit.MINUTES);
                    SelectionKey key = request.getKey();
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer2 = ByteBuffer.allocate(256000);

                    Command command = request.getCommand();

                    String res = command.execute(myCollection);


                    buffer2.clear();
                    buffer2.put(res.getBytes(StandardCharsets.UTF_8));
                    buffer2.flip();
                    client.write(buffer2);
                    buffer2.clear();

                    client.register(selector, SelectionKey.OP_READ);

                    logger.info("send a response: " + client);

                    if (command.getName().equals(property.getProperty("exitCommandName"))) {
                        logger.info("client disconnected " + client);
                        client.close();
                    }

                } catch (InterruptedException e) {
                    logger.error("problem with multithreading");
                } catch (IOException e) {
                    logger.error("unable to write swth to  client!");

                }
            }
        });

    }
}