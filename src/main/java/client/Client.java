package client;

import shared.Properties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client {
    private static InetSocketAddress ipAddress = new InetSocketAddress("localhost", Properties.PORT);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        try (AsynchronousSocketChannel socket = AsynchronousSocketChannel.open()) {
            socket.connect(ipAddress).get();
            while (socket.isOpen() && scanner.hasNext()){
                String line = scanner.nextLine();
            }
                ByteBuffer buffer = ByteBuffer.wrap("salut".getBytes("UTF-8"));
            Future<Integer> future = socket.write(buffer);
            future.get();
        }
    }
}
