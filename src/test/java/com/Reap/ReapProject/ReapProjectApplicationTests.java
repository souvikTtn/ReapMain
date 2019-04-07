package com.Reap.ReapProject;

import com.jayway.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReapProjectApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public  void canary(){
		Assert.assertEquals(1,1);
	}

	@Test
	public void testingGoogle(){
		RestAssured.given().get("https://www.google.com/").then().statusCode(200);
	}
	String uri="users/{id}";

	@Test
	public void validPostTest() {
		RestAssured.given().pathParam("id",1)
				.get(uri).then()
				.statusCode(200).log().all();
	}

	@Test
	public void invalidPostTest() {
		RestAssured.given().pathParam("id",1000)
				.get(uri).then()
				.statusCode(200).log().all();
	}


}
