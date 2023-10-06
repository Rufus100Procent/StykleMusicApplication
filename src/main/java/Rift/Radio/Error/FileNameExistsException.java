package Rift.Radio.Error;

public class FileNameExistsException extends RuntimeException {
    public FileNameExistsException(String message) {
        super(message);
    }
}