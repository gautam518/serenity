package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.http.HttpHeaders;
import org.junit.Assert;

import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.thoughtworks.xstream.io.path.Path;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;

public class ConfigurePrimeOMatSteps {

	private Response response = null; // Response
	HttpHeaders headers;
	String strapiBody;
	private String xmlFilePath;
	private String importType = "Enrollment";

	public static String APIKey = "ApiKey sKySDdGhc05FGPHbqBWo/jHNaKfFrOzeWY94F+CRLpo=";
	private RequestSpecification request;

	public Properties LoadProperties() {
		try {
			InputStream inStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			return prop;
		} catch (Exception e) {
			System.out.println("File not found exception thrown for config.properties file.");
			return null;
		}
	}

	@Given("Member call the prime-o-mat xml file")
	public void member_call_the_prime_o_mat_xml_file() {

		xmlFilePath = LoadProperties().getProperty("validXmlPath");
		System.out.println("path: " + xmlFilePath);
		
		request = SerenityRest.given();
		System.out.println("API body:" + request);
	}

	@When("Member perfom import for prime-o-mat xml file")
	public void member_perfom_import_for_prime_o_mat_xml_file() throws InterruptedException 
	{

		response = request.contentType(ContentType.XML)
				//header("Content-Type", "application/xml") //multipart/form-data
				.header("Authorization", APIKey).body(xmlFilePath).log().all()
				.when().post("/api/v10/internal/loyaltyops");

		System.out.println("XML file API response:" + response);

		Thread.sleep(1000);
		
	}

	@Then("The file should be processed in the system with status code {int}")
	public void the_file_should_be_processed_in_the_system_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

}
