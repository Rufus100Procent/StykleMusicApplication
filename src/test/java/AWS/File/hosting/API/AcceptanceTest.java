package AWS.File.hosting.API;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertNotNull(response);

        assertEquals(302, response.getStatus());
    }

//    @Test
//    public void testDeleteFile() throws Exception {
//        // Create a test file
//        Path tempFile = createTempFile("test", "Hello, World!");
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/delete/{fileName}", tempFile.getFileName()))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andReturn();
//
//        MockHttpServletResponse response = mvcResult.getResponse();
//
//        assertNotNull(response);
//
//        assertEquals(302, response.getStatus());
//
//        // Ensure the file has been deleted
//        assertTrue(Files.notExists(tempFile));
//    }

    @Test
    public void testGetLogin() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertNotNull(response);
        assertNotNull(modelAndView);

        assertEquals(200, response.getStatus());
        assertEquals("login", modelAndView.getViewName());
    }

    @Test
    public void testGetRegister() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("registration"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertNotNull(response);
        assertNotNull(modelAndView);

        assertEquals(200, response.getStatus());
        assertEquals("registration", modelAndView.getViewName());
    }

    @Test
    public void testGetVerify() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/verify")
                        .param("email", "test@example.com")
                        .param("username", "testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/verify"))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertNotNull(response);
        assertNotNull(modelAndView);

        assertEquals(200, response.getStatus());
        assertEquals("/verify", modelAndView.getViewName());
    }

    @Test
    public void testPostVerify() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/verify")
                        .param("username", "testuser")
                        .param("confirmationCode", "123456"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertNotNull(response);

        assertEquals(302, response.getStatus());
    }

    // Helper method to create a temporary test file
    private Path createTempFile(String fileName, String content) throws Exception {
        Path tempFile = Files.createTempFile(fileName, ".txt");
        Files.write(tempFile, content.getBytes());
        return tempFile;
    }

    //requare client -ID
    //    @Test
//    public void testPostLogin() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/")
//                        .param("username", "testuser")
//                        .param("password", "password123"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//
//    }


//    @Test
//    public void testPostRegister() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/register")
//                        .param("username", "testuser")
//                        .param("email", "test@example.com")
//                        .param("password", "password123"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//    }
}
