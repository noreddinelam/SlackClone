package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Properties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class Server {

    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws IOException {
        ServerImpl.initListOfFunctions();
        logger.info("Creating a server on port 9999");
        try (AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(Properties.PORT));
            logger.info("Waiting a connection...");
            while (true) {
                serverSocket.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                    @Override
                    public void completed(AsynchronousSocketChannel result, Object attachment) {
                        try {
                            if (serverSocket.isOpen()) {
                                serverSocket.accept(null, this);
                            }
                            SocketAddress socketAddress = result.getRemoteAddress();
                            ServerImpl.addConnectedClients(result);
                            logger.info("A client is connected from {}", socketAddress);
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            result.read(buffer, buffer, new ServerReaderCompletionHandler());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        exc.printStackTrace();
                    }
                });
                System.in.read();

            }
        }
    }
}

