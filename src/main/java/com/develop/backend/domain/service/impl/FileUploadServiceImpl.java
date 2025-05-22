package com.develop.backend.domain.service.impl;


import com.develop.backend.domain.service.FileUploadService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${file.upload-dir}")
    private String UPLOAD_FOLDER;

    @Value("${base-url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        Path uploadPath = Paths.get(UPLOAD_FOLDER);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                log.error("No se pudo crear el directorio de carga: {}", UPLOAD_FOLDER, e);
            }
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (!file.isEmpty()){
            Path uploadPath = Paths.get(UPLOAD_FOLDER, folder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            byte [] bytes = file.getBytes();
            String originalFileName = Objects.requireNonNull(file.getOriginalFilename()); // Obtener el nombre original del archivo
            String sanitizedFileName = originalFileName.replaceAll("[^a-zA-Z0-9.]", "_");
            String encodedFileName = URLEncoder.encode(sanitizedFileName, StandardCharsets.UTF_8);
            Path path = Paths.get(UPLOAD_FOLDER, folder, encodedFileName);
            Files.write(path, bytes);
            return baseUrl + folder + "/" + encodedFileName;
        }
        return null;
    }

    @Override
    public void deleteUpload(String fileUrl) throws IOException {
        String filePath = extractPathFromUrl(fileUrl);
        Path path = Paths.get(UPLOAD_FOLDER, filePath);
        if (Files.deleteIfExists(path)) {
            log.info("Archivo eliminado: {}", filePath);
        } else {
            log.warn("No se pudo eliminar el archivo: {}", filePath);
        }
    }

    private String extractPathFromUrl(String url) {
        int index = url.indexOf("/upload/");
        if (index == -1) {
            throw new IllegalArgumentException("URL inválida, no contiene '/upload/': " + url);
        }
        return url.substring(index + "/upload/".length());
    }
}
