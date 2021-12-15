package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Properties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class Server {

    private static Logger logger = LoggerFactory.getLogger(Server.class);
    private static ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ServerImpl.initListOfFunctionsAndParsers();
        logger.info("Creating a server on port 9999");
        try (AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(Properties.PORT));
            logger.info("Waiting a connection...");
            while (true) {
                serverSocket.accept("Connection Started", new CompletionHandler<AsynchronousSocketChannel, Object>() {
                    @Override
                    public void completed(AsynchronousSocketChannel result, Object attachment) {
                        try {
                            if (serverSocket.isOpen()) {
                                serverSocket.accept(null, this);
                            }
                            SocketAddress socketAddress = result.getRemoteAddress();
                            listOfClients.put(socketAddress.toString(),result);
                            logger.info("A client is connected from {}", socketAddress);
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int nbChar = result.read(buffer).get();
                            buffer.flip();
                            String requestData = new String(buffer.array()).substring(0, nbChar);
                            //HashMap<String, String> requestAfterParsing = ServerImpl.requestParser(requestData);
                            //ServerImpl.getFunctionWithRequestCode(requestAfterParsing.get(FieldsRequestName
                            // .netCode)).accept(requestAfterParsing);
                            logger.info("Client has sent {}", new String(buffer.array()).substring(0, nbChar));
                        } catch (IOException | InterruptedException | ExecutionException e) {
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
