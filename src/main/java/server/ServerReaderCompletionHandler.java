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
        this.logger.info("ReaderCompletionHandler completed with {}",result);
        attachment.flip();
        String requestJson = new String(attachment.array()).substring(0, result);
        logger.info("Bou3lam titich {}",requestJson);
        Request requestObject = GsonConfiguration.gson.fromJson(requestJson, Request.class);
        ServerImpl.getFunctionWithRequestCode(requestObject).apply(requestObject.getRequestData());
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
