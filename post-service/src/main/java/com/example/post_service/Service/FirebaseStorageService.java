package com.example.post_service.Service;

import com.example.post_service.Exception.AppException;
import com.example.post_service.Exception.ErrorCode;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseStorageService {


    public String uploadFile(String bucketName, String folderName, MultipartFile file) throws IOException {
        String fileName = generateFileName(file.getOriginalFilename());
        String objectName = folderName + fileName; // Đã bao gồm "post/"
        Storage storage = StorageClient.getInstance().bucket(bucketName).getStorage();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getBytes());

        return fileName; // Chỉ trả về tên file, không bao gồm đường dẫn thư mục
    }
    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}
