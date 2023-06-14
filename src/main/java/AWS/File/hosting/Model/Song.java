package AWS.File.hosting.Model;

 public class Song {
     private String name;
     private String fileUrl;

     public Song(String name, String fileUrl) {
         this.name = name;
         this.fileUrl = fileUrl;
     }

     public String getName() {
         return name;
     }

     public String getFileUrl() {
         return fileUrl;
     }
 }