package User;

import io.restassured.response.ValidatableResponse;
import praktikum.Client;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private static final String PATH_CREATE = "/api/auth/register";
    private static final String PATH_AUTH = "/api/auth/login";
    private static final String PATH_UPDATE_USER = "/api/auth/user";
    private static final String PATH_DELETE = "/api/auth/user";

    public ValidatableResponse createUser(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    public ValidatableResponse loginUser(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(PATH_AUTH)
                .then();
    }

    public ValidatableResponse updateUser(String accessToken, Credentials credentials) {
        return given()
                .header("Authorization", accessToken)
                .header("Accept", "*/*")
                .spec(getSpec())
                .body(credentials)
                .when()
                .patch(PATH_UPDATE_USER)
                .then();
    }

    public void deleteUser(String accessToken) {
        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .header("Accept", "*/*")
                    .spec(getSpecForDelete())
                    .delete(PATH_DELETE)
                    .then();
        }
    }
}
