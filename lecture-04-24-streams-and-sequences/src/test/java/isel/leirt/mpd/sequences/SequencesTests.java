package isel.leirt.mpd.sequences;

import org.junit.jupiter.api.Test;

import java.util.List;

public class SequencesTests {
    
    @Test
    public void sequenceFromCollectionTest() {
        var list = List.of("Carlos", "Paulo", "Pedro");
        var seq = Sequence.of(list);
        
        seq.forEach(System.out::println);
    }
}
