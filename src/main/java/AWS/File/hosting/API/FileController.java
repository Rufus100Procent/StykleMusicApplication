package AWS.File.hosting.API;

import AWS.File.hosting.Service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private final FileService fileStorageService;

    public FileController(FileService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String showUploadPage() {
        return "upload";
    }



    @GetMapping("/upload")
    public String upload(){
        return "upload";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
        } else {
            try {
                String fileName = fileStorageService.storeFile(file);
                redirectAttributes.addFlashAttribute("message", "File uploaded successfully: " + fileName);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
            }
        }

        return "redirect:/uploaded";
    }

    @GetMapping("/uploaded")
    public String showUploadedFiles(Model model, @RequestParam(value = "query", required = false) String query) {
        List<String> uploadedFiles;

        if (query != null && !query.isEmpty()) {
            uploadedFiles = fileStorageService.searchUploadedFiles(query);
            if (uploadedFiles.isEmpty()) {
                model.addAttribute("message", "No matching files found for the search query: " + query);
            }
        } else {
            uploadedFiles = fileStorageService.getUploadedFileNames();
        }

        model.addAttribute("uploadedFiles", uploadedFiles);
        return "uploaded";
    }

    @GetMapping("/search")
    public String searchUploadedFiles(@RequestParam("query") String query, Model model) {
        List<String> uploadedFiles = fileStorageService.searchUploadedFiles(query);
        model.addAttribute("uploadedFiles", uploadedFiles);
        return "uploaded";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        if (resource == null) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        String contentType = "application/octet-stream";
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(resource.getFilename())
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(resource);
    }

    @PostMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName, RedirectAttributes redirectAttributes) {
        try {
            fileStorageService.deleteFile(fileName);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully: " + fileName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to delete file: " + fileName);
        }

        return "redirect:/uploaded";
    }


    private void openFileManager() throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                // Get the file storage path
                String fileStoragePath = FileService.getFileStoragePath();

                // Open the file manager at the file storage path
                File fileStorageDirectory = new File(fileStoragePath);
                desktop.open(fileStorageDirectory);
            }
        }
    }
}
