package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Server {
    static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("Creating a server on port 1236");
        try (AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(1236));
            logger.info("Waiting a connection...");
            while(true) {
                Future<AsynchronousSocketChannel> future = serverSocket.accept();
                AsynchronousSocketChannel socketChannel = future.get();
                logger.info("A client is connected from " + socketChannel.getRemoteAddress());
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int nbChar = socketChannel.read(buffer).get();
                buffer.flip();
                logger.info("Client has sent {} " + new String(buffer.array()).substring(0, nbChar));
            }
        }
    }
}
