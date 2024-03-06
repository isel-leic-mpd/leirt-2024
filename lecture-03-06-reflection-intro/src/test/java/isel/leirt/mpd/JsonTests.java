package isel.leirt.mpd;

import isel.leirt.mpd.entities.Player;
import isel.leirt.mpd.entities.Team;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTests {
    
    @Test
    public void playerToJsonConversionTest() {
        var p = new Player("Rafa Silva", Team.Position.Forward, 30 );
        System.out.println(p.toJson());
    }
    
    @Test
    public void jsonToPlayerConversionTest() {
        var jsonText = """
                            {
                                "name":"Rafa Silva",
                                "position":"Forward",
                                "age" : "30"
                             }
                       """;
        var p = Player.fromJson(new JSONObject(jsonText));
        System.out.println(p);
    }
    
    @Test
    public void teamToJsonConversionTest() {
        var rafaSilva = new Player("Rafa Silva", Team.Position.Forward, 30 );
        var diMaria = new Player("Angle di Maria", Team.Position.Forward, 35 );
        var aSilva = new Player("Antonio Silva", Team.Position.CenterBack, 20 );
        var team = new Team("Benfica", List.of(aSilva, rafaSilva, diMaria));
        System.out.println(team.toJson());
    }
    
    @Test
    public void jsonToTeamConversionTest() {
        var jsonText = """
                         {
                            "players": [
                                {
                                    "name": "Antonio Silva",
                                    "position": "CenterBack",
                                    "age": 20
                                },
                                {
                                    "name": "Rafa Silva",
                                    "position": "Forward",
                                    "age": 30
                                },
                                {
                                    "name": "Angle di Maria",
                                    "position": "Forward",
                                    "age": 35
                                }
                            ],
                            "name": "Benfica"
                          }
                        """;
        var t = Team.fromJson(new JSONObject(jsonText));
        
        var json2 = t.toJson().toString(4);
        json2 = json2.replaceAll("\\s", "");
        jsonText = jsonText.replaceAll("\\s", "");
        assertEquals(jsonText, json2);
        System.out.println(json2);
    }
    
}
