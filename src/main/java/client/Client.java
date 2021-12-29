package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.FieldsRequestName;
import shared.Properties;
import shared.communication.Request;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Client {
    private final static InetSocketAddress serverIpAddress = new InetSocketAddress("localhost", Properties.PORT);
    private final static Scanner scanner = new Scanner(System.in);
    private final static Logger logger = LoggerFactory.getLogger(Client.class);
    private final static ClientImpl[] clientImplementations =
            {TerminalClientImpl.getUniqueInstanceOfTerminalClientImpl(),
                    GraphicalClientImpl.getUniqueInstanceOfGraphicalClientImpl()};
    private static String clientIpAddress = "";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socket = AsynchronousSocketChannel.open();
        socket.connect(serverIpAddress).get();
        String[] ipParts = socket.getLocalAddress().toString().split(":");
        clientIpAddress = ipParts[ipParts.length - 1];
        clientImplementations[0].initListOfFunctions();
        Thread writer = new Thread(() -> {
            String line = "SOMETHING WRONG";
            ByteBuffer buffer;
            while (true) {
                try {
                    line = scanner.nextLine();
                    Map<String, String> requestData = new HashMap<>();
                    requestData.put(FieldsRequestName.userName, "dola");
                    //requestData.put(FieldsRequestName.guest, clientIpAddress);
                    Request request = new Request(line, GsonConfiguration.gson.toJson(requestData));
                    System.out.println(request.getRequestData());
                    String jsonRes = GsonConfiguration.gson.toJson(request);
                    buffer = ByteBuffer.wrap(jsonRes.getBytes("UTF-8"));
                    socket.write(buffer, buffer, new ClientWriterCompletionHandler());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread reader = new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            try {
                while (socket.isOpen()) {
                    int nb = socket.read(buffer).get();
                    String jsonRes = new String(buffer.array()).substring(0, nb);
                    logger.info("The received response \n{}", jsonRes);
                    Response response = GsonConfiguration.gson.fromJson(jsonRes, Response.class);
                    ClientImpl.getFunctionWithRequestCode(response).accept(response.getResponse());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        writer.start();
        reader.start();
    }
}
