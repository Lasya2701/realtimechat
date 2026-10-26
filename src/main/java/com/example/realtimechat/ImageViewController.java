
package com.example.realtimechat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageViewController {

    private static final Path UPLOAD_DIR =
            Path.of("uploads").toAbsolutePath().normalize();

    private static final Map<String, MediaType> TYPES = Map.ofEntries(
            Map.entry("jpg", MediaType.IMAGE_JPEG),
            Map.entry("png", MediaType.IMAGE_PNG),
            Map.entry("webp", MediaType.parseMediaType("image/webp")),
            Map.entry("gif", MediaType.IMAGE_GIF),
            Map.entry("mp4", MediaType.parseMediaType("video/mp4")),
            Map.entry("webm", MediaType.parseMediaType("video/webm")),
            Map.entry("ogv", MediaType.parseMediaType("video/ogg")),
            Map.entry("mp3", MediaType.parseMediaType("audio/mpeg")),
            Map.entry("wav", MediaType.parseMediaType("audio/wav")),
            Map.entry("ogg", MediaType.parseMediaType("audio/ogg")),
            Map.entry("m4a", MediaType.parseMediaType("audio/mp4")),
            Map.entry("aac", MediaType.parseMediaType("audio/aac")),
            Map.entry("3gp", MediaType.parseMediaType("audio/3gpp")),
            Map.entry("flac", MediaType.parseMediaType("audio/flac"))
    );

    @GetMapping({
        "/api/media/{filename}",
        "/api/images/{filename}"
    })
    public ResponseEntity<Resource> getMedia(
            @PathVariable String filename) throws IOException {

        if (!filename.matches(
                "[a-fA-F0-9-]+\\.(jpg|png|webp|gif|mp4|webm|ogv|mp3|wav|ogg|m4a|aac|3gp|flac)")) {
            return ResponseEntity.badRequest().build();
        }

        Path file = UPLOAD_DIR.resolve(filename).normalize();

        if (!file.startsWith(UPLOAD_DIR)
                || !Files.isRegularFile(file)) {
            return ResponseEntity.notFound().build();
        }

        String extension = filename.substring(
                filename.lastIndexOf('.') + 1
        ).toLowerCase(Locale.ROOT);

        MediaType mediaType = TYPES.get(extension);

        if (mediaType == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header("X-Content-Type-Options", "nosniff")
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + filename + "\""
                )
                .body(new FileSystemResource(file));
    }
}
