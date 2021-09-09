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

public class MemberEstimatedExpirationSteps extends TestBase {
	String strapiBody;
	private String sourceKey;// source key
	private String clientKey;// new member
	Response response;// for source member
	Response responseTX = null;
	Response responseRDREV = null;
	Response responseRD = null;
	Response responseEvt = null;
	Response responseAccT=null;

	public static String getTrxId() {
		return _TrxId;
	}

	private static String _TrxId;

	public static String getTrxDate() {
		return _trxDate;
	}

	private static String _trxDate;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	@Given("user performs transaction")
	public void user_performs_transaction() {

	}

	@When("user perform enrollment")
	public void user_perform_enrollment() {
		clientKey = LoadProperties().getProperty("enkey") + "_SRIMP" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
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
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, clientKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("member enrollment is successful with status code {int}")
	public void member_enrollment_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user perform earn transaction for response monitor")
	public void user_perform_earn_transaction_for_response_monitor() throws InterruptedException {
		String TrxId = "IMSRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.getDateTime();
		_trxDate = TrxDate; // Set value here for transaction
		String price = "100";
		String quantity = "1";
		String channelkey = "";
		String productKey = "";
		String productKey2 = "";
		String channelKey2 = "";
		String productKey3 = "";
		String channelKey3 = "";
		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");
		String _getChannelKey = jsonPathEval.get("data[0].channelKey");
		String _getProductKey2 = jsonPathEval.get("data[1].key");
		String _getChannelKey2 = jsonPathEval.get("data[1].channelKey");
		String _getProductKey3 = jsonPathEval.get("data[2].key");
		String _getChannelKey3 = jsonPathEval.get("data[2].channelKey");

		channelkey = _getChannelKey;
		productKey = _getProductKey;
		channelKey2 = _getChannelKey2;
		productKey2 = _getProductKey2;
		channelKey3 = _getChannelKey3;
		productKey3 = _getProductKey3;

		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate, partnerId, price, quantity, productKey,
				channelkey, productKey2, channelKey2, productKey3, channelKey3);

		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
		Thread.sleep(10000);
	}

	@Then("member earn for response monitor is successful with status code {int}")
	public void member_earn_for_response_monitor_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
		
		
		String getestmValue = jsonPathEvaluator.get("data.rewards.BONUS.estimatedExpiration");
		System.out.println("Email id received from Response " + getestmValue);
		String strextimatExp=TestUtils.GetExpiryFutureDateTime();
		
		//2021-10-16T00:00:00.0000000Z
		System.out.println("Future date value:"+ strextimatExp);
		
		Assert.assertEquals(strextimatExp.substring(0,12), getestmValue.substring(0,12));
	}

	@When("member perform event history for response monitor")
	public void member_perform_event_history_for_response_monitor() {

		
		responseEvt = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"membershipKey\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/loyaltyops/events/list");
	}

	@Then("event history of response monitor is displayed successfully with status code {int}")
	public void event_history_of_response_monitor_is_displayed_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}
	
	@When("user perform account statement for respone monitor")
	public void user_perform_account_statement_for_respone_monitor() {
	  
		sourceKey = getsourceKey();
		
		responseAccT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				.log().all().when()
				.get("/api/v10/deposits/" + sourceKey + "/statement?type=BONUS");
		
	}

	@Then("account statement of member is successful with status code {int}")
	public void account_statement_of_member_is_successful_with_status_code(Integer code) {
	 
		Assert.assertEquals(HttpStatus.SC_OK, responseAccT.getStatusCode());
		
		JsonPath jsonEval = responseAccT.jsonPath();
		
		String getestmValue = jsonEval.get("data.bookings[0].estimatedExpiration");
		System.out.println("Email id received from Response " + getestmValue);
		String strextimatExp=TestUtils.GetExpiryFutureDateTime();
		
		//2021-10-16T00:00:00.0000000Z
		System.out.println("Future date value:"+ strextimatExp);
		
		Assert.assertEquals(strextimatExp.substring(0,12), getestmValue.substring(0,12));
		
	

	}
	
	

}
