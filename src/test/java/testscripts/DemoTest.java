package testscripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DemoTest {

	@Test
	public void phoneNumbersTypeTest() {

		RestAssured.baseURI = "https://ab544611-46e1-4ce4-afb3-ece1a3cbc28f.mock.pstmn.io";
		Response res = RestAssured.given().headers("Accept", "application/json").log().all().when().get("/test");
		System.out.println(res.asPrettyString());
		List<String> listOfType = res.jsonPath().getList("phoneNumbers.type");
		List<String> expectedList = new ArrayList<String>(Arrays.asList("iPhone", "home"));
		System.out.println(listOfType);
		Assert.assertEquals(listOfType, expectedList);

	}

	@Test
	public void phoneNumbersTest() {

		RestAssured.baseURI = "https://ab544611-46e1-4ce4-afb3-ece1a3cbc28f.mock.pstmn.io";
		Response res = RestAssured.given().headers("Accept", "application/json").log().all().when().get("/test");
		System.out.println(res.asPrettyString());
		List<Object> listOfPhoneNumbers = res.jsonPath().getList("phoneNumbers");
		for (Object obj : listOfPhoneNumbers) {
			Map<String, String> map = (Map<String, String>) obj;
			if (map.get("type").equals("iPhone"))
				Assert.assertTrue(map.get("number").startsWith("0123"));
			else if (map.get("type").equals("home"))
				Assert.assertTrue(map.get("number").startsWith("3456"));
			System.out.println(map.get("type") + " " + map.get("number"));
		}

	}

}
