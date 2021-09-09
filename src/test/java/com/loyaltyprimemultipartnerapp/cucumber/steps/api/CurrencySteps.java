package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.File;

import org.junit.Assert;

import com.loyaltyprimemultipartnerapp.testbase.TestBase;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class CurrencySteps extends TestBase {

	private Response response = null; // Response
	private String jsonFilePath="";
	private String importType="Currency";

	@Given("user check for currency conversation")
	public void user_check_for_currency_conversation() {
		System.out.println("check currency");
	}

	@When("user perform import currency")
	public void user_perform_import_currency() throws InterruptedException {

		jsonFilePath = LoadProperties().getProperty("currencyfile");
		File jsonDataInFile = new File(jsonFilePath);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile())
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/"+ importType+"");
	

		Thread.sleep(20000);
	}

	@Then("currency is imported is successfully with status code {int}")
	public void currency_is_imported_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform the convert currency")
	public void user_perform_the_convert_currency() throws InterruptedException {

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile()).log().all().when()
				.get("/api/v10/deposits/currency/BPT/convert/100?targetCcy=USD");
		Thread.sleep(20000);
	}

	@Then("currency conversion is successfully with status code {int}")
	public void currency_conversion_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

}
