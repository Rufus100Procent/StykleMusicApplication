package Rift.Radio.Error;

public class MP3FileExistsException extends RuntimeException {
    public MP3FileExistsException(String message) {
        super(message);
    }
}
