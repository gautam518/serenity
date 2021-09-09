package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

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

public class MemberStatusSteps extends TestBase {

	String strapiBody;
	private String sourceKey;// source key

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	public static String getcancelMember() {
		return _cancelMember;
	}

	private static String _cancelMember;

	public static String getblockedMember() {
		return _blockedMember;
	}

	private static String _blockedMember;

	public static String getincativeMember() {
		return _incativeMember;
	}

	private static String _incativeMember;

	Response response = null;
	Response responseMDTE = null;
	Response responseMBLK = null;
	Response responseIVMB = null;

	@Given("member is enroll for membership status")
	public void member_is_enroll_for_membership_status() {
		sourceKey = LoadProperties().getProperty("enkey") + "_PSRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_PSRC_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = sourceKey;
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
	}

	@When("user perform enrollment of member for membership status")
	public void user_perform_enrollment_of_member_for_membership_status() {
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("member is created for membership status with statuscode {int}")
	public void member_is_created_for_membership_status_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("user check the member status")
	public void user_check_the_member_status() {
		System.out.println("user check the member status");
	}

	@When("user perform membership status of member")
	public void user_perform_membership_status_of_member() {

		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/statusCheck");
	}

	@Then("member status is displayed successfully with status code {int}")
	public void member_status_is_displayed_successfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform membership status of invalid member")
	public void user_perform_membership_status_of_invalid_member() {
		sourceKey = getsourceKey() + "Invalid";
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/statusCheck");
	}

	@When("user perform membership update for member as cancelled")
	public void user_perform_membership_update_for_member_as_cancelled() {

		sourceKey = getsourceKey();
		_cancelMember = sourceKey;
		responseMDTE = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				// .body(strapiBody)
				.log().all().when().post("/api/v10/memberships/" + sourceKey + "/cancel");
	}

	@Then("membership update for member is successful with status code {int}")
	public void membership_update_for_member_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseMDTE.getStatusCode());

		JsonPath jsonPathEvaluator = responseMDTE.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

	}

	@When("user perform membership status of cancelled member")
	public void user_perform_membership_status_of_cancelled_member() {
		sourceKey = getcancelMember();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/statusCheck");
	}

	@When("user perform membership update for member as blocked")
	public void user_perform_membership_update_for_member_as_blocked() {

		strapiBody = "{\r\n" + "\r\n" + "  \"contractStatus\": \"BLOCKED\",\r\n" + "  \r\n" + "}";
		sourceKey = getsourceKey();
		_blockedMember = sourceKey;
		responseMBLK = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships/" + sourceKey + "/cancel");
	}

	@Then("membership status is marked as blocked successful with status code {int}")
	public void membership_status_is_marked_as_blocked_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseMBLK.getStatusCode());

		JsonPath jsonPathEvaluator = responseMBLK.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform membership status of blocked member")
	public void user_perform_membership_status_of_blocked_member() {
		sourceKey = getblockedMember();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/statusCheck");

	}

	@Then("user should not be able to see membership status with status code {int}")
	public void user_should_not_be_able_to_see_membership_status_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Then("user sees the error message as {string}")
	public void user_sees_the_error_message_as(String strMsg) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		String getErrorMsg = jsonPathEvaluator.get("errDetails.msgTemplate");
		Assert.assertEquals(getErrorMsg, strMsg);
	}

	@When("user perform membership update for member as inactive")
	public void user_perform_membership_update_for_member_as_inactive() {

		strapiBody = "{\r\n" + "\r\n" + "  \"contractStatus\": \"INACTIVE\",\r\n" + "  \r\n" + "}";
		sourceKey = getsourceKey();
		_incativeMember = sourceKey;
		responseIVMB = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships/" + sourceKey + "/cancel");
	}

	@Then("membership status is marked as inactive successful with status code {int}")
	public void membership_status_is_marked_as_inactive_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseIVMB.getStatusCode());

		JsonPath jsonPathEvaluator = responseIVMB.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform membership status of inactive member")
	public void user_perform_membership_status_of_inactive_member() {
		sourceKey = getincativeMember();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/statusCheck");
	}
}
