package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.communication.Request;
import shared.gson_configuration.GsonConfiguration;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class ServerReaderCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final static Logger logger = LoggerFactory.getLogger(ServerReaderCompletionHandler.class);


    ServerReaderCompletionHandler() {
        this.logger.info("ReaderCompletionHandler instantiated");
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        this.logger.info("ReaderCompletionHandler completed with {}", result);
        attachment.flip();
        String requestJson = new String(attachment.array()).substring(0, result);
        Request requestObject = GsonConfiguration.gson.fromJson(requestJson, Request.class);
        ServerImpl.getFunctionWithRequestCode(requestObject).accept(requestObject.getRequestData());
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
