package AWS.File.hosting.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private static Path fileStoragePath = null;
    private final List<String> uploadedFiles = new ArrayList<>();

    public FileService() {
        this.fileStoragePath = Paths.get(getFileStoragePath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create the directory to store files.", e);
        }
    }

    public static String getFileStoragePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return System.getenv("USERPROFILE") + "\\fileStorage";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return System.getProperty("user.home") + "/fileStorage";
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
    }


    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new IllegalArgumentException("Invalid file name: " + fileName);
            }

            Path filePath = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            uploadedFiles.add(fileName);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store the file: " + fileName, e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load the file: " + fileName, e);
        }
    }

    public List<String> getUploadedFileNames() {
        return uploadedFiles;
    }

    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);

            uploadedFiles.remove(fileName);

            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete the file: " + fileName, e);
        }
    }
}
