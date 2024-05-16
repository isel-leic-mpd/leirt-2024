package isel.leirt.mpd.weather4.requests;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;

public interface AsyncRequest {
    CompletableFuture<Reader>  get(String path);
}