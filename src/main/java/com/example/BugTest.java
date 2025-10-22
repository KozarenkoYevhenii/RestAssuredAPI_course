package com.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.*;

public class BugTest {
    public static void main(String[] args) {

        RestAssured.baseURI = "https://kozarenk007.atlassian.net/";

        String createIssueResponse = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic a296YXJlbmswMDdAZ21haWwuY29tOkFUQVRUM3hGZkdGMEN3RFJ3Z01UUlFtMHg5dF9WSENjR0VUN0laODF1dVlNd2hsT1QxVnAxRGttYzNYY0hoVmtHd3VpSzVWUlEyZGZxR2FVNzlHUjFoN3gxRElpRTlQZ3RCRmZsNHM2SkM0bGFoNEhNTno3ejgxa253UG9oWTdRaTJKaU54R0luelNONm5VX193bWRIUWt1UjJ0RHpzUWxMa1hYZkp6S09jQmVBUWNYUy03bzlKST04QjdBOEY3OQ==")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "       \"project\":\n" +
                        "       {\n" +
                        "          \"key\": \"SCRUM\"\n" +
                        "       },\n" +
                        "       \"summary\": \"Created via REST Assured with attachment\",\n" +
                        "       \"description\": {\n" +
                        "            \"type\": \"doc\",\n" +
                        "            \"version\": 1,\n" +
                        "             \"content\": [\n" +
                        "                 {\n" +
                        "                 \"type\": \"paragraph\",\n" +
                        "                 \"content\": [\n" +
                        "                            {\n" +
                        "                                 \"text\": \"Don't forget to do this too.\",\n" +
                        "                                 \"type\": \"text\"\n" +
                        "                            }\n" +
                        "                            ]\n" +
                        "                            }\n" +
                        "                        ]\n" +
                        "         },\n" +
                        "       \"issuetype\": {\n" +
                        "          \"name\": \"Bug\"\n" +
                        "       }\n" +
                        "   }\n" +
                        "}")
                .log().all()
                .post("rest/api/3/issue").then().log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js = new JsonPath(createIssueResponse);
        String issuedId = js.getString("id");
        System.out.println(issuedId);

        //Add attachment

        given()
                .pathParam("key", issuedId)
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic a296YXJlbmswMDdAZ21haWwuY29tOkFUQVRUM3hGZkdGMEN3RFJ3Z01UUlFtMHg5dF9WSENjR0VUN0laODF1dVlNd2hsT1QxVnAxRGttYzNYY0hoVmtHd3VpSzVWUlEyZGZxR2FVNzlHUjFoN3gxRElpRTlQZ3RCRmZsNHM2SkM0bGFoNEhNTno3ejgxa253UG9oWTdRaTJKaU54R0luelNONm5VX193bWRIUWt1UjJ0RHpzUWxMa1hYZkp6S09jQmVBUWNYUy03bzlKST04QjdBOEY3OQ==")
                .multiPart("file", new File("C:/COMET/Test data/cute-possum-with-photo-camera.jpg"))
                .log().all()
                .post("rest/api/3/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);

    }
}
