package com.example;

import POJO.LoginRequest;
import POJO.LoginResponsePayload;
import POJO.Orders;
import POJO.Product;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ECommerceAPITest {

    public static void main(String[] args) {

       RequestSpecification request = new RequestSpecBuilder()
               .setBaseUri("https://rahulshettyacademy.com")
               .setContentType(ContentType.JSON).build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("kozarenk007+testing@gmail.com");
        loginRequest.setUserPassword("Qw!1111111");

       RequestSpecification reqLogin = given().relaxedHTTPSValidation().spec(request).body(loginRequest);
       LoginResponsePayload loginResponse = reqLogin.when().post("/api/ecom/auth/login")
               .then().extract().response().as(LoginResponsePayload.class);

       String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();

       //Add Product

        RequestSpecification addProductreq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();

        RequestSpecification addProductSpec = given().log().all().spec(addProductreq)
                .param("productName", "blob")
                .param("productAddedBy", userId)
                .param("productCategory", "fashion")
                .param("productSubCategory", "usless")
                .param("productPrice", "100500")
                .param("productDescription", "some staff")
                .param("productFor", "women")
                .multiPart("productImage", new File("C:/COMET/Test data/face_PNG11760.png"));

        String addProductRes = addProductSpec.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();
        JsonPath js = new JsonPath(addProductRes);
        String productId = js.get("productId");

        //Create order

        RequestSpecification createOrderreq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token)
                .setContentType(ContentType.JSON).build();

        Product product = new Product();
        product.setCountry("Ukraine");
        product.setProductOrderedId(productId);
        List<Product> list = new ArrayList<Product>();
        list.add(product);
        Orders orders = new Orders();
        orders.setOrders(list);

        RequestSpecification createOrderSpec = given().log().all().spec(createOrderreq).body(orders);

        String createOrderRes = createOrderSpec.when().post("/api/ecom/order/create-order")
                .then().log().all().extract().response().asString();

        //Delete order

        RequestSpecification deleteProductreq = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", token)
                .build();

        RequestSpecification deleteProductSpec = given().log().all().spec(deleteProductreq)
                .pathParam("productId", productId);

        String deleteResp = deleteProductSpec.when().delete("/api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();

        JsonPath deleteMessageResponse = new JsonPath(deleteResp);
        Assert.assertEquals(deleteMessageResponse.get("message"), "Product Deleted Successfully");

    }
}
