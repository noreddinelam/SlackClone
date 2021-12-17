package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.Properties;

import java.io.IOException;
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
            String line = "SOMETHING WRONG";
            ByteBuffer buffer;
            while (socket.isOpen() && scanner.hasNext()) {
                line = scanner.nextLine();
                buffer = ByteBuffer.wrap(line.getBytes("UTF-8"));
                socket.write(buffer,buffer,new ClientWriterCompletionHandler());
            }

        }
    }
}
