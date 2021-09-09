package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import com.loyaltyprimemultipartnerapp.testbase.TestBase;
import com.loyaltyprimemultipartnerapp.utlis.AIPsJsonBody;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.loyaltyprimemultipartnerapp.utlis.TestUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class BulkImportProductsSteps extends TestBase {
	String jsonFilePath;
	String strapiBody;
	private String sourceKey;// source key
	private String targetKey;// target member
	private Response response = null; // Response

	String importType = "Product";
	String batchId = "12345";

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	public static String gettargetKey() {
		return _targetKey;
	}

	private static String _targetKey;
	
	@Given("user check for bulk import of product csv file")
	public void user_check_for_bulk_import_of_product_csv_file() {
	   
	}

	@When("user perform member enrollmnet for product csv file import")
	public void user_perform_member_enrollmnet_for_product_csv_file_import() {
		sourceKey = LoadProperties().getProperty("enkey") + "_SRIMP" + TestUtils.getRandomValue();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRIMP_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = _sourceKey;
		// _sourceLegalEt=strUserId;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, sourceKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}
	@Then("member is enroll successfully for product csv with status code {int}")
	public void member_is_enroll_successfully_for_product_csv_with_status_code(Integer code) {
	   
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user again perform member enrollmnet for product csv file import")
	public void user_again_perform_member_enrollmnet_for_product_csv_file_import() {
	    
		targetKey = LoadProperties().getProperty("enkey") + "_TRM" + TestUtils.getRandomValue();
		_targetKey = targetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TRM_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = _targetKey;
		// _sourceLegalEt=strUserId;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, targetKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}
	@Then("new member is enroll successfully for product csv with status code {int}")
	public void new_member_is_enroll_successfully_for_product_csv_with_status_code(Integer code) {
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user perform the bulk import of product csv file")
	public void user_perform_the_bulk_import_of_product_csv_file() throws InterruptedException {
		
		String sourceKey = getsourceKey();
		String channelKey="RETAIL";
		String sid="RETAIL-PROD:1";
		String id="{{currentKey}}_Prod1";
		String name="Product Test"+ TestUtils.getRandomValue();
		String category="Accessories";
		
		String currentKey1=gettargetKey();
		String Id1=gettargetKey()+"_Prod1";
		String name1="Product Test"+ TestUtils.getRandomValue();
		

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.BulkImportProducts(sourceKey,channelKey, sid, id, name, category, currentKey1, Id1, name1);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/csvimports/" + importType + "");

		Thread.sleep(1000);
	}

	@Then("bulk import of product csv file is successful with status code {int}")
	public void bulk_import_of_product_csv_file_is_successful_with_status_code(Integer code) throws InterruptedException {
	
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
		Thread.sleep(1000);
	}

}
