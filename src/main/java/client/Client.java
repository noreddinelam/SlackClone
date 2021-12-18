package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.Properties;
import shared.communication.Request;
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
    private static InetSocketAddress ipAddress = new InetSocketAddress("localhost", Properties.PORT);
    private static Scanner scanner = new Scanner(System.in);
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socket = AsynchronousSocketChannel.open();
        socket.connect(ipAddress).get();
        Thread writer = new Thread(() -> {
            String line = "SOMETHING WRONG";
            ByteBuffer buffer;
            while (true) {
                try {
                    line = scanner.nextLine();
                    Map<String,String> requestData = new HashMap<>();
                    // TODO switch or consumer to adapt to each request
                    requestData.put(FieldsRequestName.messageID,"10");
                    Request request = new Request(line,GsonConfiguration.gson.toJson(requestData, CommunicationTypes.mapJsonTypeData));
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
//                    Type fooType2 = new TypeToken< Response <List<Channel>>>() {}.getType();
//                    Response<List<Channel>> res = GsonConfiguration.gson.fromJson(jsonRes,fooType2);
//                    logger.info("The retrieved response {}",res.getResponse().get(0).getChannelName());
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
