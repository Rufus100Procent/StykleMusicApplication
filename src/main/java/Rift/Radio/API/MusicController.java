//// MusicController.java
//package Rift.Radio.API;
//
//import Rift.Radio.Model.Song;
//import Rift.Radio.Service.MusicService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//public class MusicController {
//
//    private final MusicService musicService;
//
//    @Autowired
//    public MusicController(MusicService musicService) {
//        this.musicService = musicService;
//    }
//
//    @GetMapping("/")
//    public String index() {
//        return "upload";
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadSong(@RequestParam("file") MultipartFile file,
//                                             @RequestParam("songName") String songName,
//                                             @RequestParam("artistName") String artistName,
//                                             @RequestParam("albumName") String albumName,
//                                             @RequestParam("releaseYear") int releaseYear) {
//        try {
//            musicService.uploadSong(file, songName, artistName, albumName, releaseYear);
//            return ResponseEntity.ok().body("Song uploaded successfully");
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().body("Failed to upload song: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/download/{songName}")
//    public ResponseEntity<Resource> downloadSong(@PathVariable String songName) {
//        try {
//            Resource resource = musicService.downloadSong(songName);
//            String contentType = "audio/mpeg";
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .body(resource);
//        } catch (IOException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}