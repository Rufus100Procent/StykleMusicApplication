package Rift.Radio.Error;

public class FileExistsException extends RuntimeException {
    public FileExistsException(String message) {
        super(message);
    }
}
