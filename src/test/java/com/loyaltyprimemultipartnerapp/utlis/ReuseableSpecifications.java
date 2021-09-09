package com.loyaltyprimemultipartnerapp.utlis;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.serenitybdd.rest.SerenityRest;

import static org.hamcrest.Matchers.*;

import java.util.concurrent.TimeUnit;

public class ReuseableSpecifications {

	public static RequestSpecBuilder rspec;
	public static RequestSpecification requestSpecification;
	//public static String APIKey="ApiKey sKySDdGhc05FGPHbqBWo/jHNaKfFrOzeWY94F+CRLpo=";
	public static String APIKey = "ApiKey 123456789!";

	public static ResponseSpecBuilder respec;
	public static ResponseSpecification responseSpecification;

	public static RequestSpecification getGenericRequestSpec() {

		rspec = new RequestSpecBuilder();
		rspec.setContentType(ContentType.JSON);
		rspec.addHeader("Authorization", APIKey);// Pass the access token
		requestSpecification = rspec.build();
		return requestSpecification;

	}

	public static ResponseSpecification getGenericResponseSpec() {
		respec = new ResponseSpecBuilder();
		respec.expectHeader("Content-Type", "application/json;charset=UTF-8");
		respec.expectHeader("Transfer-Encoding", "chunked");
		respec.expectResponseTime(lessThan(5L), TimeUnit.SECONDS);
		responseSpecification = respec.build();
		return responseSpecification;

	}

	public static RequestSpecification getGenericRequestSpecCsv() {

		rspec = new RequestSpecBuilder();
		rspec.setContentType(ContentType.TEXT);
		// rspec.setContentType("Content-Type","multipart/form-data");
		rspec.addHeader("Authorization", APIKey);// Pass the access token
		requestSpecification = rspec.build();
		return requestSpecification;

	}

	public static RequestSpecification getGenericRequestSpecQueryParm() {

		// RequestSpecification mySpec = (RequestSpecification) new
		// RequestSpecBuilder();

		rspec = new RequestSpecBuilder().setUrlEncodingEnabled(false);
		rspec.setContentType(ContentType.JSON);
		rspec.addHeader("Authorization", APIKey);// Pass the access token
		requestSpecification = rspec.build();
		return requestSpecification;

	}

	// SerenityRest.given().header("Content-Type",
	// "application/json").header("Authorization", APIKey)
	// .body(jsonDataInFile).log().all().when().post("/api/v10/imports/" +
	// importType + "");

	public static RequestSpecification getGenericRequestSpecJsonFile() {

		rspec = new RequestSpecBuilder();
		rspec.setContentType(ContentType.JSON);
		rspec.addHeader("Authorization", APIKey);// Pass the access token
		requestSpecification = rspec.build();
		return requestSpecification;

	}
}
