package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.FieldsRequestName;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;

public class ReaderCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private static Logger logger = LoggerFactory.getLogger(ReaderCompletionHandler.class);
    private AsynchronousSocketChannel client;

    ReaderCompletionHandler(AsynchronousSocketChannel client) {
        this.logger.info("ReaderCompletionHandlerInstantiated");
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        String requestData = new String(attachment.array()).substring(0, result);
        HashMap<String, String> requestAfterParsing = ServerImpl.requestParser(requestData);
        ServerImpl.getFunctionWithRequestCode(requestAfterParsing.get(FieldsRequestName
                .netCode)).accept(requestAfterParsing);
        logger.info("Client has sent \"{}\"", requestData);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
