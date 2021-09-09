package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;

import com.loyaltyprimemultipartnerapp.utlis.AIPsJsonBody;
import com.loyaltyprimemultipartnerapp.utlis.ExcelReader;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.loyaltyprimemultipartnerapp.utlis.TestUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class AddTransactionSteps {
	private String sourceKey;// source key
	private String TrxId;
	private String TrxDate;
	private Response response = null; // Response
	Response responseSM;
	Response responseTXM;
	Response responseTXMRV;
	HttpHeaders headers;
	String strapiBody;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	public static String getclientKey() {
		return _clientKey;
	}

	private static String _clientKey;

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

	public static String getTrxIdforRev() {
		return _TrxIdRev;
	}

	private static String _TrxIdRev;

	public static String getTrxDateforRev() {
		return _TrxDateRev;
	}

	private static String _TrxDateRev;
	
	public static String getTxDuplicate() {
		return _txDuplicate;
	}

	private static String _txDuplicate;
	
	

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

	@Given("User wants to enroll a member")
	public void user_wants_to_enroll_a_member() {
		sourceKey = LoadProperties().getProperty("enkey") + "_SRC" + TestUtils.getRandomAlphaNumbericValue()
				+ TestUtils.getRandomValue();
		_sourceKey = sourceKey;

	}

	@When("User performs enrollment")
	public void user_performs_enrollment() throws InterruptedException {

		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRC_AltId" + TestUtils.getRandomValue()
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

		responseSM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
		Thread.sleep(10000);
	}

	@Then("User should be able to enroll member successfully with status code {int}")
	public void user_should_be_able_to_enroll_member_successfully_with_status_code(Integer int1) {

		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("User must have the valid Post Transaction request parameters and authorization APIkey")
	public void user_must_have_the_valid_Post_Transaction_request_parameters_and_authorization_APIkey() {

		sourceKey = getsourceKey();
		System.out.println("client key:" + sourceKey);
		SerenityRest.given();

	}

	@When("User makes Transaction API Request using POST method, valid parameters and valid APIkey for clientId {string} and txId {string}")
	public void user_makes_Transaction_API_Request_using_POST_method_valid_parameters_and_valid_APIkey_for_clientId_and_txId(
			String clientId, String txId) {

		String key = getsourceKey();
		String date = TestUtils.getDateTime();
		String transx = txId + TestUtils.getRandomValue();
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
		String partnerId = LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(transx, key, date, "1", "10", productKey, channelkey, partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("User should see success as response and transaction must be added to the system.")
	public void user_should_see_success_as_response_and_transaction_must_be_added_to_the_system() {

		String responseBody = response.getBody().toString();
		System.out.println("responseBody --->" + responseBody);

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		System.out.println("Transaction is Added successfully:" + response.asString());
	}

	@When("User makes Transaction API Request using POST method, valid parameters and valid APIkey")
	public void user_makes_Transaction_API_Request_using_POST_method_valid_parameters_and_valid_APIkey() {

		String key = getsourceKey();
		String date = TestUtils.getDateTime();
		String transx = "Tx_" + TestUtils.getRandomValue();
		_txDuplicate=transx;
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
		String partnerId = LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(transx, key, date, "1", "10", productKey, channelkey, partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		System.out.println("response: " + response.prettyPrint());
	}

	@Then("User should receive the clientId as {string} and should have program as {string} in Response")
	public void user_should_receive_the_clientId_as_and_should_have_program_as_in_Response(String clientId,
			String program) {

		String getClientId = response.path("data.clientId");
		String getProgram = response.path("data.program");

		// Let us print the city variable to see what we got
		System.out.println("clientId received from Response " + getClientId);
		System.out.println("clientId received from Response " + getProgram);

		// Validate the response
		Assert.assertEquals(getClientId, getsourceKey());
		Assert.assertEquals(getProgram, program);
	}

	@When("User makes the POST  Transaction request for transaction ID {string} which is already existing in the system")
	public void user_makes_the_POST_Transaction_request_for_transaction_ID_which_is_already_existing_in_the_system(
			String trxId) {
		String key = getsourceKey();
		String transx = getTxDuplicate();//trxId;
		String date = TestUtils.getDateTime();
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
		String partnerId = LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(transx, key, date, "1", "10", productKey, channelkey, partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		System.out.println("response: " + response.prettyPrint());
	}

	@Then("User should not be able to post the trx and receive status as false")
	public void user_should_not_be_able_to_post_the_trx_and_receive_status_as_false() {

		Assert.assertEquals(response.path("success"), false);
	}

	@Given("Member call the earn transaction")
	public void member_call_the_earn_transaction() {

		sourceKey = getsourceKey();
		System.out.println("The cleitn key:" + sourceKey);
	}

	@When("Add the earn transaction to the system with details MembereTrx and rownumber {int}")
	public void add_the_earn_transaction_to_the_system_with_details_MembereTrx_and_rownumber(Integer rowNumber)
			throws InterruptedException, InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		TrxDate = TestUtils.getDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		String price = testData.get(rowNumber).get("Price");
		String quantity = testData.get(rowNumber).get("Quantity");
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

		sourceKey = getsourceKey();// get the key value here from the first class
		_clientKey = sourceKey;

		String partnerId = LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPIBody(TrxId, sourceKey, TrxDate, partnerId, price, quantity, productKey,
				channelkey, productKey2, channelKey2, productKey3, channelKey3);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		System.out.println("Transaction added successfully:" + response.asString());
		Thread.sleep(20000);

	}

	@Then("The earn transaction should be successfull with status code {int}")
	public void the_earn_transaction_should_be_successfull_with_status_code(Integer int1) throws InterruptedException {

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		System.out.println("Transaction added successfully:" + response.asString());

		String partnerId = LoadProperties().getProperty("partnerId");

		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		String gettxId = jsonPathEvaluator.get("data.txId");
		System.out.println("TxId received from Response " + gettxId);

		String getClientId = jsonPathEvaluator.get("data.txId");
		System.out.println("ClientId received from Response " + getClientId);

		Thread.sleep(20000);
		Assert.assertEquals(gettxId, TrxId + "_" + partnerId);

		/*
		 * String getBonusPointsType =
		 * jsonPathEvaluator.get("data.rewards.BONUS.pointsType");
		 * System.out.println("Get Bonus PointsType from Response " +
		 * getBonusPointsType); Assert.assertEquals(getBonusPointsType, "BONUS");
		 * 
		 * String getTotalBonusPoints =
		 * jsonPathEvaluator.get("data.rewards.BONUS.total");
		 * System.out.println("Get Bonus Points in Response " + getTotalBonusPoints);
		 * Assert.assertEquals(getTotalBonusPoints, "4601.060");
		 * 
		 * String getVolumePointsType =
		 * String.valueOf(jsonPathEvaluator.get("data.rewards.VOLUME.pointsType"));
		 * System.out.println("Get volume PointsType from Response " +
		 * getVolumePointsType); Assert.assertEquals(getVolumePointsType, "VOLUME");
		 * 
		 * String getTotalVolumePoints =
		 * String.valueOf(jsonPathEvaluator.get("data.rewards.VOLUME.total"));
		 * System.out.println("Get Bonus PointsType from Response " +
		 * getTotalVolumePoints); Assert.assertEquals(getTotalVolumePoints, "2087.30");
		 */

	}

	@Given("Member call the redeemption")
	public void member_call_the_redeemption() {

		sourceKey = getsourceKey();
		System.out.println("Get Member Id:" + sourceKey);
	}

	@When("Add new redeemption to the system with details MembereTrx and rownumber {int}")
	public void add_new_redeemption_to_the_system_with_details_MembereTrx_and_rownumber(Integer rowNumber)
			throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");
		TrxDate = getTrxDate();// get date value from last
		String receiptId = testDataRdeem.get(rowNumber).get("ReceiptId") + TestUtils.getRandomValue();
		_receiptId = receiptId;
		String debitorId = LoadProperties().getProperty("debitorId");
		_debitorId = debitorId;
		String amount = testDataRdeem.get(rowNumber).get("Amount");
		_amount = amount;
		String description = testDataRdeem.get(rowNumber).get("Description");
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");

		System.out.println("Member redemption has been done successfully:" + response.asString());
		Thread.sleep(20000);
	}

	@Then("Redeemption should display with added details")
	public void redeemption_should_display_with_added_details() {

		Assert.assertEquals(200, response.getStatusCode());

		/*
		 * JsonPath jsonPathEvaluator = response.jsonPath(); String getCleientId =
		 * jsonPathEvaluator.get("data.membershipKey");
		 * 
		 * System.out.println("client received from Response " + getCleientId);
		 * 
		 * sourceKey = getsourceKey(); Assert.assertEquals(sourceKey, getCleientId);
		 * 
		 * boolean getStatus = jsonPathEvaluator.get("success");
		 * Assert.assertEquals(getStatus, true);
		 */
	}

	@Given("Member call the Adjustment")
	public void member_call_the_Adjustment() {

		sourceKey = getsourceKey();
		System.out.println("Client key:" + sourceKey);
	}

	@When("Add member points adjustment")
	public void add_member_points_adjustment() throws InterruptedException {

		String receiptId = getReceiptId();
		String date = TestUtils.getDateTime();
		String amount = getAmount();
		String debitorId = LoadProperties().getProperty("partnerId");
		String description = "Test adjustment";
		sourceKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount, debitorId, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/adjust?type=BONUS");

		Thread.sleep(10000);

	}

	@Then("Adjustment should happens with valid data")
	public void adjustment_should_happens_with_valid_data() {

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Given("Member call the transaction reversal")
	public void member_call_the_transaction_reversal() {
		sourceKey = getsourceKey();
		System.out.println("client key:" + sourceKey);

	}

	@When("Member post the earn transaction in system with valid input")
	public void member_post_the_earn_transaction_in_system_with_valid_input() throws InterruptedException {
		String partnerId = LoadProperties().getProperty("partnerId");
		String strTxKey = "TXINT";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		_TrxIdRev = TrxId + "_" + partnerId; // Set value here for transaction
		TrxDate = TestUtils.getDateTime();
		_TrxDateRev = TrxDate;

		String price = "10";
		String quantity = "1";
		String clientKey = sourceKey;
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

		responseTXM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
		Thread.sleep(10000);
	}

	@Then("Earn transaction should be successfull with status code {int}")
	public void earn_transaction_should_be_successfull_with_status_code(Integer code) {
		// check success status
		Assert.assertEquals(code.intValue(), responseTXM.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXM.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("Member post the transaction reversal in system with valid input")
	public void member_post_the_transaction_reversal_in_system_with_valid_input() {

		String description = "Test Tx Reversal";
		String TrxId = getTrxIdforRev();
		String TrxDate = getTrxDateforRev();
		String debitorId = LoadProperties().getProperty("debitorId");
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, sourceKey, description, debitorId);

		responseTXMRV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx/reversal");
	}
	
	@Then("Transaction reversal should be successfull with valid response")
	public void transaction_reversal_should_be_successfull_with_valid_response() {
		Assert.assertEquals(HttpStatus.SC_OK, responseTXMRV.getStatusCode());
		JsonPath jsonPathEvaluator = responseTXMRV.jsonPath();


		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

}