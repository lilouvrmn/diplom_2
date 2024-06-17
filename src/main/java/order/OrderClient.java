package order;

import io.restassured.response.ValidatableResponse;
import user.Client;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String PATH_CREATE = "/api/orders";
    private static final String PATH_GET_ORDERS_USER = "/api/orders";

    public ValidatableResponse createOrder(Ingredients ingredients, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .header("Accept", "*/*")
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    public ValidatableResponse createOrderWithoutAuthorization(Ingredients ingredients) {
        return given()
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    public ValidatableResponse getOrdersForUser(String accessToken) {
        if (accessToken != null) {
            return given()
                    .header("Authorization", accessToken)
                    .header("Accept", "*/*")
                    .spec(getSpecForGetOrders())
                    .get(PATH_GET_ORDERS_USER)
                    .then();
        }
        return null;
    }
}
