package com.example;

import POJO.GetCourse;
import POJO.api;
import POJO.webAutomation;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OAuth {
    public static void main(String[] args) {

        String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};

        RestAssured.baseURI = "https://rahulshettyacademy.com/";

        String response = given()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when().log().all()
                .post("oauthapi/oauth2/resourceOwner/token").asString();

        JsonPath js = new JsonPath(response);
        String access_token = js.getString("access_token");

        GetCourse response2 = given()
                .queryParam("access_token", access_token)
                .when().log().all()
                .get("oauthapi/getCourseDetails").as(GetCourse.class);

        System.out.println(response2.getCourses().getApi().get(1).getCourseTitle());

        List<api> apiCourses = response2.getCourses().getApi();
        for(int i=0;i<apiCourses.size();i++)
        {
            if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
            {
                System.out.println(apiCourses.get(i).getPrice());
            }

        }

        ArrayList<String> receivedTitlesList = new ArrayList<>();
        List<webAutomation> webAutomationCourses = response2.getCourses().getWebAutomation();
        for(int i=0;i<webAutomationCourses.size();i++)
        {
            receivedTitlesList.add(webAutomationCourses.get(i).getCourseTitle());
            //System.out.println(webAutomationCourses.get(i).getCourseTitle());
        }

        List<String> expectedList = Arrays.asList(courseTitles);
        Assert.assertTrue(receivedTitlesList.equals(expectedList));

    }
}
