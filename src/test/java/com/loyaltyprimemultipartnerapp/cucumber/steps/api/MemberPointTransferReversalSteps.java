package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpStatus;
import org.junit.Assert;

import com.loyaltyprimemultipartnerapp.utlis.AIPsJsonBody;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.loyaltyprimemultipartnerapp.utlis.TestUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;


public class MemberPointTransferReversalSteps {
	
	String strapiBody;
	private String sourceKey;// source key
	private String targetKey; // target key
	private String TrxDate;
	private String TrxId;

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
	
	public static String getReciptId() {
		return _reciptId;
	}
	private static String _reciptId;
	public static Float getSoucreBalanceAfterTransfer() {
		return _getSourceBalanceAfterTransfer;
	}

	private static Float _getSourceBalanceAfterTransfer;

	public static Float getTargetBalanceAfterTransfer() {
		return _getTargetBalanceAfterTransfer;
	}
	private static Float _getTargetBalanceAfterTransfer;

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
	
	public static String getReciptIdPT() {
		return _reciptIdPT;
	}

	private static String _reciptIdPT;
	
	public static String getsourceKeyP() {
		return _sourceKeyP;
	}

	private static String _sourceKeyP;

	public static String gettargetKeyP() {
		return _targetKeyP;
	}

	private static String _targetKeyP;
	
	Response response=null;
	Response  responseTM=null;
	Response responsePT=null;
	Response response_TRAGACAFTTP=null;
	Response response_SRCACAFTTP=null;
	Response response_TRAGREDREV=null;
	Response responseRD=null;
	Response responseTMRD=null;
	Response responseTMADJ=null;
	Response responsePTD=null;
	Response response_MUT=null;
	Response responseAdj=null;
	Response responseSMP =null;
	Response responseTMP=null;
	Response responseRDM=null;
	
	public Properties LoadProperties() {
		try {
			InputStream inStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			return prop;
		} catch (Exception e) {
			System.out.println("File not found exception thrown for config.properties file.");
			return null;
		}
	}
	
	@Given("source member is enroll for point transfer reversal")
	public void source_member_is_enroll_for_point_transfer_reversal() {
	  
		System.out.println("source member is enroll for point transfer reversal");
	}

	@When("user perform enrollment of source member for point transfer reversal")
	public void user_perform_enrollment_of_source_member_for_point_transfer_reversal() throws InterruptedException {
		sourceKey = LoadProperties().getProperty("enkey") + "_SRCPTR" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_AltId_SCRPTR" + TestUtils.getRandomValue();
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

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/memberships");
		Thread.sleep(1000);
	}

	@Then("source member is created for point transfer reversal with statuscode {int}")
	public void source_member_is_created_for_point_transfer_reversal_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("target member is enroll for point transfer reversal")
	public void target_member_is_enroll_for_point_transfer_reversal() {
	  System.out.println("target member is enroll for point transfer reversal");
	
	}

	@When("user perform enrollment of target member for point transfer reversal")
	public void user_perform_enrollment_of_target_member_for_point_transfer_reversal() {
		targetKey = LoadProperties().getProperty("enkey") + "_DSTPTR" +TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_targetKey = targetKey; // save this member to make it target
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_AltId_DSTPTR" + TestUtils.getRandomValue();
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

		responseTM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/memberships");
	}

	@Then("target member is created for point transfer reversal with statuscode {int}")
	public void target_member_is_created_for_point_transfer_reversal_with_statuscode(Integer code) {
	   
		Assert.assertEquals(code.intValue(), responseTM.getStatusCode());

		JsonPath jsonPathEvaluator = responseTM.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("source member post transaction for point transfer reversal")
	public void source_member_post_transaction_for_point_transfer_reversal() {
		sourceKey = getsourceKey();
	}

	@When("user perform earn transaction as source member for point transfer reversal")
	public void user_perform_earn_transaction_as_source_member_for_point_transfer_reversal() throws InterruptedException {
		String TrxId = "PTRSRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.getDateTime();
		_trxDate = TrxDate; // Set value here for transaction
		String price = "200";
		String quantity = "1";
		String clientKey = getsourceKey();
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
		
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, price, quantity, productKey, channelkey,
				productKey2, channelKey2, productKey3, channelKey3);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
		Thread.sleep(10000);
	}

	@Then("earn transaction as source member for point transfer reversal is successfull with status code {int}")
	public void earn_transaction_as_source_member_for_point_transfer_reversal_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("source member transfer points to target member")
	public void source_member_transfer_points_to_target_member() {

		String _sourceId = getsourceKey();
		System.out.println("Source Key" + _sourceId);
		String _targetId = gettargetKey();
		System.out.println("Target key " + _targetId);
	}

	@When("user perform point transfer for source to target member")
	public void user_perform_point_transfer_for_source_to_target_member() {
		
		String receiptId = "PTRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		
		String date = TestUtils.getDateTime();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("debitorId");
		//_reciptId=receiptId+"_"+debitorId;
		_reciptId=receiptId;
		String description ="Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responsePT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Then("point transfer for source to target member is successful with status code {int}")
	public void point_transfer_for_source_to_target_member_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responsePT.getStatusCode());

		JsonPath jsonPathEvaluator = responsePT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Given("points transfer reversal is not possible for invalid TxId")
	public void points_transfer_reversal_is_not_possible_for_invalid_TxId() {
		System.out.println("Source member point transfer reversal");
	}

	@When("user perform point transfer reversal for invalid TxId")
	public void user_perform_point_transfer_reversal_for_invalid_TxId() {
		String txId = "TxPTRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		String pointType = LoadProperties().getProperty("pointType");
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/transfer/reversal");
	}

	@Then("user should not be able to do point transfe reversal with status code {int}")
	public void user_should_not_be_able_to_do_point_transfe_reversal_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
	}

	@Then("the user sees a error message as {string}")
	public void the_user_sees_a_error_message_as(String strMsg) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		String getErrorMsg = jsonPathEvaluator.get("errDetails.msgTemplate");
		Assert.assertEquals(getErrorMsg, strMsg);
	}

	@Given("points transfer reversal is not possible for invalid points type")
	public void points_transfer_reversal_is_not_possible_for_invalid_points_type() {
		System.out.println("Source member point transfer reversal");
	}

	@When("user perform point transfer reversal for invalid points type")
	public void user_perform_point_transfer_reversal_for_invalid_points_type() {
		String txId = getReciptId();
		// "TxPTRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		String pointType = "XYZType";
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/transfer/reversal");
	}

	@Given("points transfer reversal for source member is successful")
	public void points_transfer_reversal_for_source_member_is_successful() {
	
		System.out.println("Source member point transfer reversal");
	}

	@When("user perform point transfer reversal for source member")
	public void user_perform_point_transfer_reversal_for_source_member() {
		String txId = getReciptId();
		String date = TestUtils.getDateTime();
		String pointType = LoadProperties().getProperty("pointType");
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/transfer/reversal");
	}

	@Then("point transfer reversal is successful with status code {int}")
	public void point_transfer_reversal_is_successful_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Then("the user sees a success message and target member numbers with point type")
	public void the_user_sees_a_success_message_and_target_member_numbers_with_point_type() {
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String getTargetKey = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(getTargetKey, gettargetKey());

		String getMemberType = jsonPathEvaluator.get("data.pointsType");
		Assert.assertEquals(getMemberType, "BONUS");
	}
	
	@Given("source member see his account statement")
	public void source_member_see_his_account_statement() {
	   sourceKey= getsourceKey();
	  
	}

	@When("user perform account statement for source member after point transfer reversal")
	public void user_perform_account_statement_for_source_member_after_point_transfer_reversal() {
		// Deposit statement
				response_SRCACAFTTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
						.when().get("/api/v10/deposits/" + sourceKey + "/statement?type=BONUS");
	}

	@Then("source member can see his account statement after transfer reversal with status code {int}")
	public void source_member_can_see_his_account_statement_after_transfer_reversal_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response_SRCACAFTTP.getStatusCode());
		JsonPath jsonPathEvaluator = response_SRCACAFTTP.jsonPath();
		Float balance_After_Tp_src = jsonPathEvaluator.get("data.balance");
		_getSourceBalanceAfterTransfer = balance_After_Tp_src;

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("target member see his account statement")
	public void target_member_see_his_account_statement() {
		targetKey = gettargetKey();
		System.out.println("The target key:" + targetKey);
	}

	@When("user perform account statement for target member after point transfer reversal")
	public void user_perform_account_statement_for_target_member_after_point_transfer_reversal() {
		response_TRAGACAFTTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + targetKey + "/statement?type=BONUS");
	}

	@Then("user can see account statement for target member after transfer reversal with status code {int}")
	public void user_can_see_account_statement_for_target_member_after_transfer_reversal_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response_TRAGACAFTTP.getStatusCode());
		JsonPath jsonPathEvaluator = response_TRAGACAFTTP.jsonPath();
		Float balance_After_Tp_tg = jsonPathEvaluator.get("data.balance");
		_getTargetBalanceAfterTransfer = balance_After_Tp_tg;

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Then("the user sees a success message with type as {string}")
	public void the_user_sees_a_success_message_with_type_as(String strMsg) {
		JsonPath jsonPathEvaluator1 = response_TRAGACAFTTP.jsonPath();
		String type = strMsg;
		// Need to put the integration code here as facing some issue in nested jsonextraction.
		List<String> getmembershipIds = jsonPathEvaluator1.getList("data.bookings.references[1].uniqueId");
		System.out.println("The target card history membershipKey:" + getmembershipIds);
		for (String val : getmembershipIds) {
			if (val.equals(type)) {// here check the field contains or not
				strMsg = val;
				break;
			}
		}
		Assert.assertEquals(type, strMsg);
	}
	
	@Given("source member do redemption\\/redemption reversal")
	public void source_member_do_redemption_redemption_reversal() {
	    sourceKey=getsourceKey();
	    
	    
	}

	@When("user perform redemption for source member after point transfer reversal")
	public void user_perform_redemption_for_source_member_after_point_transfer_reversal() {
	  
		String TrxDate = getTrxDate();
		_RedTXDate = TrxDate;
		String receiptId = "AutorecTest" + TestUtils.getRandomValue();
		
		String debitorId = LoadProperties().getProperty("debitorId");
		_debitorId = debitorId;
		_receiptId = receiptId+"_"+debitorId;
		String amount = "10";
		_amount = amount;
		String description = "Auto Test Redeem";
		String clientKey = getsourceKey();
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		responseRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}

	
	@Then("redemption for source member after point transfer reversal is successful with status code {int}")
	public void redemption_for_source_member_after_point_transfer_reversal_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseRD.getStatusCode());
		JsonPath jsonPathEvaluator = responseRD.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	@When("user perform redemption reversal for source member after point transfer reversal")
	public void user_perform_redemption_reversal_for_source_member_after_point_transfer_reversal() {
		TrxId = getReceiptId();
		TrxDate = getTrxDate();
		String description = "Auto Redemption reversal test";
		String PointsType = "BONUS";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response_TRAGREDREV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				.body(strapiBody).log().all().when().post("/api/v10/deposits/redeem/reversal");

		System.out.println("Redemption reversal responce" + response_TRAGREDREV.asString());
	}
	
	@Then("redemption reversal for source member after point transfer reversal is successful with status code {int}")
	public void redemption_reversal_for_source_member_after_point_transfer_reversal_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response_TRAGREDREV.getStatusCode());
		JsonPath jsonPathEvaluator = response_TRAGREDREV.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	@Given("duplicate point transfer reversal is not possible for same txId")
	public void duplicate_point_transfer_reversal_is_not_possible_for_same_txId() {
	    System.out.println("duplicate tx");
	}

	@When("user perform point transfer reversal for same transactionId again")
	public void user_perform_point_transfer_reversal_for_same_transactionId_again() {
		String txId = getReciptId();
		String date = TestUtils.getDateTime();
		String pointType = LoadProperties().getProperty("pointType");
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/transfer/reversal");
	}

	@Then("point transfer reversal for duplicate transactionId should not be successful with status code {int}")
	public void point_transfer_reversal_for_duplicate_transactionId_should_not_be_successful_with_status_code(Integer code) {
	   
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
	}
	
	@Given("partial points transfer reversal is not possible")
	public void partial_points_transfer_reversal_is_not_possible() {
	    System.out.println("partial transfer");
	}
	
	
	@When("user perform point transfer of {string} points for source to target member")
	public void user_perform_point_transfer_of_points_for_source_to_target_member(String strPoints) {
		String receiptId = "PTRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		_reciptIdPT=receiptId;
		String date = TestUtils.getDateTime();
		String amount = strPoints;
		String debitorId = LoadProperties().getProperty("debitorId");
		String description ="Test Transfer";
		String clientKey = getsourceKey();//getsourceKey();
		String destclientKey = gettargetKey(); //gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responsePT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}
	@Then("{string} points should be deducted from the source member balance")
	public void points_should_be_deducted_from_the_source_member_balance(String strPoints) {
		
		Assert.assertEquals(HttpStatus.SC_OK, responsePT.getStatusCode());

		JsonPath jsonPathEvaluator = responsePT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	    
	}

	@When("user perform redemption of {string} points for target member")
	public void user_perform_redemption_of_points_for_target_member(String strPoints) {
		String TrxDate = getTrxDate();
		_RedTXDate = TrxDate;
		String receiptId = "AutorecTest" + TestUtils.getRandomValue();
	
		String debitorId = LoadProperties().getProperty("debitorId");
		_debitorId = debitorId;
		_receiptId = receiptId+"_"+debitorId;
		String amount = "10";
		_amount = amount;
		String description = "Auto Test Redeem";
		String clientKey =gettargetKey(); //gettargetKey();
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		responseTMRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}
	
	@Then("target member should be able to do redemption successful with status code {int}")
	public void target_member_should_be_able_to_do_redemption_successful_with_status_code(Integer code) {
	   
		Assert.assertEquals(code.intValue(), responseTMRD.getStatusCode());
		JsonPath jsonPathEvaluator = responseTMRD.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(gettargetKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		
	}

	@When("user perform point transfer reversal of partial points source member")
	public void user_perform_point_transfer_reversal_of_partial_points_source_member() {
		String txId = getReciptIdPT();
		String date = TestUtils.getDateTime();
		String pointType = LoadProperties().getProperty("pointType");
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/transfer/reversal");
	}

	@Then("source member should not be able to do partial point transfer reversal with status code {int}")
	public void source_member_should_not_be_able_to_do_partial_point_transfer_reversal_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
	}

	@Then("Response message template should show message {string}")
	public void response_message_template_should_show_message(String strMsg) {
		JsonPath jsonPathEvaluator = response.jsonPath();
		String getErrorMsg = jsonPathEvaluator.get("errDetails.msgTemplate");
		Assert.assertEquals(getErrorMsg, strMsg);
	}
	
	@Given("Member wants to points transfer reversal")
	public void member_wants_to_points_transfer_reversal() {
	    System.out.println("point transfer after adjustment of points");
	}
	@When("user perform point transfer of {string} points for partial point transfer reversal")
	public void user_perform_point_transfer_of_points_for_partial_point_transfer_reversal(String strPoints) {
		String receiptId = "PTRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		_reciptIdPT=receiptId;
		String date = TestUtils.getDateTime();
		String amount = strPoints;
		String debitorId = LoadProperties().getProperty("debitorId");
		String description ="Test Transfer";
		String clientKey = getsourceKeyP();//getsourceKey();
		String destclientKey = gettargetKeyP(); //gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}
	@Then("{string} points should be deducted from the source member balance Partial point transfer reversal")
	public void points_should_be_deducted_from_the_source_member_balance_Partial_point_transfer_reversal(String string) {
		
		Assert.assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(false,getStatus);
		
	}
	@When("user perform redemption of {string} points for partial point transfer reversal of target")
	public void user_perform_redemption_of_points_for_partial_point_transfer_reversal_of_target(String string) {
	 
		String TrxDate = getTrxDate();
		_RedTXDate = TrxDate;
		String receiptId = "AutorecTest" + TestUtils.getRandomValue();
	
		String debitorId = LoadProperties().getProperty("debitorId");
		_debitorId = debitorId;
		_receiptId = receiptId+"_"+debitorId;
		String amount = "10";
		_amount = amount;
		String description = "Auto Test Redeem";
		String clientKey =gettargetKeyP(); //gettargetKey();
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		responseRDM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}
	@Then("target member should be able to do redemption successfully for partial point transfer reversal with status code {int}")
	public void target_member_should_be_able_to_do_redemption_successfully_for_partial_point_transfer_reversal_with_status_code(Integer code) {
		 
		Assert.assertEquals(code.intValue(), responseRDM.getStatusCode());
		JsonPath jsonPathEvaluator = responseRDM.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(gettargetKeyP(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	@When("user perform point adjustment of {string} points for target member")
	public void user_perform_point_adjustment_of_points_for_target_member(String strPoints) {
		
		
		String receiptId="RecAuto_" + TestUtils.getRandomValue();
		String date=TestUtils.getDateTime();		;
		String description=" Test Point Adjustment";
		String debitorId=LoadProperties().getProperty("debitorId");
		AIPsJsonBody apibody = new AIPsJsonBody();
		targetKey = gettargetKey();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, strPoints,debitorId, description);
	
		responseTMADJ = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/" + targetKey + "/adjust?type=BONUS");
	}
	
	@Then("Target member should be able to do point adjustment for point transfer reversal with status code {int}")
	public void target_member_should_be_able_to_do_point_adjustment_for_point_transfer_reversal_with_status_code(Integer code) {
		
		Assert.assertEquals(code.intValue(), responseTMADJ.getStatusCode());

		JsonPath jsonPathEvaluator = responseTMADJ.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform point transfer reversal for source member after adjustment of points")
	public void user_perform_point_transfer_reversal_for_source_member_after_adjustment_of_points() {
	 
		String txId = getReciptIdPT();
		String date = TestUtils.getDateTime();
		String pointType = LoadProperties().getProperty("pointType");
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/transfer/reversal");
	}
	@Then("source member should be able to do point transfe reversal with status code {int}")
	public void source_member_should_be_able_to_do_point_transfe_reversal_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	
	@Given("point transfer reversal not possible for cancel member")
	public void point_transfer_reversal_not_possible_for_cancel_member() {
	   System.out.println("point transfer for cancel member");
	   targetKey=gettargetKey();
	}

	@When("user perform membership update for target member as cancel")
	public void user_perform_membership_update_for_target_member_as_cancel() {
		targetKey=null;
		targetKey=gettargetKey();
	    response_MUT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
	    		//.body(strapiBody)
				.log().all().when()
				.post("/api/v10/memberships/"+targetKey+"/cancel");
	}
	@Then("membership update for target member is successful with status code {int}")
	public void membership_update_for_target_member_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response_MUT.getStatusCode());

		JsonPath jsonPathEvaluator = response_MUT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform point transfer reversal after updating membership status of target member")
	public void user_perform_point_transfer_reversal_after_updating_membership_status_of_target_member() {
	 
		String txId = getReciptIdPT();
		String date = TestUtils.getDateTime();
		String pointType = LoadProperties().getProperty("pointType");
		String description = "Test Transfer";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferReversalAPIBody(txId, date, pointType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/transfer/reversal");
	}
	/*
	@Then("source member should not be able to do partial point transfe reversal with status code {int}")
	public void source_member_should_not_be_able_to_do_partial_point_transfe_reversal_with_status_code(Integer code) {
	   
		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
	}*/
	@Then("Source member should not be able to do point transfe reversal with status code {int}")
	public void source_member_should_not_be_able_to_do_point_transfe_reversal_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
	}
	
	@When("user perform enrollment for point transfer reversal of source member")
	public void user_perform_enrollment_for_point_transfer_reversal_of_source_member() throws InterruptedException {
		sourceKey=null;
		sourceKey = LoadProperties().getProperty("enkey") + "_SMPTR" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_sourceKeyP = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_AltId_SMRPTR" + TestUtils.getRandomValue();
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

		responseSMP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/memberships");
		Thread.sleep(1000);
	}
	
	@Then("source member is created successfully for point transfer reversal with statuscode {int}")
	public void source_member_is_created_successfully_for_point_transfer_reversal_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseSMP.getStatusCode());

		JsonPath jsonPathEvaluator = responseSMP.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform enrollment for point transfer reversal of target member")
	public void user_perform_enrollment_for_point_transfer_reversal_of_target_member() {
		targetKey=null;
		targetKey = LoadProperties().getProperty("enkey") + "_TMTPTR" +TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_targetKeyP = targetKey; // save this member to make it target
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_AltId_TMTPTR" + TestUtils.getRandomValue();
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
		
		responseTMP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/memberships");
	}
	
	@Then("target member is created successfully for point transfer reversal with statuscode {int}")
	public void target_member_is_created_successfully_for_point_transfer_reversal_with_statuscode(Integer code) {
	   
		Assert.assertEquals(code.intValue(), responseTMP.getStatusCode());

		JsonPath jsonPathEvaluator = responseTMP.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}


	@When("user perform point adjustment for point transfer reversal of target member")
	public void user_perform_point_adjustment_for_point_transfer_reversal_of_target_member() throws InterruptedException {
		targetKey=null;
		targetKey = gettargetKeyP();
		AIPsJsonBody apibody = new AIPsJsonBody();
		String receiptId="Rextest_"+TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		String date=TestUtils.getDateTime();
		String amount="-100";
		String debitorId=LoadProperties().getProperty("debitorId");
		String description="Text";
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		responseAdj = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + targetKey + "/adjust?type=BONUS");

		Thread.sleep(10000);
		
	}
	@Then("target member point adjustment is successfull for point transfer reversal with statuscode {int}")
	public void target_member_point_adjustment_is_successfull_for_point_transfer_reversal_with_statuscode(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseAdj.getStatusCode());

		JsonPath jsonPathEvaluator = responseAdj.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

}
