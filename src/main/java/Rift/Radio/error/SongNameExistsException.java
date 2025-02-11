package Rift.Radio.error;

public class SongNameExistsException extends RuntimeException {
    public SongNameExistsException(String message) {
        super(message);
    }
}