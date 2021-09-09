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

public class EventIntegrationSteps extends TestBase {
	String strapiBody;
	private String sourceKey;// source key
	private String clientKey;//new member
	Response response;// for source member
	Response responseTX=null;
	Response responseRDREV=null;
	Response responseRD=null;
	Response responseEvt=null;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	public static String gettargetKey() {
		return _targetKey;
	}

	private static String _targetKey;
	
	public static String getanotherKey() {
		return _anotherKey;
	}

	private static String _anotherKey;
	
	public static String getuserIdLe() {
		return _userIdLe;
	}

	private static String _userIdLe;

	public static String getTrxId() {
		return _TrxId;
	}

	private static String _TrxId;

	public static String getTrxDate() {
		return _trxDate;
	}

	private static String _trxDate;

	public static String getReceiptId() {
		return _receiptId;
	}

	private static String _receiptId;

	public static String getDebitorId() {
		return _debitorId;
	}

	private static String _debitorId;

	public static String getAmount() {
		return _amount;
	}

	private static String _amount;

	public static String getRedTXDate() {
		return _RedTXDate;
	}

	private static String _RedTXDate;
	
	@Given("user check for member")
	public void user_check_for_member() {
	    System.out.println("enroll member");
	}

	@When("user perform enrollment for events")
	public void user_perform_enrollment_for_events() {
	
		clientKey = LoadProperties().getProperty("enkey") + "_SRIMP" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRIMP_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = _sourceKey;
		//_sourceLegalEt=strUserId;
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
	@Then("enrollment is successful with status code {int}")
	public void enrollment_is_successful_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();
		
		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Given("user check for event listing of api")
	public void user_check_for_event_listing_of_api() {
	 System.out.println("event api"); 
	}

	@When("member perform event history")
	public void member_perform_event_history() {
	  
		sourceKey = getsourceKey();
		responseEvt = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"membershipKey\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/loyaltyops/events/list");
	}

	@Then("event history is displayed successfully with status code {int}")
	public void event_history_is_displayed_successfully_with_status_code(Integer code) throws InterruptedException {
	 
		Assert.assertEquals(code.intValue(), responseEvt.getStatusCode());

		JsonPath jsonPtEval = responseEvt.jsonPath();
		sourceKey = getsourceKey();
		boolean getstatus = jsonPtEval.get("success");
		Assert.assertEquals(getstatus, true);

		String str_data1 = null;
		String str_data2 = null;
/*
		List<String> trnsIds = jsonPtEval.getList("data.transferredTo");
		System.out.println("The events history transferredTo: " + trnsIds);
		for (String id : trnsIds) {
			if (id.equals(sourceKey)) {// here check the field contais or not
				str_data1 = id;
				break;
			}
		}
		List<String> reasonsIds = jsonPtEval.getList("data.transferReason");
		System.out.println("The events history transferReason:" + reasonsIds);
		for (String val : reasonsIds) {
			if (val.equals("Membership merge")) {// here check the field contais or not
				str_data2 = val;
				break;
			}
		}
		Thread.sleep(1000);
		Assert.assertEquals(sourceKey, str_data1);
		Assert.assertEquals("Membership merge", str_data2);
*/
	}

	@Given("user performs transactions")
	public void user_performs_transactions() {
	    System.out.println("perform transacion");
	}

	@When("user perform earn transaction for events")
	public void user_perform_earn_transaction_for_events() throws InterruptedException {
		 
	}

	@Then("member earn is successful with status code {int}")
	public void member_earn_is_successful_with_status_code(Integer code) {
	  
	Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

	JsonPath jsonPathEvaluator = responseTX.jsonPath();

	Boolean strSuccess = jsonPathEvaluator.get("success");
	Assert.assertEquals(true, strSuccess);
	}

	@Given("user performs redemption and redemption reversal")
	public void user_performs_redemption_and_redemption_reversal() {
	    System.out.println("perform redemption");
	}

	@When("user perform redemption for events")
	public void user_perform_redemption_for_events() {
		String clientKey = getsourceKey();
		String TrxDate = TestUtils.getDateTime();// get date value from last
		_RedTXDate = TrxDate;
		String receiptId = TrxDate + TestUtils.getRandomValue();
		String debitorId =LoadProperties().getProperty("debitorId");
		_debitorId = debitorId;
		
		_receiptId = receiptId+"_"+debitorId;
		String amount ="10";
		_amount = amount;
		String description = "Test Redeem";
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		responseRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}
	
	@Then("redemption is successful with status code {int}")
	public void redemption_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseRD.getStatusCode());

		JsonPath jsonPathEval = responseRD.jsonPath();
		String getCleientId = jsonPathEval.get("data.membershipKey");
		Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);
		
	}
	
	@When("user perform redemption reversal for events")
	public void user_perform_redemption_reversal_for_events() {
	  
		sourceKey = getsourceKey();
		System.out.println("Get value of txvalue"+ getReceiptId());
		String TrxId = getReceiptId();
		String TrxDate = getRedTXDate();// get date value from last
		String description = "Redemption reversal test";
		String PointsType = "BONUS";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		responseRDREV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/redeem/reversal");
	}

	@Then("redemption reversal is successful with status code {int}")
	public void redemption_reversal_is_successful_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseRDREV.getStatusCode());

		sourceKey = getsourceKey();
		JsonPath jsonPathEvaluator = responseRDREV.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(sourceKey, getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}


}
