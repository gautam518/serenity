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

public class TransactionRedemptionLopSteps {
	private String clientKey;// Source key
	private String receiptId;
	private String debitorId;
	private String description;
	private String amount;
	private Response response = null; // Response
	private Response responseSM  = null;
	private Response  responseTX  = null;
	HttpHeaders headers;
	String strapiBody;
	private String TrxId;
	private String TrxDate;
	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	public static String getTrxDate() {return _TrxDate;}
	private static String _TrxDate;
	
	public static String getRcptId() {return _RcptId;}
	private static String _RcptId;

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
	
	@Given("Member wants to enrollment on PCV{int} for redemption")
	public void member_wants_to_enrollment_on_PCV_for_redemption(Integer int1) {
		clientKey = LoadProperties().getProperty("enkey") + "_SRC" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
	}

	@When("Member performs enrollment on PCV{int} for redemption")
	public void member_performs_enrollment_on_PCV_for_redemption(Integer int1) {
	   
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRC_AltId" +TestUtils.getRandomValue()+ TestUtils.getRandomNumber();
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
	
	@Then("Member should be created in pcv{int} for redemption with statuscode {int}")
	public void member_should_be_created_in_pcv_for_redemption_with_statuscode(Integer int1, Integer int2) {
		
		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("The member performs transaction on PCV{int} for redemption")
	public void the_member_performs_transaction_on_PCV_for_redemption(Integer int1) throws InterruptedException {
	  
		String strTxKey = "TXRED";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		TrxDate = TestUtils.getDateTime();
		String price = "20";
		String quantity = "1";
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

		clientKey = getsourceKey();// get the key value here from the first class
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, price, quantity, productKey, channelkey,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		System.out.println("Transaction added for redemption:" + responseTX.asString());
		Thread.sleep(2000);
	}
	@Then("Then transaction should be successful with status code {int}")
	public void then_transaction_should_be_successful_with_status_code(Integer int1) {
	  
		Assert.assertEquals(HttpStatus.SC_OK, responseTX.getStatusCode());
		System.out.println("Transaction Redeption:" + responseTX.asString());
	}


	@Given("Member call the point redemption")
	public void member_call_the_point_redemption() {
		clientKey = getsourceKey();
		System.out.println("The client ky:" + clientKey);
	}

	@When("The member perfom point redemption with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_with_details_MembereTrx_and_rownumber(Integer rownumber)
			throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		_TrxDate=TrxDate;
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId") + "_" + TestUtils.getRandomValue();
		_RcptId=receiptId;
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");
		

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(100);
	}

	@Then("Member should able to do point redemption successfully with status code {int}")
	public void member_should_able_to_do_point_redemption_successfully_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		System.out.println("clientID received from Response " + getCleientId);

		Assert.assertEquals(clientKey, getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("TxId received from Response " + getStatus);
	}

	@Then("The redemption detail should be displayed in member account statement")
	public void the_redemption_detail_should_be_displayed_in_member_account_statement() {

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		debitorId = LoadProperties().getProperty("debitorId");
		String strReciptId=receiptId+"_"+debitorId;
		
		// match the redemption id here
		String getAmountRedem = null;
		List aList = response.jsonPath().getList("data.bookings.receiptId.uniqueId");
		for (int i = 0; i < aList.size(); i++) {
			System.out.print(aList.get(i));

			if (aList.get(i).equals(strReciptId)) {
				getAmountRedem = aList.get(i).toString();
				System.out.println("receiptId value" + getAmountRedem);
			}
		}
	
		Assert.assertEquals(getAmountRedem,strReciptId);

	}

	@When("The member perfom point redemption with details receiptid{string} date{string} debitorId{string} amount{int} and description{string}")
	public void the_member_perfom_point_redemption_with_details_receiptid_date_debitorId_amount_and_description(
			String receiptid, String strdate, String debitorId, Integer amount, String description) {

		String receiptId = receiptid+"_" + TestUtils.getRandomValue();
		String date=strdate;
		clientKey = getsourceKey();
		debitorId = LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, date,debitorId, amount.toString(), description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}

	@Given("Member call the point redemption with blank receiptid")
	public void member_call_the_point_redemption_with_blank_receiptid()
			throws InvalidFormatException, IOException, InterruptedException {

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption when the receiptid is passed as blank in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_when_the_receiptid_is_passed_as_blank_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = "";
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);

	}

	@Then("Member should not able to do point redemption with status code {int}")
	public void member_should_not_able_to_do_point_redemption_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		/*JsonPath jsonPathEvaluator = response.jsonPath();

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
		System.out.println("Response of API " + getStatus);*/
	}

	@Given("Member call the point redemption for any value of receiptid")
	public void member_call_the_point_redemption_for_any_value_of_receiptid() {
	clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perform point redemption when any value of receiptid is passed in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_redemption_when_any_value_of_receiptid_is_passed_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId") + "_" + TestUtils.getRandomValue();
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");


		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);
	}

	@Then("Member should able to do point redemption with status code {int}")
	public void member_should_able_to_do_point_redemption_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Response of API " + getStatus);

	}

	@Given("Member call the point redemption without receiptid")
	public void member_call_the_point_redemption_without_receiptid() {
		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perform point redemption without receiptid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_redemption_without_receiptid_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = "without";
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);

	}

	@Given("Member call the transaction redemption when type is not passed")
	public void member_call_the_transaction_redemption_when_type_is_not_passed(){

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);

	}

	@When("The member perfom point redemption without type in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_without_type_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId");
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(10000);

	}

	@Given("Member call the point redemption with non numeric value")
	public void member_call_the_point_redemption_with_non_numeric_value() {

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perform point redemption when amount is passed as alphabet in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_redemption_when_amount_is_passed_as_alphabet_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId");
		debitorId = LoadProperties().getProperty("debitorId");
		amount = "baab";
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(10000);

	}

	@Given("Member call the point redemption when amount is zero")
	public void member_call_the_point_redemption_when_amount_is_zero(){
		clientKey = getsourceKey();
		
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption with amount as zero in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_with_amount_as_zero_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId");
		debitorId = LoadProperties().getProperty("debitorId");
		amount = "0";
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(10000);

	}

	@Given("Member call the point redemption without amount")
	public void member_call_the_point_redemption_without_amount() {
		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption when amount is not passed in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_when_amount_is_not_passed_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId");
		debitorId = LoadProperties().getProperty("debitorId");
		amount = "without";
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(10000);

	}
	@Given("Member call the point redemption when debitorId is blank")
	public void member_call_the_point_redemption_when_debitorId_is_blank(){

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption when debitorId is passed as blank in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_when_debitorId_is_passed_as_blank_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId")+"_"+TestUtils.getRandomValue();
		debitorId = "";
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");
	
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(10000);

	}

	@Given("Member call the point redemption without debitorId")
	public void member_call_the_point_redemption_without_debitorId() {

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}
	@When("The member perform point reversal when debitorId is not passed in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_reversal_when_debitorId_is_not_passed_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = "Rex"+"X_"+TestUtils.getRandomAlphaNumbericValue()+TestUtils.getRandomNumber();
		debitorId ="without";
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);

	}

	@Given("Member call the point redemption when date is blank")
	public void member_call_the_point_redemption_when_date_is_blank(){

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption when date is blank in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_when_date_is_blank_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		TrxDate = "";
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId");
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");
		clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);

	}

	@Given("Member call the point redemption with invalid date")
	public void member_call_the_point_redemption_with_invalid_date() {

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption with invalid date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_with_invalid_date_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		TrxDate = "25-02-2021";
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId");
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");
		clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);

	}

	@Given("Member call the point redemption with invalid type")
	public void member_call_the_point_redemption_with_invalid_type(){

		clientKey = getsourceKey();
		System.out.println("Member key :" + clientKey);
	}

	@When("The member perfom point redemption when type is passed as volume in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_redemption_when_type_is_passed_as_volume_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testDataRdeem = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"RedeemTx");

		clientKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();
		receiptId = testDataRdeem.get(rownumber).get("ReceiptId")+"E_"+TestUtils.getRandomNumber();
		debitorId = LoadProperties().getProperty("debitorId");
		amount = testDataRdeem.get(rownumber).get("Amount");
		description = testDataRdeem.get(rownumber).get("Description");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=VOLUME");

		System.out.println("Member redemption response:" + response.asString());
		Thread.sleep(1000);
	}
}
