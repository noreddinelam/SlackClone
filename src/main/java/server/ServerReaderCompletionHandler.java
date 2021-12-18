package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.communication.Request;
import shared.gson_configuration.GsonConfiguration;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerReaderCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger logger = LoggerFactory.getLogger(ServerReaderCompletionHandler.class);

    private AsynchronousSocketChannel client;

    ServerReaderCompletionHandler(AsynchronousSocketChannel client) {
        this.logger.info("ReaderCompletionHandler instantiated");
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        this.logger.info("ReaderCompletionHandler completed");
        attachment.flip();
        String requestJson = new String(attachment.array()).substring(0, result);
        System.out.println(requestJson);
        Request requestObject = GsonConfiguration.gson.fromJson(requestJson, Request.class);
        String response = ServerImpl.getFunctionWithRequestCode(requestObject).apply(requestObject.getRequestData());
        attachment.clear();
        attachment = ByteBuffer.wrap(response.getBytes());
        System.out.println(response.getBytes());
        this.client.write(attachment, attachment, new ServerWriterCompletionHandler(this.client));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
