package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    
    @Test(dataProvider = "BooksData")
    public void addBooks(String isbn, String aisle)
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().header("Content-type", "application/json")
        .body(payload.Addbook(isbn, aisle))
        .when().post("Library/Addbook.php")
        .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReUsableMethods.rawToJson(response);
        String id = js.get("ID");
        System.out.println(id);
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData()
    {
        return new Object[][] {{"asdfd", "1273"}, {"dsdfsf", "2343"}, {"werwrw", "3454"}};
    }
}
