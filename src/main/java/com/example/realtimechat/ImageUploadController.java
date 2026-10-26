
package com.example.realtimechat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private static final long MAX_FILE_SIZE = 25L * 1024 * 1024;

    private static final Path UPLOAD_DIR =
            Path.of("uploads").toAbsolutePath().normalize();

    private static final Map<String, String> ALLOWED_TYPES = Map.ofEntries(
            Map.entry("image/jpeg", "jpg"),
            Map.entry("image/png", "png"),
            Map.entry("image/webp", "webp"),
            Map.entry("image/gif", "gif"),
            Map.entry("video/mp4", "mp4"),
            Map.entry("video/webm", "webm"),
            Map.entry("video/ogg", "ogv"),
            Map.entry("audio/mpeg", "mp3"),
            Map.entry("audio/mp3", "mp3"),
            Map.entry("audio/wav", "wav"),
            Map.entry("audio/x-wav", "wav"),
            Map.entry("audio/ogg", "ogg"),
            Map.entry("audio/webm", "webm"),
            Map.entry("audio/mp4", "m4a"),
            Map.entry("audio/aac", "aac"),
            Map.entry("audio/3gpp", "3gp"),
            Map.entry("audio/flac", "flac")
    );

    @PostMapping(
        value = "/media",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadMedia(
            @RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please select a file."));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(Map.of("error", "The file must not exceed 25 MB."));
        }

        String contentType = file.getContentType();

        if (contentType == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unknown file type."));
        }

        contentType = contentType.toLowerCase(Locale.ROOT)
                .split(";")[0].trim();

        String extension = ALLOWED_TYPES.get(contentType);

        if (extension == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error",
                        "Unsupported file type. Use an approved image, video, or audio format."
                    ));
        }

        String mediaType;

        if (contentType.startsWith("image/")) {
            mediaType = "IMAGE";
        } else if (contentType.startsWith("video/")) {
            mediaType = "VIDEO";
        } else if (contentType.startsWith("audio/")) {
            mediaType = "AUDIO";
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unsupported media type."));
        }

        try {
            Files.createDirectories(UPLOAD_DIR);

            String filename = UUID.randomUUID() + "." + extension;
            Path destination = UPLOAD_DIR.resolve(filename).normalize();

            if (!destination.startsWith(UPLOAD_DIR)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid file path."));
            }

            file.transferTo(destination.toFile());

            return ResponseEntity.ok(Map.of(
                "url", "/api/media/" + filename,
                "type", mediaType,
                "filename", filename
            ));

        } catch (IOException | IllegalStateException e) {
            System.err.println("Media upload failed: " + e.getMessage());

            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Could not save the uploaded file."));
        }
    }
}
