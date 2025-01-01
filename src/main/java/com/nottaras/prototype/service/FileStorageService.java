package com.nottaras.prototype.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.nottaras.prototype.validation.ValidMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final GridFSBucket gridFSBucket;
    private final GridFsOperations gridFsOperations;

    public String store(@ValidMultipartFile MultipartFile file) {
        ObjectId uploadedFileId;
        var fileName = generateFileName(file);

        try (InputStream inputStream = file.getInputStream()) {
            uploadedFileId = gridFSBucket.uploadFromStream(fileName, inputStream);
        } catch (IOException e) {
            log.error("Failed to store file {}: ", fileName, e);
            throw new RuntimeException("Failed to store file", e);
        }

        log.info("File stored successfully with name: {}", fileName);
        return uploadedFileId.toString();
    }

    public Resource loadAsResource(String fileId) {
        try {
            var objectId = new ObjectId(fileId);
            var gridFSFile = getFileByObjectId(objectId);

            if (gridFSFile == null) {
                log.error("File not found for ID: {}", fileId);
                throw new IllegalArgumentException("File not found");
            }

            InputStream inputStream = getInputStreamFromFile(gridFSFile);
            log.info("File retrieved successfully for ID: {}", fileId);
            return new InputStreamResource(inputStream);
        } catch (IllegalArgumentException e) {
            log.error("Invalid file ID {}: ", fileId, e);
            throw e;
        } catch (IOException e) {
            log.error("Failed to retrieve file for ID {}: ", fileId, e);
            throw new RuntimeException("File retrieval failed", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving file for ID {}: ", fileId, e);
            throw new RuntimeException("File retrieval failed", e);
        }
    }

    private String generateFileName(MultipartFile file) {
        return (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank())
            ? file.getOriginalFilename()
            : new ObjectId().toHexString();
    }

    private GridFSFile getFileByObjectId(ObjectId objectId) {
        return gridFSBucket.find(Filters.eq("_id", objectId)).first();
    }

    private InputStream getInputStreamFromFile(GridFSFile file) throws IOException {
        return gridFsOperations.getResource(file).getInputStream();
    }
}
