package user;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserLoginTest {

    private User user;
    private User userIncorrect;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userIncorrect = UserGenerator.getUserIncorrect();
        userClient = new UserClient();
    }

    @Test
    public void loginUser200(){
        userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.from(user));
        accessToken = responseLogin.extract().path("accessToken");
        assertEquals(SC_OK, responseLogin.extract().statusCode());
        assertTrue(responseLogin.extract().path("success"));
    }

    @Test
    public void loginUserWithIncorrect401(){
        ValidatableResponse responseCreate = userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.from(userIncorrect));
        accessToken = responseCreate.extract().path("accessToken");
        assertEquals(SC_UNAUTHORIZED, responseLogin.extract().statusCode());
        assertEquals("email or password are incorrect", responseLogin.extract().path("message"));
    }

    @Test
    public void loginUserWithEmptyPassword401(){
        ValidatableResponse responseCreate = userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.fromOnlyEmail(user));
        accessToken = responseCreate.extract().path("accessToken");
        assertEquals(SC_UNAUTHORIZED, responseLogin.extract().statusCode());
        assertEquals("email or password are incorrect", responseLogin.extract().path("message"));
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }
}