package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerWriterCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private static Logger logger = LoggerFactory.getLogger(ServerWriterCompletionHandler.class);
    private final AsynchronousSocketChannel client;


    public ServerWriterCompletionHandler(AsynchronousSocketChannel client){
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        logger.info("{} chars have been sent to the server", result);
        attachment.clear();
        this.client.read(attachment,attachment,new ServerReaderCompletionHandler(this.client));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
