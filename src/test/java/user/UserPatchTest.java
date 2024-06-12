package user;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserPatchTest {

    private User user;
    private User userUpdate;
    private User userSecond;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userUpdate = UserGenerator.getUserUpdate();
        userSecond = UserGenerator.getUserSecond();
        userClient = new UserClient();
    }

    @Test
    public void updateUser200(){
        userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.from(user));
        accessToken = responseLogin.extract().path("accessToken");
        ValidatableResponse responseUpdateUser = userClient.updateUser(accessToken, Credentials.from(userUpdate));
        assertEquals(SC_OK, responseUpdateUser.extract().statusCode());
        assertTrue(responseUpdateUser.extract().path("success"));
    }

    @Test
    public void updateUserWithoutLogin401(){
        ValidatableResponse responseCreate = userClient.createUser(Credentials.from(user));
        accessToken = responseCreate.extract().path("accessToken");
        ValidatableResponse responseUpdateUser = userClient.updateUser("", Credentials.from(userUpdate));
        assertEquals(SC_UNAUTHORIZED, responseUpdateUser.extract().statusCode());
        assertEquals("You should be authorised", responseUpdateUser.extract().path("message"));
    }

    @Test
    public void updateUserOnExistEmail403(){
        ValidatableResponse responseCreate = userClient.createUser(Credentials.from(user));
        ValidatableResponse responseCreateSecondUser = userClient.createUser(Credentials.from(userSecond));
        accessToken = responseCreate.extract().path("accessToken");
        String accessTokenSecond = responseCreateSecondUser.extract().path("accessToken");
        ValidatableResponse responseUpdateUser = userClient.updateUser(accessToken, Credentials.from(userSecond));
        assertEquals(SC_FORBIDDEN, responseUpdateUser.extract().statusCode());
        assertEquals("User with such email already exists", responseUpdateUser.extract().path("message"));
        userClient.deleteUser(accessTokenSecond);
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }
}