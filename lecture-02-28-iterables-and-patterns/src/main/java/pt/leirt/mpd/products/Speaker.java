package pt.leirt.mpd.products;

import org.json.JSONArray;
import org.json.JSONObject;

public class Speaker extends BaseElectronics {
    private final double power; // in Watts

    public Speaker(String name, String brand, double price, double power) {
        super(name, brand, price);
        this.power = power;
    }

    @Override
    public Category getCategory() {
        return Category.AUDIO;
    }

    public double getPower() {
        return power;
    }


}
