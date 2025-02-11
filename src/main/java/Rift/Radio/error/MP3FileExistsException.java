package Rift.Radio.error;

public class MP3FileExistsException extends RuntimeException {
    public MP3FileExistsException(String message) {
        super(message);
    }
}
