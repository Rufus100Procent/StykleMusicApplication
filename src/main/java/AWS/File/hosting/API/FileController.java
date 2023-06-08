package AWS.File.hosting.API;


import AWS.File.hosting.Cognito.Cognito;
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
    public String GetLogin(){

        return "login";
    }


    @PostMapping("/")
    public String PostLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes){

        System.out.println(username);
        System.out.println(password);

        if (Cognito.Login(username,password)){

            return "redirect:/home";

        }else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String GetRegister(){

        return "registration";
    }

    /**
     *
     * @param username
     * @param email
     * @param password
     * @param redirectAttributes
     * @return
     * It retrieves the username, email, and password from the request parameters.
     * It calls the Cognito.Register method to register the user in the Cognito user pool.
     * If the registration is successful, it redirects to the verification page ("/verify") and passes the email and username as redirect attributes.
     * If the registration fails, it returns the "auth/registration" view to display the registration form again.
     */

    @PostMapping("/register")
    public String PostRegister(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               RedirectAttributes redirectAttributes){

        System.out.println(username);
        System.out.println(email);
        System.out.println(password);

        if (Cognito.Register( username,password,email)){

            redirectAttributes.addAttribute("email",email);
            redirectAttributes.addAttribute("username",username);
            return "redirect:/verify";

        }else {
            return "registration";
        }
    }

    @GetMapping("/verify")
    public String GetVerify(@RequestParam("email") String email,
                            @RequestParam("username") String username,
                            Model model
    ){

        model.addAttribute("email",email);
        model.addAttribute("username",username);
        return "/verify";
    }

    @PostMapping("/verify")
    public String PostVerify(@RequestParam("username") String username,
                             @RequestParam("confirmationCode") String confirmationCode
    ){

        if (Cognito.ConfirmUser(username,confirmationCode)){

            return "redirect:/home";

        }else
            return "redirect:/home";

    }




    @PostMapping("/logout")
    public String PostLogout(){
        Cognito.Logout();
        return "redirect:/login";
    }



    @GetMapping("/home")
    public String showHomePage(Model model, HttpServletRequest request) {
        List<String> uploadedFiles = fileStorageService.getUploadedFileNames();
        model.addAttribute("uploadedFiles", uploadedFiles);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/home";
        }

        try {
            String fileName = fileStorageService.storeFile(file);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully: " + fileName);
            openFileManager();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
        }

        return "redirect:/home";
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

        return "redirect:/home";
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
