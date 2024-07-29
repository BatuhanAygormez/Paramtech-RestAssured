package utilities;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class TestBase {

    @BeforeClass
    public static void init() {

        // save baseurl inside this variable so that we do not need to type each http
        // method
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

    }

}
