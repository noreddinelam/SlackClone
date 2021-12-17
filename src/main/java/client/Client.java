package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Properties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Client {
    private static InetSocketAddress ipAddress = new InetSocketAddress("localhost", Properties.PORT);
    private static Scanner scanner = new Scanner(System.in);
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        try (AsynchronousSocketChannel socket = AsynchronousSocketChannel.open()) {
            socket.connect(ipAddress).get();

            Thread writer = new Thread(()->{
                logger.info("Thread writer is running");
                String line = "SOMETHING WRONG";
                ByteBuffer buffer;
                while (scanner.hasNext()) {
                    logger.info("Thread looping");
                    try {
                        line = scanner.nextLine();
                        buffer = ByteBuffer.wrap(line.getBytes("UTF-8"));
                        socket.write(buffer,buffer,new ClientWriterCompletionHandler());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread reader = new Thread(()->{
                logger.info("Thread reader is running");
                ByteBuffer buffer = ByteBuffer.allocate(1024);
//                while(socket.isOpen()){
//                    logger.info("Thread looping");
//                    socket.read(buffer,buffer,new ClientReaderCompletionHandler(socket));
//                }
            });
            writer.start();
            //reader.start();
        }
    }
}
