package com.develop.backend.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadFile(MultipartFile file, String folder) throws IOException;
    void deleteUpload(String fileUrl) throws IOException;
}
