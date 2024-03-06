package pt.leirt.mpd.products;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;

/**
 * Defines the contract for a product
 */
public interface Electronics {

    enum Category { AUDIO, VIDEO, INFORMATICS, COMMUNICATIONS }

    String getName();               // get  product name
    double getPrice();              // get  product price
    Category getCategory();         // get  product category
    String getBrand();              // get  product brand
}
