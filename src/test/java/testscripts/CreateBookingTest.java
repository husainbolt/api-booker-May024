package testscripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.request.createbooking.Bookingdates;
import pojo.request.createbooking.CreateBookingRequest;

public class CreateBookingTest {
//given - all inputs [URI, headers, path/query parameters, payload]	
//when - submit api [headers/endpoint]
//then - validate
	
	CreateBookingRequest payload;
	String token;
	int bookingid;

	@BeforeMethod
	void generateBooking() {

		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
		Response res = RestAssured.given().headers("Content-Type", "application/json").log().all()
				.body("{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}")
				.when().post("/auth");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		token = res.jsonPath().getString("token");
		System.out.println(token);

	}

	@Test(enabled = false)
	public void createBookingTest() {
		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json")
				.body("{\r\n" + "    \"firstname\" : \"Bukayo\",\r\n" + "    \"lastname\" : \"Saka\",\r\n"
						+ "    \"totalprice\" : 111,\r\n" + "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n" + "        \"checkin\" : \"2018-01-01\",\r\n"
						+ "        \"checkout\" : \"2019-01-01\"\r\n" + "    },\r\n"
						+ "    \"additionalneeds\" : \"Breakfast\"\r\n" + "}")
				.when().post("/booking");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);

	}

	@Test(enabled = false)
	public void createBookingTestinPlan() {
		RequestSpecification reqspec = RestAssured.given();
		String payload = "{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}";
		reqspec.baseUri("https://restful-booker.herokuapp.com");
		reqspec.headers("Content-Type", "application/json");
		reqspec.body(payload);
		Response res = reqspec.post("/auth");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
	}

	@Test
	public void createBookingTestWithPojo() {

		payload = new CreateBookingRequest();
		Bookingdates dates = new Bookingdates();
		dates.setCheckin("2018-01-01");
		dates.setCheckout("2019-01-01");
		payload.setFirstname("Bukayo");
		payload.setLastname("Saka");
		payload.setTotalprice(111);
		payload.setDepositpaid(true);
		payload.setBookingdates(dates);
		payload.setAdditionalneeds("breakfast");
		

		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").body(payload).log().all().when().post("/booking");
		bookingid = res.jsonPath().getInt("bookingid");
		System.out.println(bookingid);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);		
		Assert.assertTrue(bookingid > 0);
		validateResponse(res, payload, "booking.");
		
	}

	@Test(priority = 1, enabled = false)
	public void getAllBookings() {
		Response res = RestAssured.given().headers("Accept", "application/json").log().all().when().get("/booking");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		List<Integer> listOfBookingsids = res.jsonPath().getList("bookingid");
		System.out.println(listOfBookingsids.size());
		Assert.assertTrue(listOfBookingsids.contains(bookingid));
	}
	
	@Test(priority = 2, enabled = false)
	public void getBookingID() {
		Response res = RestAssured.given().headers("Accept", "application/json").log().all().when().get("/booking/"+bookingid);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
		validateResponse(res, payload, "");
	}
	
	@Test()
	public void getBookingIDWithDeserialization() {
		Response res = RestAssured.given().headers("Accept", "application/json").log().all().when().get("/booking/"+bookingid);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		System.out.println(res.asPrettyString());
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		//Assert.assertEquals(payload.firstname, responseBody.firstname);
		//Assert.assertEquals(payload.lastname, responseBody.lastname);
		Assert.assertTrue(payload.equals(responseBody));
	}
	
	private void validateResponse(Response res, CreateBookingRequest payload, String object) {
		
		Assert.assertEquals(res.jsonPath().getString(object+"firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(object+"lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(object+"totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getString(object+"additionalneeds"), payload.getAdditionalneeds());
		Assert.assertEquals(res.jsonPath().getBoolean(object+"depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().get(object+"bookingdates.checkin"), payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().get(object+"bookingdates.checkout"), payload.getBookingdates().getCheckout());

		
	}

}
