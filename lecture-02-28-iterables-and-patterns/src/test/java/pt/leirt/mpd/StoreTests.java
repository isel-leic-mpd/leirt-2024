package pt.leirt.mpd;

import org.junit.jupiter.api.Test;
import pt.leirt.mpd.products.Resolution;
import pt.leirt.mpd.products.Speaker;
import pt.leirt.mpd.products.TV;

public class StoreTests {
  private final static Store store = new Store();
  private final static Resolution hd = new Resolution(1024, 768);
  private final static Resolution fullHd = new Resolution(1920, 1080);
  private final static Resolution uhd = new Resolution(3840, 2160);

  static {

    store.addCatalog(new TV("X95", "Sony", 3000, uhd, 65.0))
         .addCatalog(new Speaker("x300", "JBL", 100, 40));
  }

  @Test
  public void duplicatedProduct() {
    store.addCatalog(new TV("X95", "Sony", 3000, uhd, 65.0));
  }

}
