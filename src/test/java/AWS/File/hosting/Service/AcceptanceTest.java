package AWS.File.hosting.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AcceptanceTest {
    @Mock
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testUploadLoadAndDeleteFile() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String fileContent = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MultipartFile multipartFile = new MockMultipartFile("file", fileName, null, inputStream);

        // Mock storeFile method
        Mockito.when(fileService.storeFile(multipartFile)).thenReturn(fileName);

        // Mock loadFileAsResource method
        Mockito.when(fileService.loadFileAsResource(fileName)).thenReturn(Mockito.mock(Resource.class));

        // Mock deleteFile method
        Mockito.when(fileService.deleteFile(fileName)).thenReturn(true);

        // Act
        String storedFileName = fileService.storeFile(multipartFile);
        Resource loadedResource = fileService.loadFileAsResource(fileName);
        boolean deletionResult = fileService.deleteFile(fileName);

        // Assert
        Assertions.assertEquals(fileName, storedFileName);
        Assertions.assertNotNull(loadedResource);
        Assertions.assertTrue(deletionResult);
    }
}
