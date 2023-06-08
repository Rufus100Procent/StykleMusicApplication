package AWS.File.hosting.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String username = "stykle";
        String refreshToken = "1234";
        String accessToken = "555";

        // Act
        User user = new User(username, refreshToken);
        user.setAccessToken(accessToken);

        // Assert
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(refreshToken, user.getRefreshToken());
        Assertions.assertEquals(accessToken, user.getAccessToken());
    }
}