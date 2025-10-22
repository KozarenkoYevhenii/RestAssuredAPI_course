package com.example;

import files.ReUsableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Main {
    public static void main(String[] args) {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        //POST place

        String response = given().log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json").body(payload.AddPlace())
                .when().post("maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        JsonPath js = ReUsableMethods.rawToJson(response);
        String placeId = js.getString("place_id");

        System.out.println(placeId);

        //Update

        String newAddress = "Lviv, Main street";

        given().log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n\"place_id\":\"" + placeId + "\",\n\"address\":\"" + newAddress
                        + "\",\n\"key\":\"qaclick123\"\n}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //Get updated place

        String getPlaceResponse = given().log().all().queryParam("place_id", placeId).queryParam("key", "qaclick123")
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        System.out.println(getPlaceResponse);

        JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);
        //JsonPath js1 = new JsonPath(getPlaceResponse);
        String actualAddress = js1.getString("address");
        System.out.println(actualAddress);

        Assert.assertEquals(actualAddress, newAddress);

        //Complex JSON retrieving

        System.out.println("!!!Complex JSON task!!!");
        JsonPath dummyJson = ReUsableMethods.rawToJson(payload.CoursePrice());
        //String[] courses = dummyJson.getString("courses").split("],");
        //System.out.println("No of courses: " + courses.length);
        System.out.println("No of courses: " + dummyJson.getInt("courses.size()"));
        System.out.println("Purchase amount: " + dummyJson.getInt("dashboard.purchaseAmount"));
        System.out.println("Title of the first course: " + dummyJson.get("courses[0].title"));

        //Print All course titles and their respective Prices
        for( int i=0; i<dummyJson.getInt("courses.size()"); i++)
        {
            System.out.print(dummyJson.get("courses["+i+"].title").toString() + " ");
            System.out.println(dummyJson.get("courses["+i+"].price").toString());
        }

        //Print number of copies sold by RPA course
        for(int i = 0; i < dummyJson.getInt("courses.size()"); i++) {
            String title = dummyJson.get("courses["+i+"].title").toString();
            if (title.equals("RPA")) {
                System.out.println(dummyJson.get("courses["+i+"].copies").toString());
                break;
            }
        }

        //If sum of courses equals to purchase amount
        int sum = 0;
        for(int i = 0; i < dummyJson.getInt("courses.size()"); i++) {
            int price = dummyJson.get("courses["+i+"].price");
            int copies = dummyJson.get("courses["+i+"].copies");
            sum = sum + (price * copies);
        }
        int purchaseAmount = dummyJson.getInt("dashboard.purchaseAmount");
        System.out.println("sum = " + sum);
        System.out.println("purchaseAmount = " + purchaseAmount);
        Assert.assertEquals(sum, purchaseAmount);

    }
    
}