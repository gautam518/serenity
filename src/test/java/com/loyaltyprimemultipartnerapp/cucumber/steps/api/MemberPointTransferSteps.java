package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
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

public class MemberPointTransferSteps {
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

	public static Float getSoucreBalanceBeforTransfer() {
		return _getSourcebalancebeforeTransfer;
	}

	private static Float _getSourcebalancebeforeTransfer;

	
	public static Float getTargetBalanceBeforTransfer() {
		return _getTargetbalancebeforeTransfer;
	}

	private static Float _getTargetbalancebeforeTransfer;

	
	public static Float getSoucreBalanceAfterTransfer() {
		return _getSourceBalanceAfterTransfer;
	}

	private static Float _getSourceBalanceAfterTransfer;

	public static Float getTargetBalanceAfterTransfer() {
		return _getTargetBalanceAfterTransfer;
	}
	private static Float _getTargetBalanceAfterTransfer;

	public static String getdiffprogKey() {
		return _diffprogKey;
	}

	private static String _diffprogKey;

	Response responseSM;// for source member
	Response responseTM;// for source member
	Response responseTX;
	Response responseRD;
	Response responseTPT;
	Response responseDPM;
	Response responseTPTDP;
	Response response_SRCACBTP;
	Response response_TRAGACBTP;
	Response response_SRCACAFTTP;
	Response response_TRAGACAFTTP;
	Response responseTARG_TXREV;
	Response response_TRAGREDREV;
	Response response_TARGPT;
	Response response_TRAGTVELST;
	Response response_ACCSTR1;
	Response response_R1SPT;
	Response response_ACCSTR2;
	Response response_R2SPTP;
	Response responseTARNW;
	Response response_TARACCSTR;

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

	@Given("Enroll member in the PCV{int} as source member")
	public void enroll_member_in_the_PCV_as_source_member(Integer int1) {
		sourceKey = LoadProperties().getProperty("enkey") + "_MPTSRC" + TestUtils.getRandomValue();
		_sourceKey = sourceKey;
	}

	@When("Member performs enrollment in PCV{int} as source member")
	public void member_performs_enrollment_in_PCV_as_source_member(Integer int1) {
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MPTSRC_AltId" + TestUtils.getRandomValue();
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

		responseSM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("Member should be created in PCV{int} as source member with statuscode {int}")
	public void member_should_be_created_in_PCV_as_source_member_with_statuscode(Integer int1, Integer int2) {
		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("Enroll member in the PCV{int} as target member")
	public void enroll_member_in_the_PCV_as_target_member(Integer int1) {
		targetKey = LoadProperties().getProperty("enkey") + "_MPTTARG" + TestUtils.getRandomValue();
		_targetKey = targetKey; // save this member to make it target
	}

	@When("Member performs enrollment in PCV{int} as target member")
	public void member_performs_enrollment_in_PCV_as_target_member(Integer int1) {
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MPTDEST_AltId" + TestUtils.getRandomValue();
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

	@Then("Member should be created in PCV{int} as target member with statuscode {int}")
	public void member_should_be_created_in_PCV_as_target_member_with_statuscode(Integer int1, Integer int2) {
		Assert.assertEquals(HttpStatus.SC_OK, responseTM.getStatusCode());
		System.out.println("target member is Added successfully:" + responseTM.asString());

		JsonPath jsonPathEvaluator = responseTM.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member call the earn transaction as source member")
	public void member_call_the_earn_transaction_as_source_member() {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.getDateTime();
		_trxDate = TrxDate; // Set value here for transaction
		String price = "20";
		String quantity = "1";
		String clientKey = getsourceKey();
		String channelkey = "";
		String productKey = "";
		String productKey2 ="";
		String channelKey2 = "";
		String productKey3 ="";
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


		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Member point earn for the source member should be successful with status code {int}")
	public void member_point_earn_for_the_source_member_should_be_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("Source member wants to see account statement before point transfer")
	public void source_member_wants_to_see_account_statement_before_point_transfer() {
		sourceKey = getsourceKey();
		System.out.println("The source key:" + sourceKey);
	}

	@When("Source member perform account statement before point transfer")
	public void source_member_perform_account_statement_before_point_transfer() {
		// Deposit statement
		response_SRCACBTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + sourceKey + "/statement?type=BONUS");
	}

	@Then("Source member should be able to see the account statement before point transfer with status code {int}")
	public void source_member_should_be_able_to_see_the_account_statement_before_point_transfer_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), response_SRCACBTP.getStatusCode());
		JsonPath jsonPathEvaluator = response_SRCACBTP.jsonPath();
		Float balance_before_merge_src = jsonPathEvaluator.get("data.balance");
		_getSourcebalancebeforeTransfer = balance_before_merge_src;
		// Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("Target member perform account statement before point transfer")
	public void target_member_perform_account_statement_before_point_transfer() {
		targetKey = gettargetKey();
		response_TRAGACBTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + targetKey + "/statement?type=BONUS");
	}

	@Then("Target member should be able to see the account statement before point transfer with status code {int}")
	public void target_member_should_be_able_to_see_the_account_statement_before_point_transfer_with_status_code(
			Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, response_TRAGACBTP.getStatusCode());
		JsonPath jsonPathEvaluator = response_TRAGACBTP.jsonPath();
		Float balance_before_merge_src = jsonPathEvaluator.get("data.balance");
		_getTargetbalancebeforeTransfer = balance_before_merge_src;
		// Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("Member wants to transfer points in PCV{int}")
	public void member_wants_to_transfer_points_in_PCV(Integer int1) {

		String _sourceId = getsourceKey();
		System.out.println("Source Key" + _sourceId);
		String _targetId = gettargetKey();
		System.out.println("Target key " + _targetId);
	}

	@When("Member perform point transfer from source to target member")
	public void member_perform_point_transfer_from_source_to_target_member() {

		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		String amount = "10";
		String debitorId=LoadProperties().getProperty("debitorId");
		String description ="Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responseTPT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Then("Point transfer should be successful with status code {int}")
	public void point_transfer_should_be_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTPT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTPT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Then("The response message template should contain the source and target member numbers with point type")
	public void the_response_message_template_should_contain_the_source_and_target_member_numbers_with_point_type() {

		JsonPath jsonPathEvaluator = responseTPT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String getSourceKey = jsonPathEvaluator.get("data.sourceKey");
		Assert.assertEquals(getSourceKey, getSourceKey);

		String getTargetKey = jsonPathEvaluator.get("data.targetKey");
		Assert.assertEquals(getTargetKey, gettargetKey());

		String getMemberType = jsonPathEvaluator.get("data.pointsType");
		Assert.assertEquals(getMemberType, "BONUS");
	}

	@Given("Members want to transfer blocked points PCV{int}")
	public void members_want_to_transfer_blocked_points_PCV(Integer int1) {
		String _sourceId = getsourceKey();
		System.out.println("Source Key" + _sourceId);
		String _targetId = gettargetKey();
		System.out.println("Target key " + _targetId);
	}

	@When("Member perform the point transfer for blocked points type")
	public void member_perform_the_point_transfer_for_blocked_points_type() {
		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("debitorId");
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responseTPT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BLOCKED");
	}

	@Then("Member should be able to transfer blocked points type with status code {int}.")
	public void member_should_be_able_to_transfer_blocked_points_type_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTPT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTPT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Given("Members want to transfer point from US to DE program")
	public void members_want_to_transfer_point_from_US_to_DE_program() {
		sourceKey = getsourceKey();
		System.out.println("Check member point transfer diffrent member type");
	}

	@When("Member performs enrollment in PCV{int} as for US program")
	public void member_performs_enrollment_in_PCV_as_for_US_program(Integer int1) {
		String clientKey = LoadProperties().getProperty("enkey") + "_SRCDP" + TestUtils.getRandomValue();
		_diffprogKey = clientKey;
		String strSince = TestUtils.getDateTime();
		String strType ="SHOPA"; //LoadProperties().getProperty("diffmemberType");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SDP_AltId" + TestUtils.getRandomValue();
		String strUserId = _diffprogKey;
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

		responseDPM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("Member should be created in PCV{int} for US program with statuscode {int}")
	public void member_should_be_created_in_PCV_for_US_program_with_statuscode(Integer int1, Integer code) {
		Assert.assertEquals(code.intValue(), responseDPM.getStatusCode());

		JsonPath jsonPathEvaluator = responseDPM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member perform the point transfer from US to DE program")
	public void member_perform_the_point_transfer_from_US_to_DE_program() {
		String receiptId = "TestResr_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = getdiffprogKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responseTPTDP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when()
				.post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Then("Member should not be able to transfer points with status code {int}")
	public void member_should_not_be_able_to_transfer_points_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTPTDP.getStatusCode());

		JsonPath jsonPathEvaluator = responseTPTDP.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Then("Message teamplate should display the error message {string}")
	public void message_teamplate_should_display_the_error_message(String strExcptedMsg) {
		JsonPath jsonPathEvaluator = responseTPTDP.jsonPath();
		String getActualMsg = jsonPathEvaluator.get("error");
		Assert.assertEquals(strExcptedMsg, getActualMsg);
	}

	@Given("Members want to transfer point of diffrent member type")
	public void members_want_to_transfer_point_of_diffrent_member_type() {
		String destclientKey = getdiffprogKey();
		System.out.println("The member number :" + destclientKey);
	}

	@When("Member perform the point transfer diffrent member type")
	public void member_perform_the_point_transfer_diffrent_member_type() {

		String receiptId = "TestResr_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = getdiffprogKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responseTPTDP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when()
				.post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Given("Source member wants to see account statement after point transfer")
	public void source_member_wants_to_see_account_statement_after_point_transfer() {
		sourceKey = getsourceKey();
		System.out.println("The source key:" + sourceKey);
	}

	@When("Source member perform account statement after point transfer")
	public void source_member_perform_account_statement_after_point_transfer() {
		// Deposit statement
		response_SRCACAFTTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + sourceKey + "/statement?type=BONUS");

	}

	@Then("Source member should be able to see the account statement after point transfer with status code {int}")
	public void source_member_should_be_able_to_see_the_account_statement_after_point_transfer_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), response_SRCACAFTTP.getStatusCode());
		JsonPath jsonPathEvaluator = response_SRCACAFTTP.jsonPath();
		Float balance_After_Tp_src = jsonPathEvaluator.get("data.balance");
		_getSourceBalanceAfterTransfer = balance_After_Tp_src;

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("Target member wants to see account statement after point transfer")
	public void target_member_wants_to_see_account_statement_after_point_transfer() {
		targetKey = gettargetKey();
		System.out.println("The target key:" + targetKey);
	}

	@When("Target member perform account statement after point transfer")
	public void target_member_perform_account_statement_after_point_transfer() {
		response_TRAGACAFTTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + targetKey + "/statement?type=BONUS");
	}

	@Then("Target member should be able to see the account statement after point transfer with status code {int}")
	public void target_member_should_be_able_to_see_the_account_statement_after_point_transfer_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), response_TRAGACAFTTP.getStatusCode());
		JsonPath jsonPathEvaluator = response_TRAGACAFTTP.jsonPath();
		Float balance_After_Tp_tg = jsonPathEvaluator.get("data.balance");
		_getTargetBalanceAfterTransfer = balance_After_Tp_tg;

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Then("This should show the message template with type as {string}")
	public void this_should_show_the_message_template_with_type_as(String strType) {
		JsonPath jsonPathEvaluator1 = response_TRAGACAFTTP.jsonPath();

		String _strType = null;
		String type = "TRANSFER";
		// Need to put the integration code here as facing some issue in nested json
		// extraction.
		List<String> getmembershipIds = jsonPathEvaluator1.getList("data.bookings.references[1].uniqueId");
		System.out.println("The target card history membershipKey:" + getmembershipIds);
		for (String val : getmembershipIds) {
			if (val.equals(type)) {// here check the field contains or not
				strType = val;
				break;
			}
		}
		Assert.assertEquals(type, strType);
	}

	@Given("Target member wants to do the points reversal after point transfer")
	public void target_member_wants_to_do_the_points_reversal_after_point_transfer() {
		targetKey = gettargetKey();
		System.out.println("The target Key value is :" + targetKey);
	}

	@When("Target member perform points reversal after point transfer")
	public void target_member_perform_points_reversal_after_point_transfer() {
		TrxId = getTrxId();
		TrxDate = getTrxDate();
		String description = "Auto Test Reversal";
		String clientKey = gettargetKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		responseTARG_TXREV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx/reversal");
	}

	@Then("Target member should not be able to reverse point after point transfer and show status code {int}")
	public void target_member_should_not_be_able_to_reverse_point_after_point_transfer_and_show_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), responseTARG_TXREV.getStatusCode());

		JsonPath jsonPathEvaluator = responseTARG_TXREV.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);

	}

	@Then("Message teamplate should display the message {string}")
	public void message_teamplate_should_display_the_message(String strExcptedMsg) {
		JsonPath jsonPathEvaluator = responseTARG_TXREV.jsonPath();
		String getActualMsg = jsonPathEvaluator.get("errDetails.msgTemplate");
		Assert.assertEquals(strExcptedMsg, getActualMsg);
	}

	@Then("This should show the message teamplat as {string}")
	public void this_should_show_the_message_teamplat_as(String string) {

	}

	@When("source member perform point redemption")
	public void source_member_perform_point_redemption() {

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

	@Then("Source member point redemption should be successful with status code {int}")
	public void source_member_point_redemption_should_be_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseRD.getStatusCode());
		JsonPath jsonPathEvaluator = responseRD.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Status received from Response " + getStatus);
	}

	@Given("Target member wants to redemption reversal after point transfer")
	public void target_member_wants_to_redemption_reversal_after_point_transfer() {
		targetKey = gettargetKey();
		System.out.println("Target key: " + targetKey);
	}

	@When("Target member performs redemption reversal after point transfer")
	public void target_member_performs_redemption_reversal_after_point_transfer() {

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

	@Then("Target member should be able to do redemption reversal after point transfer with status code {int}")
	public void target_member_should_be_able_to_do_redemption_reversal_after_point_transfer_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), response_TRAGREDREV.getStatusCode());

		JsonPath jsonPathEvaluator = response_TRAGREDREV.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

	}

	@Given("Target member wants to tranfer points to source member")
	public void target_member_wants_to_tranfer_points_to_source_member() {
		targetKey = gettargetKey();
		System.out.println("Target key: " + targetKey);
	}

	@When("Target member perform point transfer to source member")
	public void target_member_perform_point_transfer_to_source_member() {
		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String description = "Test Transfer";
		String clientKey = gettargetKey();
		String destclientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		response_TARGPT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when()
				.post("/api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Then("Target member should be able to transfer points successfully with status code {int}")
	public void target_member_should_be_able_to_transfer_points_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response_TARGPT.getStatusCode());

		JsonPath jsonPathEvaluator = response_TARGPT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Then("The message template is {string}")
	public void the_message_template_is(String string) {

	}

	@Given("Target member wants to check the event after point transfer")
	public void target_member_wants_to_check_the_event_after_point_transfer() {
		targetKey = gettargetKey();
		System.out.println("Target key: " + targetKey);
	}

	@When("Target member perform event history after point transfer")
	public void target_member_perform_event_history_after_point_transfer() {

		targetKey = gettargetKey();
		response_TRAGTVELST = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"membershipKey\", \"value\": \"" + targetKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/loyaltyops/events/list");
	}

	@Then("Target member should have all events history after point transfe with status code {int}")
	public void target_member_should_have_all_events_history_after_point_transfe_with_status_code(Integer statuscode)
			throws InterruptedException {

		Assert.assertEquals(statuscode.intValue(), response_TRAGTVELST.getStatusCode());
		JsonPath jsonPtEval = response_TRAGTVELST.jsonPath();

		boolean getstatus = jsonPtEval.get("success");
		Assert.assertEquals(getstatus, true);

	}

	@Then("Target member should contain the fields {string} and {string} after point transfer")
	public void target_member_should_contain_the_fields_and_after_point_transfer(String string, String string2) {

		JsonPath jsonPtEval = response_TRAGTVELST.jsonPath();
		// targetKey = getsourceKey();
		sourceKey = getsourceKey();

		String str_data1 = null;
		String str_data2 = null;
		String str_data3 = null;

		List<String> lstIds = jsonPtEval.getList("data.conditionData.transferredTo");
		System.out.println("The events history transferredTo: " + lstIds);
		lstIds = lstIds.stream().filter(x -> x != null).collect(Collectors.toList());

		System.out.println("New values transferredTo: " + lstIds);
		for (String element : lstIds) {
			if (sourceKey.equals(element)) {
				// if (element.equals("sourceKey")) {
				System.out.println("element" + element);
				str_data1 = element;
				break;
			}
		}

		List<String> _lstfrmIds = jsonPtEval.getList("data.conditionData.transferredFrom");
		_lstfrmIds = _lstfrmIds.stream().filter(x -> x != null).collect(Collectors.toList());
		System.out.println("The events history transferredFrom: " + _lstfrmIds);
		for (String id : _lstfrmIds) {
			if (sourceKey.equals(id)) {// here check the field contais or not
				str_data3 = id;
				break;
			}
		}
		Assert.assertEquals(sourceKey, str_data3);
		Assert.assertEquals(sourceKey, str_data1);

		/*String strReason = "Test transfer";

		List<String> _lstreasons = jsonPtEval.getList("data.conditionData.description");
		_lstreasons = _lstreasons.stream().filter(x -> x != null).collect(Collectors.toList());
		System.out.println("The events history transferReason:" + _lstreasons);
		for (String val : _lstreasons) {
			if (strReason.equals(val)) {// here check the field contais or not
				str_data2 = val;
				break;
			}
		}

		Assert.assertEquals(strReason, str_data2);*/
	}

	@Given("Source member wants to do point transfer")
	public void source_member_wants_to_do_point_transfer() {
		sourceKey = getsourceKey();
		System.out.println("The source key:" + sourceKey);
	}

	@When("Source member perform point transafer to target member for {string} points")
	public void source_member_perform_point_transafer_to_target_member_for_points(String strpoints) {

		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String amount = strpoints;
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		
		response_R1SPT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
		
	    response_ACCSTR1 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().get("/api/v10/deposits/" + sourceKey + "/statement?type=bonus&limit=0");	
	}
	
	@Then("{string} points should be deducted from the source member original balance")
	public void points_should_be_deducted_from_the_source_member_original_balance(String strpoints) {
	   
		Assert.assertEquals(HttpStatus.SC_OK, response_R1SPT.getStatusCode());
		JsonPath getjsonValSrcReq = response_R1SPT.jsonPath();
		Float StrOrognVal=getjsonValSrcReq.get("data.balance");
		
		Assert.assertEquals(HttpStatus.SC_OK, response_ACCSTR1.getStatusCode());

		JsonPath getjsonSrcVal = response_ACCSTR1.jsonPath();
		
		Float  strAcctBlance = getjsonSrcVal.get("data.balance");
		
		Assert.assertEquals(StrOrognVal, strAcctBlance);
	}

	@When("Source member again perform point transafer to target member {string}  points")
	public void source_member_again_perform_point_transafer_to_target_member_points(String strPoints) {
		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String amount = strPoints;
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);
		
		response_R2SPTP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
		
		response_ACCSTR2 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().get("/api/v10/deposits/" + sourceKey + "/statement?type=bonus&limit=0");	
	}

	@Then("Again {string} should be deducted from the source member original balance")
	public void again_should_be_deducted_from_the_source_member_original_balance(String strpoints) {
	   
		Assert.assertEquals(HttpStatus.SC_OK, response_R2SPTP.getStatusCode());
		JsonPath getjsonValReq = response_R2SPTP.jsonPath();
		Float StrOrognVal=getjsonValReq.get("data.balance");
		
		Assert.assertEquals(HttpStatus.SC_OK, response_ACCSTR2.getStatusCode());

		JsonPath getjsonVal = response_ACCSTR2.jsonPath();
		
		Float  strAcctBlance = getjsonVal.get("data.balance");
		
		Assert.assertEquals(StrOrognVal, strAcctBlance);
	}
	@When("Target member perform point transafer to source member for {string} points")
	public void target_member_perform_point_transafer_to_source_member_for_points(String strPoints) {

		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String amount = strPoints;
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responseTARNW = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
		
		response_TARACCSTR = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().get("/api/v10/deposits/" + sourceKey + "/statement?type=bonus&limit=0");	
	}

	@Then("Target member balance should be {string} points less from the original balance")
	public void target_member_balance_should_be_points_less_from_the_original_balance(String string) {
		Assert.assertEquals(HttpStatus.SC_OK, responseTARNW.getStatusCode());
		JsonPath getjsonValReq = responseTARNW.jsonPath();
		Float StrOrognVal=getjsonValReq.get("data.balance");
		
		Assert.assertEquals(HttpStatus.SC_OK, response_TARACCSTR.getStatusCode());

		JsonPath getjsonVal = response_TARACCSTR.jsonPath();
		
		Float  strAcctBlance = getjsonVal.get("data.balance");
		
		Assert.assertEquals(StrOrognVal, strAcctBlance);
	}
}