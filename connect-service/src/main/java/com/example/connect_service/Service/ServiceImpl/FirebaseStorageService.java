package com.example.connect_service.Service.ServiceImpl;


import com.example.connect_service.Exception.AppException;
import com.example.connect_service.Exception.ErrorCode;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseStorageService {


    public String getSignedUrl(String bucketName, String filePath) throws IOException {
        Storage storage = StorageClient.getInstance().bucket(bucketName).getStorage();
        BlobId blobId = BlobId.of(bucketName, filePath);
        Blob blob = storage.get(blobId);

        if (blob == null || !blob.exists()) {

            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }
        return blob.signUrl(1, TimeUnit.HOURS).toString();
    }
    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}
