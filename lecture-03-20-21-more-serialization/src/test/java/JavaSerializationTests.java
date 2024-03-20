import isel.leirt.mpd.entities.Player;
import isel.leirt.mpd.entities.Team;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaSerializationTests {
    @Test
    public void serializeTeamWithJavaSerialization() throws IOException {
        var rafaSilva = new Player("Rafa Silva", Team.Position.Forward, 30 );
        var diMaria = new Player("Angel di Maria", Team.Position.Forward, 35 );
        var aSilva = new Player("Antonio Silva", Team.Position.CenterBack, 20 );
        var team = new Team("Benfica", List.of(aSilva, rafaSilva, diMaria));
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.dat"))) {
            oos.writeObject(team);
        }
       
    }
    
    @Test
    public void serializeAndDeserializeTeamWithJavaSerialization()
                        throws IOException, ClassNotFoundException, FileNotFoundException {
        var rafaSilva = new Player("Rafa Silva", Team.Position.Forward, 30 );
        var diMaria = new Player("Angel di Maria", Team.Position.Forward, 35 );
        var aSilva = new Player("Antonio Silva", Team.Position.CenterBack, 20 );
        var team = new Team("Benfica", List.of(aSilva, rafaSilva, diMaria));
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.dat"))) {
            oos.writeObject(team);
        }
        
        try(var ois = new ObjectInputStream(new FileInputStream("object.dat"))) {
            var newTeam = (Team) ois.readObject();
            assertEquals(team, newTeam);
            System.out.println(newTeam);
        }
    }
}
