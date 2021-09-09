package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.util.List;

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

public class PointTransferAccountIdsSteps extends TestBase {

	String strapiBody;
	Response response = null;
	Response responseVCGNe = null;
	Response responsePT = null;
	Response responseAccountsIds = null;
	private String TrxDate;
	private String TrxId;
	private String sourceKey;// source key
	private String targetKey;// targetKey

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	public static String gettargetKey() {
		return _targetKey;
	}

	private static String _targetKey;

	public static String getTrxId() {
		return _TrxId;
	}

	private static String _TrxId;

	public static String getTrxDate() {
		return _trxDate;
	}

	private static String _trxDate;

	public static String getRcptId() {
		return _RcptId;
	}

	private static String _RcptId;

	public static String getsourceVoucherToken() {
		return _sourceVoucherToken;
	}

	private static String _sourceVoucherToken;

	public static String getsourceVoucherName() {
		return _sourceVoucherName;
	}

	private static String _sourceVoucherName;

	public static String getsourceVoucher() {
		return _sourceVoucher;
	}

	private static String _sourceVoucher;

	public static String getacctId() {
		return _acctId;
	}

	private static String _acctId;

	Response responseTM = null;
	Response responseVCissue = null;
	Response responseVCTpe = null;
	Response responseSRCVTOKEN = null;
	Response responseRD = null;

	@Given("user check for member for accountids")
	public void user_check_for_member_for_accountids() {

	}

	@When("user perform enrollment for accountids as source member")
	public void user_perform_enrollment_for_accountids_as_source_member() {
		sourceKey = LoadProperties().getProperty("enkey") + "_VCSRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_VCSRC_AltId" + TestUtils.getRandomValue()
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
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");

	}

	@Then("enrollment for accountids is successful with status code {int}")
	public void enrollment_for_accountids_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform enrollment for accountids as target member")
	public void user_perform_enrollment_for_accountids_as_target_member() {

		targetKey = LoadProperties().getProperty("enkey") + "_TGT" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_targetKey = targetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TGT_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = targetKey;
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
		responseTM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("enrollment for accountids of target member is successful with status code {int}")
	public void enrollment_for_accountids_of_target_member_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTM.getStatusCode());

		JsonPath jsonPathEvaluator = responseTM.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user performs points adjust for accountids of source member")
	public void user_performs_points_adjust_for_accountids_of_source_member() {

		String date = TestUtils.getDateTime();
		String receiptId = "TesADj" + TestUtils.getRandomNumber();
		String debitorId = LoadProperties().getProperty("debitorId");
		Double amount = 100.0;
		String description = "Test Adjustment";
		String clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");
	}

	@Then("point adjustment is succesful for source member with status code {int}")
	public void point_adjustment_is_succesful_for_source_member_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform get accountids for source member")
	public void user_perform_get_accountids_for_source_member() {
		String clientKey = getsourceKey();
		responseAccountsIds = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/memberships/" + clientKey + "/accounts");
	}

	@Then("accountids details is successful with status code {int}")
	public void accountids_details_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseAccountsIds.getStatusCode());

		JsonPath jsonPathEvaluator = responseAccountsIds.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String getaccountId = jsonPathEvaluator.getString("data.accountId[0]");
		_acctId = getaccountId;
		System.out.println("accountid value:" + _acctId);
	}

	@When("user perform point transfer from source to target member for accountids")
	public void user_perform_point_transfer_from_source_to_target_member_for_accountids() {

		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("debitorId");
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();
		String aId = getsourceKey() + ".Bonus Points";
		// getacctId();
		System.out.println("get value here by pushkar:" + aId);
		// "testMshipKey_VCSRC829462.Bonus Points"

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responsePT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey
						+ "?type=BONUS&account=" + aId + "");
	}

	@Then("point transfer is successful with status code {int}")
	public void point_transfer_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responsePT.getStatusCode());

		JsonPath jsonPathEvaluator = responsePT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

}
