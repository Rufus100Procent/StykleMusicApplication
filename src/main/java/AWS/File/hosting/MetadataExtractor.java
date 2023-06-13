package AWS.File.hosting;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.IOException;
import java.io.InputStream;

public class MetadataExtractor {

    public Metadata extractAudioMetadata(InputStream inputStream) throws IOException {
        Parser audioParser = new AudioParser();
        BodyContentHandler audioHandler = new BodyContentHandler();
        Metadata audioMetadata = new Metadata();
        audioParser.parse(inputStream, audioHandler, audioMetadata);

        return audioMetadata;
    }

    public Metadata extractVideoMetadata(String filePath) throws IOException {
        FFprobe ffprobe = new FFprobe();
        FFprobe.setFFprobePath(FFprobe.ffprobePath().toAbsolutePath().toString());
        FFmpegProbeResult videoProbeResult = ffprobe.probe(filePath);
        FFmpegStream videoStream = videoProbeResult.getStreams().get(0);

        Metadata videoMetadata = new Metadata();
        videoMetadata.set("Duration", videoStream.duration);
        videoMetadata.set("Format", videoStream.format);
        videoMetadata.set("Resolution", videoStream.width + "x" + videoStream.height);

        return videoMetadata;
    }
}
