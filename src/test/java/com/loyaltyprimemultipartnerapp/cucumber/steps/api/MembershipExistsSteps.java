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

public class MembershipExistsSteps extends TestBase {

	String jsonFilePath;
	String strapiBody;
	private String sourceKey;// source key
	private Response response = null; // Response

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	@Given("user check membership exists api")
	public void user_check_membership_exists_api() {
		System.out.println("member existence");
	}

	@When("user perform member enrollmnet for membership exists")
	public void user_perform_member_enrollmnet_for_membership_exists() {

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

	@Then("member enroll is successful for membership exists")
	public void member_enroll_is_successful_for_membership_exists() {

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user performs get membership exists")
	public void user_performs_get_membership_exists() {

		String key = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + key + "/statusCheck");

	}

	@Then("member exists is successful with status code {int}")
	public void member_exists_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		
		String  getActive = jsonPathEvaluator.get("data.contractStatus");
		Assert.assertEquals(getActive, "ACTIVE");
	}

}
