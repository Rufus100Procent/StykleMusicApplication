package Rift.Radio.Error;

public class SongNameExistsException extends RuntimeException {
    public SongNameExistsException(String message) {
        super(message);
    }
}