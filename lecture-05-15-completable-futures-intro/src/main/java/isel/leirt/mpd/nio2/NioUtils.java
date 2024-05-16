package isel.leirt.mpd.nio2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.CompletableFuture;

public class NioUtils {
    private static Charset charSet = Charset.defaultCharset();
    private static CharsetDecoder decoder = charSet.newDecoder();
    private static CharsetEncoder encoder = charSet.newEncoder();

   
    /*
     *  CompletableFuture async operations
     */
    
    private static CompletionHandler<Void, CompletableFuture<Void>> conHandler =
        new CompletionHandler<>() {
            
            @Override
            public void completed(Void result, CompletableFuture<Void> future) {
                future.complete(null);
            }
            
            @Override
            public void failed(Throwable exc, CompletableFuture<Void> future) {
                future.completeExceptionally(exc);
            }
        };
    
    private static CompletionHandler<Integer, CompletableFuture<Integer>>
        rwHandler = new CompletionHandler<Integer,CompletableFuture<Integer>>() {
        
        @Override
        public void completed(Integer result, CompletableFuture<Integer> future) {
            future.complete(result);
        }
        
        @Override
        public void failed(Throwable exc, CompletableFuture<Integer>  future) {
            
            future.completeExceptionally(exc);
        }
    };
    
    public static CompletableFuture<Void>
    connect(AsynchronousSocketChannel channel,  SocketAddress sockAddr)
    {

        CompletableFuture<Void> result = new CompletableFuture<>();
        channel.connect(sockAddr, result, conHandler);
        return result;
    }

    public static CompletableFuture<Integer>
            write(AsynchronousSocketChannel channel, String cmd) {
        CharBuffer buffer = CharBuffer.allocate(4096);
        CompletableFuture<Integer> result = new CompletableFuture<>();
        try {
            buffer.put(cmd);
            buffer.flip();
            ByteBuffer bbuf = encoder.encode(buffer);
            channel.write(bbuf, result, rwHandler);
        }
        catch(IOException e) {
            result.completeExceptionally(e);
        }
        return result;
    }

    public static  CompletableFuture<String> read(AsynchronousSocketChannel channel)
    {
        CompletableFuture<Integer> result = new CompletableFuture<>();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        
        channel.read(buffer, result, rwHandler);
        
        return result.thenApply( n -> {
            if (n <= 0)
                return null;
            buffer.flip();
            try {
                return decoder.decode(buffer).toString();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
