package com.nottaras.prototype.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    @Mock
    private GridFSFile gridFSFile;

    @Mock
    private GridFSBucket gridFSBucket;

    @Mock
    private GridFsResource gridFsResource;

    @Mock
    private GridFsOperations gridFsOperations;

    @Mock
    private GridFSFindIterable findIterable;

    @Mock
    private InputStream inputStream;

    @Captor
    private ArgumentCaptor<String> filenameCaptor;

    @Test
    public void givenValidFile_whenStore_thenFileStoredSuccessfully() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file content".getBytes());
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        String fileId = fileStorageService.store(file);

        assertNotNull(fileId);
        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenValidFile_whenStore_thenFileStoredSuccessfullyAndFilenameIsCorrect() {
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content);
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        String fileId = fileStorageService.store(file);

        assertNotNull(fileId);
        verify(gridFSBucket).uploadFromStream(filenameCaptor.capture(), any(InputStream.class));

        assertEquals("test.txt", filenameCaptor.getValue());
        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenFileWithNoOriginalFilename_whenStore_thenGeneratedFilenameIsUsed() {
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", null, "text/plain", content);
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        String fileId = fileStorageService.store(file);

        assertNotNull(fileId);

        verify(gridFSBucket).uploadFromStream(filenameCaptor.capture(), any(InputStream.class));
        String capturedFileName = filenameCaptor.getValue();

        assertEquals(24, capturedFileName.length());
        assertTrue(capturedFileName.matches("^[a-fA-F0-9]{24}$"));

        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenFileWithEmptyOriginalFilename_whenStore_thenGeneratedFilenameIsUsed() {
        byte[] content = "Test file content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "\t\n\r\f ", "text/plain", content);
        when(gridFSBucket.uploadFromStream(any(), any(InputStream.class))).thenReturn(new ObjectId());

        String fileId = fileStorageService.store(file);

        assertNotNull(fileId);

        verify(gridFSBucket).uploadFromStream(filenameCaptor.capture(), any(InputStream.class));
        String capturedFileName = filenameCaptor.getValue();

        assertEquals(24, capturedFileName.length());
        assertTrue(capturedFileName.matches("^[a-fA-F0-9]{24}$"));

        verify(gridFSBucket).uploadFromStream(any(), any(InputStream.class));
    }

    @Test
    public void givenRuntimeExceptionOnStorage_whenStore_thenRuntimeExceptionThrown() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file content".getBytes());
        doThrow(new RuntimeException("Failed to store file")).when(gridFSBucket).uploadFromStream(any(), any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.store(file));
        assertNotNull(exception.getMessage());
    }

    @Test
    public void givenIOExceptionOnGetInputStream_whenStore_thenRuntimeExceptionThrown() throws IOException {
        MockMultipartFile mockFile = mock(MockMultipartFile.class);
        doThrow(new IOException()).when(mockFile).getInputStream();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.store(mockFile), "Failed to store file");
        assertEquals("Failed to store file", exception.getMessage());
    }

    @Test
    public void givenNonExistentFileId_whenLoadAsResource_thenIllegalArgumentExceptionThrown() {
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find((Bson) any())).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileStorageService.loadAsResource(objectId.toString()));
        assertEquals("File not found", exception.getMessage());
    }

    @Test
    public void givenValidFileIdAndResourceUnavailable_whenLoadAsResource_thenIOExceptionThrownByInputStreamRetrieval() throws IOException {
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find((Bson) any())).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(gridFSFile);
        when(gridFsOperations.getResource(gridFSFile)).thenReturn(gridFsResource);
        doThrow(IOException.class).when(gridFsResource).getInputStream();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.loadAsResource(objectId.toString()), "File retrieval failed");
        assertEquals("File retrieval failed", exception.getMessage());
    }

    @Test
    public void givenValidFileIdAndResourceUnavailable1_whenLoadAsResource_thenIOExceptionThrownByInputStreamRetrieval() throws IOException {
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find((Bson) any())).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(gridFSFile);
        when(gridFsOperations.getResource(gridFSFile)).thenReturn(gridFsResource);
        doThrow(RuntimeException.class).when(gridFsResource).getInputStream();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileStorageService.loadAsResource(objectId.toString()), "File retrieval failed");
        assertEquals("File retrieval failed", exception.getMessage());
    }

    @Test
    public void givenValidFileId_whenLoadAsResource_thenInputStreamRetrievedAndLoggedSuccessfully() throws IOException {
        ObjectId objectId = new ObjectId();
        when(gridFSBucket.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(gridFSFile);
        when(gridFsOperations.getResource(gridFSFile)).thenReturn(gridFsResource);
        when(gridFsResource.getInputStream()).thenReturn(inputStream);

        Resource resource = fileStorageService.loadAsResource(objectId.toString());

        assertInstanceOf(InputStreamResource.class, resource);
        verify(gridFsResource, times(1)).getInputStream();
    }
}
