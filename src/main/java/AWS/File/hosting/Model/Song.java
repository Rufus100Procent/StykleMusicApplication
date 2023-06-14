package AWS.File.hosting.Model;

 public class Song {
     private String name;
     private String filePath;

     public Song(String name, String filePath) {
         this.name = name;
         this.filePath = filePath;
     }

     public String getName() {
         return name;
     }

     public String getFilePath() {
         return filePath;
     }
 }