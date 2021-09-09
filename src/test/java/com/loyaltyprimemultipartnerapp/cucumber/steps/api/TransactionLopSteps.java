package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loyaltyprimemultipartnerapp.utlis.AIPsJsonBody;
import com.loyaltyprimemultipartnerapp.utlis.ExcelReader;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.loyaltyprimemultipartnerapp.utlis.TestUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;

public class TransactionLopSteps {
	private String clientKey;// Source key
	private String TrxId;
	private String TrxDate;
	private Response response = null; // Response
	HttpHeaders headers;
	String strapiBody;
	private RequestSpecification request;
	Response responseSM;
	Response responseAFTTRX = null;
	Response responseBFTX;
	Response responseCANSM; 
	Response responseCANSMUPD;
	private String CancelclientKey;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	public static String getcancelsourceKey() {
		return _cancelsourceKey;
	}

	private static String _cancelsourceKey;

	public static String getTrxId() {
		return _TrxId;
	}

	private static String _TrxId;

	public static String getTrxIdval() {
		return _TrxIdval;
	}

	private static String _TrxIdval;

	public static float getMBalance() {
		return _MBalance;
	}

	private static float _MBalance;

	@Given("User wants to enroll a member to perform transaction")
	public void user_wants_to_enroll_a_member_to_perform_transaction() {
		clientKey = LoadProperties().getProperty("enkey") + "_SRC" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
	}

	@When("User performs enrollment to do transaction")
	public void user_performs_enrollment_to_do_transaction() {
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRC_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = clientKey;
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

		responseSM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("User should be able to enroll member to do transaction successfully with status code {int}")
	public void user_should_be_able_to_enroll_member_to_do_transaction_successfully_with_status_code(Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member perfom transaction in the system with details MembereTrx and rownumber {int}")
	public void member_perfom_transaction_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber)
			throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");
		String strTxKey = "TXINT";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		_TrxId = TrxId;
		TrxDate = TestUtils.getDateTime();

		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		clientKey = getsourceKey();
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
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey,partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
		System.out.println("Transaction added successfully:" + response.asString());
		Thread.sleep(10000);
	}

	@Then("Member should be able to get points successfully with status code {int}")
	public void member_should_be_able_to_get_points_successfully_with_status_code(Integer statuscode) {
		// check success status
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("Member call the earn transaction when member is blank")
	public void member_call_the_earn_transaction_when_member_is_blank() {
		clientKey = "";
		System.out.println("Member is blank" + clientKey);
	}

	@When("the member perfom earn transaction with balnk member number in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_with_balnk_member_number_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId");
		String channelkey = "";
		String productKey = "";
		clientKey = "";
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

	@Then("It should be equal to status code {int} and message{string}")
	public void it_should_be_equal_to_status_code_and_message(Integer statuscode, String errormessage) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		String getBonusPointsType = jsonPathEvaluator.get("error");
		Assert.assertEquals(getBonusPointsType, errormessage);
	}

	@Given("Member call the earn transaction when productkey is blank")
	public void member_call_the_earn_transaction_when_productkey_is_blank() {
		SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/tx");
	}

	@When("the member perfom earn transaction when productkey is blank in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_when_productkey_is_blank_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		clientKey = getsourceKey();
		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.getDateTime();
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId");
		String channelkey = "";
		String productKey = "";

		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getChannelKey = jsonPathEval.get("data[0].channelKey");

		channelkey = _getChannelKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("It should be equal to status code {int} and status{string}")
	public void it_should_be_equal_to_status_code_and_status(Integer statuscode, String status) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(String.valueOf(getStatus), status);

		/*
		 * String getBonusPointsType = jsonPathEvaluator.get("data.rewards");
		 * Assert.assertEquals(getBonusPointsType, "");
		 */
	}

	@Given("Member call the earn transaction channel key is not passed in transaction")
	public void member_call_the_earn_transaction_channel_key_is_not_passed_in_transaction() {

		clientKey = getsourceKey();
		System.out.println("The client :" + clientKey);
	}

	@When("the member perfom earn transaction when channel key is not passed in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_when_channel_key_is_not_passed_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String partnerId=LoadProperties().getProperty("partnerId");
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		clientKey = getsourceKey();

		String productKey = "";
		String channelKey1 = "without";
		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");

		productKey = _getProductKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelKey1, partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
		Thread.sleep(2000);
	}

	@Given("Member call the earn transaction with amount is passed as zero")
	public void member_call_the_earn_transaction_with_amount_is_passed_as_zero() {

		clientKey = getsourceKey();
		System.out.println("Client Key:" + clientKey);
	}

	@When("the member perfom earn transaction when amount is passed as zero in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_when_amount_is_passed_as_zero_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "T_" + TestUtils.getRandomValue();
		String quantity = testData.get(rownumber).get("Quantity");
		TrxDate = TestUtils.getDateTime();
		String price = "0";
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();

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

		System.out.println("Transaction added successfully:" + response.asString());
		Thread.sleep(2000);
	}

	@Given("Member call the earn transaction with negative amount in transaction")
	public void member_call_the_earn_transaction_with_negative_amount_in_transaction() {
		clientKey = getsourceKey();
		System.out.println("The clientkey " + clientKey);
	}

	@When("the member perfom earn transaction when negative amount is passed in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_when_negative_amount_is_passed_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "N_" + TestUtils.getRandomValue();
		String quantity = testData.get(rownumber).get("Quantity");
		TrxDate = TestUtils.getDateTime();
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();
		String price = "-5";
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
		Thread.sleep(2000);
	}

	@Given("Member call the earn transaction when amount is not passed in transaction")
	public void member_call_the_earn_transaction_when_amount_is_not_passed_in_transaction() {

		clientKey = getsourceKey();
		System.out.println("Member Id:" + clientKey);
	}

	@When("the member perfom earn transaction when amount is not passed in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_when_amount_is_not_passed_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws OpenXML4JException, Exception {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "A_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();
		String price = "without";
		String quantity = testData.get(rownumber).get("Quantity");
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
		Thread.sleep(2000);
	}

	@Given("Member call the earn transaction without date in transaction")
	public void member_call_the_earn_transaction_without_date_in_transaction() {
		clientKey = getsourceKey();
		System.out.println("The client key :" + clientKey);
	}

	@When("the member perfom earn transaction without date in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_without_date_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "WD_" + TestUtils.getRandomValue();
		TrxDate = "without";
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
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

	@Given("Member call the earn transaction with blank date in transaction")
	public void member_call_the_earn_transaction_with_blank_date_in_transaction() {

		clientKey = getsourceKey();
		System.out.println("clientkey :" + clientKey);
	}

	@When("the member perfom earn transaction with blank date in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_with_blank_date_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "BD_" + TestUtils.getRandomValue();
		TrxDate = "";
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();
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

	@Given("Member call the earn transaction with invalid date format in transaction")
	public void member_call_the_earn_transaction_with_invalid_date_format_in_transaction() {
		clientKey = getsourceKey();
		System.out.println("client key :" + clientKey);
	}

	@When("the member perfom earn transaction with invalid date format in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_with_invalid_date_format_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "ID_" + TestUtils.getRandomValue();
		TrxDate = "2021-01-20";
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();

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

	@Given("Member call the earn transaction with blank transaction id in transaction")
	public void member_call_the_earn_transaction_with_blank_transaction_id_in_transaction() {
		clientKey = getsourceKey();
		System.out.println("client key :" + clientKey);
	}

	@When("the member perfom earn transaction with blank transaction id in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_with_blank_transaction_id_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");
		TrxId = "";
		TrxDate = TestUtils.getDateTime();
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();
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

	@Given("Member call the earn transaction with invalid partner value in transaction")
	public void member_call_the_earn_transaction_with_invalid_partner_value_in_transaction() {

		clientKey = getsourceKey();
		System.out.println("client key :" + clientKey);
	}

	@When("the member perfom earn transaction with invalid partner value in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_with_invalid_partner_value_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId") + "invalid";// make invalid partner
		clientKey = getsourceKey();

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

	@Given("Member call the earn transaction with blank partner value in transaction")
	public void member_call_the_earn_transaction_with_blank_partner_value_in_transaction() {

		clientKey = getsourceKey();
		System.out.println("client key :" + clientKey);
	}

	@When("the member perfom earn transaction with blank partner value in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_with_blank_partner_value_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String price = testData.get(rownumber).get("Price");
		String partnerId = "";
		clientKey = getsourceKey();
		String quantity = testData.get(rownumber).get("Quantity");
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

	@Given("Member call the earn transaction for cancelled member")
	public void member_call_the_earn_transaction_for_cancelled_member() {
		clientKey = getsourceKey();
		System.out.println("client key :" + clientKey);
	}
	@When("Member performs enrollment to do transaction and mark it cancelled")
	public void member_performs_enrollment_to_do_transaction_and_mark_it_cancelled() {
	    
		
		CancelclientKey = LoadProperties().getProperty("enkey") + "_MEBCAN" + TestUtils.getRandomValue();
		_cancelsourceKey = CancelclientKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MEBCAN_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = CancelclientKey;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, CancelclientKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		responseCANSM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
		
		responseCANSMUPD=SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log()
				.all().when().post("api/v10/memberships/"+CancelclientKey+"/cancel");
	}

	@Then("Member should be created and marked as cancelled with status {int}")
	public void member_should_be_created_and_marked_as_cancelled_with_status(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseCANSM.getStatusCode());
		
		Assert.assertEquals(code.intValue(), responseCANSMUPD.getStatusCode());

		JsonPath jsonPathEvaluator = responseCANSMUPD.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

	}

	@When("the member perfom earn transaction for cancelled member in transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_for_cancelled_member_in_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		String strTxKey = "TXING";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String price = "10";
		String quantity = "1";
		String partnerId=LoadProperties().getProperty("partnerId");

		clientKey = getcancelsourceKey();// cancelled member
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

	@Then("It should gives us the error message for cancelled member{string}")
	public void it_should_gives_us_the_error_message_for_cancelled_member(String strMsg) {

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		String getErrormsg = jsonPathEvaluator.get("error");

		Assert.assertEquals(getErrormsg, strMsg.replace("cleientkey", clientKey));

	}

	@Then("It should gives us the errormessage{string}")
	public void it_should_gives_us_the_errormessage(String errormessage) {

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		String getmessage = jsonPathEvaluator.get("error");
		Assert.assertEquals(getmessage, errormessage);
	}

	@When("the member perfom earn transaction for duplicate transactionid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_for_duplicate_transactionid_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");

		TrxId = getTrxId();
		TrxDate = TestUtils.getDateTime();
		clientKey = getsourceKey();
		String partnerId=LoadProperties().getProperty("partnerId");

		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
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
		Thread.sleep(10000);
	}

	@When("Member perfom earn transaction with special characters in transactionid in the system with details MembereTrx and rownumber {int}")
	public void member_perfom_earn_transaction_with_special_characters_in_transactionid_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");
		// String strTxKey = testData.get(rownumber).get("TransactionId");
		TrxId = "*~$%" + TestUtils.getSpecialKeyward()+ TestUtils.getRandowmSpecialCharacters();// create unique special keywaord characters
		TrxDate = TestUtils.getDateTime();
		String price = testData.get(rownumber).get("Price");
		String quantity = testData.get(rownumber).get("Quantity");
		String partnerId=LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();
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

	@Then("It should gives us the duplicate message{string}")
	public void it_should_gives_us_the_duplicate_message(String message) {

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
		// errDetails.msgTemplate-old errDetails.msgData.joinedMsgTemplates
		String getErrorMsg = jsonPathEvaluator.get("errDetails.msgTemplate");

		Assert.assertEquals(getErrorMsg, message);
	}

	@Then("It should gives us the error message{string}")
	public void it_should_gives_us_the_error_message(String strMsg) {

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
		// errDetails.msgTemplate
		String getErrorMsg = jsonPathEvaluator.get("errDetails.msgTemplate");

		Assert.assertEquals(getErrorMsg, strMsg);
	}

	@Then("It should gives us the invalid partner message{string}")
	public void it_should_gives_us_the_invalid_partner_message(String strMsg) {

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		// String getErrorMsg = jsonPathEvaluator.get("errDetails.msgTemplate");
		String getErrormsg = jsonPathEvaluator.get("errDetails.msgTemplate");
		// String[] splitString =getErrormsg.split(":");
		Assert.assertEquals(getErrormsg, strMsg);
	}

	@Then("Transaction detail should be displayed in member trasaction history")
	public void transaction_detail_should_be_displayed_in_member_trasaction_history() {
		response = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"txId\", \"value\": \"" + TrxId + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/tx");

		String _gettxId = null;
		// check success status
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonPathEvaluator1 = response.jsonPath();
		List<String> al = jsonPathEvaluator1.get("data.txId");
		String[] stringArr = al.stream().toArray(String[]::new);
		for (String s : stringArr) {
			_gettxId = s;
		}
		Assert.assertEquals(_gettxId.toString(), TrxId);
	}

	@Then("Transaction reward history should be displayed")
	public void transaction_reward_history_should_be_displayed() {
		/// Check the member rewardsHistory
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/tx/" + TrxId + "/rewardsDetails");
		// check success status
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		JsonPath getjsonPathEvaluator = response.jsonPath();

		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String gettxId = getjsonPathEvaluator.get("data.txId");
		System.out.println("reward history Response " + gettxId);
		Assert.assertEquals(gettxId, TrxId);

		String getBonusPointsType = getjsonPathEvaluator.get("data.rewards.BONUS.pointsType");
		Assert.assertEquals(getBonusPointsType, "BONUS");
	}

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
}
