package client;

import models.Channel;
import models.Message;
import models.User;
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
import java.time.LocalDateTime;
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
                    // TODO switch or consumer to adapt to each request
                    //Map<String,String> requestData = new HashMap<>();
                    //requestData.put(FieldsRequestName.messageID,"10");

                    Channel channel =new Channel(new User("dola"),"test-channel","test",true);
                    Message requestData = new Message("testing-message",new User("nouredine","123456"),channel,
                            LocalDateTime.now());
                    Request request = new Request(line,GsonConfiguration.gson.toJson(requestData));
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
