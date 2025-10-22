package com.example;

import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class sumValidations {
    @Test

    public void sumOfCourses() {

        JsonPath js = new JsonPath(payload.CoursePrice());
        int sum = 0;
        for(int i = 0; i < js.getInt("courses.size()"); i++) {
            int price = js.get("courses["+i+"].price");
            int copies = js.get("courses["+i+"].copies");
            sum = sum + (price * copies);
        }
        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("sum = " + sum);
        System.out.println("purchaseAmount = " + purchaseAmount);
        System.out.println("git test");
        Assert.assertEquals(sum, purchaseAmount);
    }
}
