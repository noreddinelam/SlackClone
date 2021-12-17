package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Channel;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.FieldsRequestName;
import shared.adapters.LocalDateTimeSerializer;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;
import java.util.Map;

public class ServerReaderCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger logger = LoggerFactory.getLogger(ServerReaderCompletionHandler.class);
    private final static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,new LocalDateTimeSerializer()).setPrettyPrinting().create();
    private AsynchronousSocketChannel client;

    ServerReaderCompletionHandler(AsynchronousSocketChannel client) {
        this.logger.info("ReaderCompletionHandler instantiated");
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        this.logger.info("ReaderCompletionHandler completed");
        attachment.flip();
        String requestData = new String(attachment.array()).substring(0, result);
        Map<String, String> requestAfterParsing = ServerImpl.requestParser(requestData);
        ServerImpl.getFunctionWithRequestCode(requestAfterParsing.get(FieldsRequestName
                .netCode)).accept(requestAfterParsing);
        logger.info("Client has sent \"{}\"", requestData);
        attachment.clear();
        String jsonRes = gson.toJson(new Channel(new User(),"test","test",true));
        attachment = ByteBuffer.wrap(jsonRes.getBytes());
       this.client.write(attachment,attachment,new ServerWriterCompletionHandler(this.client));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
