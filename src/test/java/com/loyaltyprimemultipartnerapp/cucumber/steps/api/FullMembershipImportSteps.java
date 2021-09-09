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

public class FullMembershipImportSteps extends TestBase {

	String jsonFilePath;
	private Response response = null; // Response
	private Response responseME = null; // Response

	private String importType = "FullMembership";
	private String batchId = "12345";
	private String strKey="fullImport_mship001_key";

	@Given("user check for bulk import for full membership")
	public void user_check_for_bulk_import_for_full_membership() {
		System.out.println("Check import ");
	}

	@When("user perform the bulk import for full membership")
	public void user_perform_the_bulk_import_for_full_membership() throws InterruptedException {
		jsonFilePath = LoadProperties().getProperty("fullmemberimportfilejson");
		/*
		 * File jsonDataInFile = new File(jsonFilePath); responseME =
		 * SerenityRest.given().spec(ReuseableSpecifications.
		 * getGenericRequestSpecJsonFile())
		 * .body(jsonDataInFile).log().all().when().post("/api/v10/imports/"+
		 * importType_E+"");
		 */

		/*
		 * RequestSpecification request =
		 * SerenityRest.given().urlEncodingEnabled(false).
		 * config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().
		 * appendDefaultContentCharsetToContentTypeIfUndefined(false)))
		 * .multiPart("file", uploadFile, "csv"); Response r = request.put(URL);
		 */

		/*
		 * String csvFilePath=LoadProperties().getProperty("fullmemberimportfile"); File
		 * csvDataInFile = new File(csvFilePath);
		 * 
		 * response = SerenityRest.given().urlEncodingEnabled(false)
		 * .header("Content-Type","text/csv") .header("Authorization", APIKey)
		 * .body(csvDataInFile).log().all() .when() .post("/api/v10/csvimports/"+
		 * importType_E +""); System.out.println("Csv file API response:"+ response);
		 * 
		 */
		File jsonDataInFile = new File(jsonFilePath);
		responseME = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(jsonDataInFile)
				.log().all().when().post("/api/v10/imports/" + importType + "?batchId=" + batchId + "");

		Thread.sleep(20000);
	}

	@Then("bulk import for full membership file is successfully with status code {int}")
	public void bulk_import_for_full_membership_file_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseME.getStatusCode());

		JsonPath jsonPathEvaluator = responseME.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Success response of API " + getStatus);

		String strImportType = jsonPathEvaluator.get("data.importType");
		Assert.assertEquals(importType, strImportType);
	}

	@Given("user check for import processed for full membership")
	public void user_check_for_import_processed_for_full_membership() {
		System.out.println("import processed for full membership");
	}

	@When("user perform check import processed for full membership")
	public void user_perform_check_import_processed_for_full_membership() throws InterruptedException {

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile()).log().all().when()
				.get("/api/v10/imports/" + batchId + "");

		Thread.sleep(20000);
	}

	@Then("import processed is successfully with status code {int}")
	public void import_processed_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String strbatchId = jsonPathEvaluator.get("data.key");
		Assert.assertEquals(batchId, strbatchId);

		/*
		 * String strSuccessRecords = jsonPathEvaluator.get("data.successfulRecords");
		 * Assert.assertEquals("6", strSuccessRecords);
		 * 
		 * String strfailRecord = jsonPathEvaluator.get("data.failedRecords");
		 * Assert.assertEquals("4", strfailRecord);
		 */

	}

	@Given("user check for member information")
	public void user_check_for_member_information() {
		System.out.println("user check for member information");
	}

	@When("user perform get member details of imported member")
	public void user_perform_get_member_details_of_imported_member() {
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				.log()
				.all()
				.when().get("/api/v10/memberships/"+strKey+"/details");
	}

	@Then("member information should be displayed successfully with status code {int}")
	public void member_information_should_be_displayed_successfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
}
