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

public class PointExpiryIntegrationSteps extends TestBase {

	String strapiBody;
	Response response = null;// for source member
	Response responsePL=null;
	Response responseACC=null;

	private String sourceIntKey;// source key
	public static String getsourceIntKey() {
		return _sourceIntKey;
	}

	private static String _sourceIntKey;

	public static String getTrxId() {
		return _TrxId;
	}

	private static String _TrxId;

	public static String getTrxDate() {
		return _trxDate;
	}
	private static String _trxDate;
	@Given("member point expiry for a member")
	public void member_point_expiry_for_a_member() {
		System.out.println("member point expiry for a member");
	}

	@When("user perform enrollment of a of member on date{string} for point expiry")
	public void user_perform_enrollment_of_a_of_member_on_date_for_point_expiry(String strDate) {
		
		sourceIntKey = LoadProperties().getProperty("enkey") + "_PSRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceIntKey = sourceIntKey;
		String strSince = strDate;
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_PSRC_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = sourceIntKey;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, sourceIntKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("member is created successful with status code {int}")
	public void member_is_created_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform earn transaction of member on date{string} for point expiry")
	public void user_perform_earn_transaction_of_member_on_date_for_point_expiry(String strTxDate)
			throws InterruptedException {

		String TrxId = "SRCNW_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = strTxDate;
		String price = "101";
		String quantity = "1";
		String clientKey =getsourceIntKey();
		String partnerId = LoadProperties().getProperty("partnerId");
		clientKey = getsourceIntKey();

		String channelkey = "";
		String productKey = "";
		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");
		String _getChannelKey = jsonPathEval.get("data[0].channelKey");

		channelkey = _getChannelKey;
		productKey = _getProductKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("the member earn transaction for point expiry is successful with status code {int}")
	public void the_member_earn_transaction_for_point_expiry_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEval = response.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform manual point adjustment of points{string}for point expiry")
	public void user_perform_manual_point_adjustment_of_points_for_point_expiry(String strPoints)
			throws InterruptedException {

		String clientKey = getsourceIntKey();
		String receiptId = "MaRcp_" + TestUtils.getRandomValue();
		String date = "";
		String amount = strPoints;
		String debitorId = "";
		String description = "Auto Adjustment";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		Thread.sleep(10000);
	}

	@Then("the member point adjustment for point expiry  is successful with status code {int}")
	public void the_member_point_adjustment_for_point_expiry_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEval = response.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform account statement for member balance")
	public void user_perform_account_statement_for_member_balance() {
		String clientKey = getsourceIntKey();

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}

	@Then("member blance is displayed successfully with status code {int}")
	public void member_blance_is_displayed_successfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEval = response.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("again user perform earn transaction of member on date{string} for point expiry")
	public void again_user_perform_earn_transaction_of_member_on_date_for_point_expiry(String strTxDate)
			throws InterruptedException {

		String TrxId = "SRCNWA_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = strTxDate;
		String price = "101";
		String quantity = "1";
		String clientKey = getsourceIntKey();
		String partnerId = LoadProperties().getProperty("partnerId");
		clientKey = getsourceIntKey();

		String channelkey = "";
		String productKey = "";
		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");
		String _getChannelKey = jsonPathEval.get("data[0].channelKey");

		channelkey = _getChannelKey;
		productKey = _getProductKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("member second earn transaction for point expiry is successful with status code {int}")
	public void member_second_earn_transaction_for_point_expiry_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEval = response.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user run the point unlock job")
	public void user_run_the_point_unlock_job() throws InterruptedException {
		String jobName = "PointsUnlock";

		String Url = "/api/v10/loyaltyops/" + jobName + "/schedule-now?schedule=%231";
		responsePL = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecQueryParm()).log().all()
				.when().get(Url);
		Thread.sleep(1000);

		String clientKey = getsourceIntKey();
		responseACC = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
	}

	@Then("the point unlock job run sucessfully with status code {int}")
	public void the_point_unlock_job_run_sucessfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responsePL.getStatusCode());
	}
}
