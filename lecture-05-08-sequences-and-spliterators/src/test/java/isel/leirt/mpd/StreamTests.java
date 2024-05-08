package isel.leirt.mpd;

import isel.leirt.mpd.streams.StreamUtils;
import org.junit.jupiter.api.Test;

public class StreamTests {
    
    @Test
    public void fileLinesStreamTest() {
        try(var stream = StreamUtils.fileLines("input.txt")) {
            stream.forEach(System.out::println);
        }
        
//        var stream =
//            StreamUtils.fileLines("input.txt")
//            .limit(10);
//        stream.forEach(System.out::println);
    }
    
}
