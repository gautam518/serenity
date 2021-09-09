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

public class BalanceApiSteps extends TestBase {

	String jsonFilePath;
	String strapiBody;
	private String sourceKey;// source key
	private Response response = null; // Response

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	@Given("user check for member balance api")
	public void user_check_for_member_balance_api() {
		System.out.println("Balance api");
	}

	@When("user perform member enrollmnet for balance")
	public void user_perform_member_enrollmnet_for_balance() {

		sourceKey = LoadProperties().getProperty("enkey") + "_SRBLN" + TestUtils.getRandomValue();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRBLN_AltId" + TestUtils.getRandomValue()
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
	

	@Then("member enroll is successful for member balance")
	public void member_enroll_is_successful_for_member_balance() {
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user performs points adjust for balance")
	public void user_performs_points_adjust_for_balance() {
		String date = TestUtils.getDateTime();
		String receiptId = "TesADjpt" + TestUtils.getRandomNumber();
		String debitorId = LoadProperties().getProperty("debitorId");
		Double amount = 100.0;
		String description = "Test Adjustment";
		String clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");
	}
	
	@Then("point adjustment is succesful for member balance with status code {int}")
	public void point_adjustment_is_succesful_for_member_balance_with_status_code(Integer code) {
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}
	
	@When("user perform get member balance")
	public void user_perform_get_member_balance() {

		String key=getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				.log().all().when().get("/api/v10/deposits/"+key+"/balance?type=Bonus points");
	}
	@Then("member balance is displayed successfully with status code {int}")
	public void member_balance_is_displayed_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	
	
}
