package pt.leirt.mpd;

import static pt.leirt.mpd.products.Electronics.Category;

import pt.leirt.mpd.products.Electronics;
import pt.leirt.mpd.products.Speaker;
import pt.leirt.mpd.products.TV;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class Store {
    private Collection<Electronics> catalog = new ArrayList<>();

    public Store addCatalog(Electronics product) {
        catalog.add(product);
        return this;
    }

    public Iterable<Electronics> fromSamsung() {
        List<Electronics> samsungProds = new ArrayList<>();
        var samsungProds1 = new ArrayList<Electronics>();

        for(var p : catalog) {
            if (p.getBrand().equalsIgnoreCase("Samsung"))
                samsungProds.add(p);
        }
        return samsungProds;
    }

    public Iterable<TV> getAbove65Inches() {
        List<TV> bigTvs = new ArrayList<>();
      
        for(var p : catalog) {
            /*
            if ( p.getCategory() == Category.VIDEO &&
                p.getClass() == TV.class &&
                ((TV) p).getScreenSize() > 65) {
                bigTvs.add((TV) p);
            }
            */
            
            if (    p.getCategory() == Category.VIDEO &&
                    (p instanceof TV tv) &&
                    tv.getScreenSize() > 65) {
                bigTvs.add(tv);
                
            }
        }
        return bigTvs;
    }

    public Iterable<Speaker> getSpeakersInPowerLessThen40() {

        return null;
    }


}
