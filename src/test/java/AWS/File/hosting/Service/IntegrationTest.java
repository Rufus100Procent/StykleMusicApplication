package AWS.File.hosting.Service;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
@SpringBootTest
@TestPropertySource(properties = {
        "file.storage.path=test-files" // Set a different file storage path for testing
})
public class IntegrationTest {

    @Autowired
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        // Clean up the test directory before each test
        fileService.getUploadedFileNames().forEach(fileService::deleteFile);
    }

    @Test
    public void testStoreFile() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String fileContent = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MockMultipartFile file = new MockMultipartFile("file", fileName, null, inputStream);

        // Act
        String storedFileName = fileService.storeFile(file);

        // Assert
        Assertions.assertEquals(fileName, storedFileName);
        Assertions.assertTrue(fileService.getUploadedFileNames().contains(fileName));


        boolean deletionResult = fileService.deleteFile(fileName);

    }


    @Test
    public void testDeleteFile() throws IOException {
        // Arrange
        String fileName = "test.txt";
        String fileContent = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MockMultipartFile file = new MockMultipartFile("file", fileName, null, inputStream);
        fileService.storeFile(file);

        // Act
        boolean deletionResult = fileService.deleteFile(fileName);

        // Assert
        Assertions.assertTrue(deletionResult);
        Assertions.assertFalse(fileService.getUploadedFileNames().contains(fileName));
    }
}
