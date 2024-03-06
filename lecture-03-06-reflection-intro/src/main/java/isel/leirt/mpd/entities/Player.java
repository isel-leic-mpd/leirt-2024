package isel.leirt.mpd.entities;

import org.json.JSONObject;

public class Player {
    private String name;
    private Team.Position position;
    private int age;
    
    public Player(String name, Team.Position position, int age) {
        this.name = name;
        this.position = position;
        this.age = age;
    }
    
    public String getName() { return name; }
    public  Team.Position getPosition() { return position; }
    public int getAge() { return age; }
    
    @Override
    public String toString() {
        return String.format("{ name=%s, position=%s, age=%d }",
            name, position.name(), age);
    }
    
    public JSONObject toJson() {
        var playerJson = new JSONObject();
        playerJson.put("name", name);
        playerJson.put("age", age);
        playerJson.put("position", position.name());
        return playerJson;
    }
    
    public static Player fromJson(JSONObject jsonPlayer)  {
        
        int age = jsonPlayer.getInt("age");
        String name = jsonPlayer.getString("name");
        Team.Position position = jsonPlayer.getEnum(Team.Position.class, "position");
        
        return new Player(name, position, age);
    }
    
    public static Player fromJsonText(String jsonText)   {
        return fromJson(new JSONObject(jsonText));
    }
}
