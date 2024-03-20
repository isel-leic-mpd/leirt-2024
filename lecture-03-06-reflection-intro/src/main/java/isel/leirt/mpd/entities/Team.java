package isel.leirt.mpd.entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team  implements Serializable {
    public  enum Position {
        GoalKeeper, CenterBack, WingBack, MidFielder, Forward
    }
    
    private String name;
    private Player[] players;
    
    
    public Team(String name, List<Player> players) {
        this.name = name;
        this.players  = players.toArray(sz -> new Player[sz]);
    }
    
    public JSONObject toJson() {
        var teamJson = new JSONObject();
        teamJson.put("name", name);
        var playersJson = new JSONArray();
        
        for(int i=0; i < players.length; ++i) {
            playersJson.put(i, players[i].toJson());
        }
        
        teamJson.put("players", playersJson);
        return teamJson;
    }
    
    public static Team fromJson(JSONObject jsonTeam)  {
       String name = jsonTeam.getString("name");
       var jsonPlayers = jsonTeam.getJSONArray("players");
       var playersList = new ArrayList<Player>();
       for (int i=0; i < jsonPlayers.length(); ++i) {
               playersList.add(Player.fromJson(jsonPlayers.getJSONObject(i)));
       }
       return new Team(name, playersList);
    }
    
    public static Team fromJsonText(String jsonText)   {
        return fromJson(new JSONObject(jsonText));
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("name: " + name);
        sb.append(System.lineSeparator());
        sb.append("players:");
        sb.append(System.lineSeparator());
        for (int i=0; i < players.length; ++i) {
            sb.append("  ");
            sb.append(players[i]);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != Team.class) return false;
        Team otherTeam = (Team) obj;
        if (!otherTeam.name.equals(name)) return false;
        if (players.length != otherTeam.players.length) return false;
        for(int i=0; i < players.length; ++i) {
            if (!players[i].equals(otherTeam.players[i]) ) return false;
        }
        return true;
    }
}
