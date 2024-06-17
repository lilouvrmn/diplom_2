package order;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.Credentials;
import user.User;
import user.UserClient;
import user.UserGenerator;


import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderTest {

    private User user;
    private Ingredients ingredients;
    private Ingredients ingredientsEmpty;
    private Ingredients ingredientsIncorrect;
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        ingredients = IngredientsGenerator.getIngredients();
        ingredientsEmpty = IngredientsGenerator.getIngredientsEmpty();
        ingredientsIncorrect = IngredientsGenerator.getIngredientsIncorrect();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @Test
    public void createOrderWithAuthorization200(){
        userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.from(user));
        accessToken = responseLogin.extract().path("accessToken");
        ValidatableResponse responseOrderCreate = orderClient.createOrder(ingredients, accessToken);
        assertEquals(SC_OK, responseOrderCreate.extract().statusCode());
        assertTrue(responseOrderCreate.extract().path("success"));
    }

    @Test
    public void createOrderWithOutAuthorization200(){
        ValidatableResponse responseUserCreate = userClient.createUser(Credentials.from(user));
        accessToken = responseUserCreate.extract().path("accessToken");
        ValidatableResponse responseOrderCreate = orderClient.createOrderWithoutAuthorization(ingredients);
        assertEquals(SC_OK, responseOrderCreate.extract().statusCode());
        assertTrue(responseOrderCreate.extract().path("success"));
    }

    @Test
    public void createOrderWithOutIngredients400(){
        ValidatableResponse responseUserCreate = userClient.createUser(Credentials.from(user));
        accessToken = responseUserCreate.extract().path("accessToken");
        ValidatableResponse responseOrderCreate = orderClient.createOrderWithoutAuthorization(ingredientsEmpty);
        assertEquals(SC_BAD_REQUEST, responseOrderCreate.extract().statusCode());
        assertEquals("Ingredient ids must be provided", responseOrderCreate.extract().path("message"));
    }

    @Test
    public void createOrderWithIncorrectHashIngredients500(){
        ValidatableResponse responseUserCreate = userClient.createUser(Credentials.from(user));
        accessToken = responseUserCreate.extract().path("accessToken");
        ValidatableResponse responseOrderCreate = orderClient.createOrderWithoutAuthorization(ingredientsIncorrect);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseOrderCreate.extract().statusCode());
    }

    @Test
    public void getOrdersForUser200(){
        int totalOrder = 1;
        userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.from(user));
        accessToken = responseLogin.extract().path("accessToken");
        orderClient.createOrder(ingredients, accessToken);
        ValidatableResponse responseGetOrders = orderClient.getOrdersForUser(accessToken);
        ArrayList<Object> orders = responseGetOrders.extract().path("orders");
        assertEquals(SC_OK, responseGetOrders.extract().statusCode());
        assertTrue(responseGetOrders.extract().path("success"));
        assertEquals(totalOrder, orders.size());
    }

    @Test
    public void getOrdersForUserWithoutAuthorization401(){
        userClient.createUser(Credentials.from(user));
        ValidatableResponse responseLogin = userClient.loginUser(Credentials.from(user));
        accessToken = responseLogin.extract().path("accessToken");
        orderClient.createOrder(ingredients, accessToken);
        ValidatableResponse responseGetOrders = orderClient.getOrdersForUser("");
        assertEquals(SC_UNAUTHORIZED, responseGetOrders.extract().statusCode());
        assertFalse(responseGetOrders.extract().path("success"));
        assertEquals("You should be authorised", responseGetOrders.extract().path("message"));
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }
}