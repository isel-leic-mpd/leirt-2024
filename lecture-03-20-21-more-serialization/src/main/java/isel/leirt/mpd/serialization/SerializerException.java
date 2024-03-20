package isel.leirt.mpd.serialization;

public class SerializerException extends RuntimeException {
    public SerializerException(String msg, Exception e) {
        super(msg, e);
    }
    
    public SerializerException(String msg ) {
        super(msg );
    }
}
