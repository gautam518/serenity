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

public class TransactionReversalLopSteps {
	private String clientKey;// Source key

	private String TrxDate;
	private Response response = null; // Response
	private Response responseTRXM;
	HttpHeaders headers;
	String strapiBody;
	private String TrxId;

	public static String getTrxId() {return _TrxId;}
	private static String _TrxId;
	
	public static String getTrxDate() {return _TrxDate;}
	private static String _TrxDate;
	
	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	@Given("Member wants to enrollment for transaction reversal")
	public void member_wants_to_enrollment_for_transaction_reversal() {
		clientKey = LoadProperties().getProperty("enkey") + "_TXRV" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
	}

	@When("Member perform to enrollment for transaction reversal")
	public void member_perform_to_enrollment_for_transaction_reversal() {
	   
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TXRV_AltId" +TestUtils.getRandomValue()+ TestUtils.getRandomNumber();
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

		responseTRXM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("Member should be enroll successfully for transaction reversal with statuscode {int}")
	public void member_should_be_enroll_successfully_for_transaction_reversal_with_statuscode(Integer int1) {
	   
		Assert.assertEquals(HttpStatus.SC_OK, responseTRXM.getStatusCode());

		JsonPath jsonPathEvaluator = responseTRXM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member perform the earn transaction in the system")
	public void member_perform_the_earn_transaction_in_the_system() throws InterruptedException {

		String strTxKey = "TXRVSL";
		TrxId = strTxKey + "_" + TestUtils.getRandomAlphaNumbericValue()+TestUtils.getRandomValue();
		
		clientKey = getsourceKey();
		String TrxDate = TestUtils.getDateTime();
		_TrxDate=TrxDate;
		String price = "10";
		String quantity ="1";
		String partnerId = LoadProperties().getProperty("partnerId");
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

		System.out.println("Transaction added successfully for reversal:" + response.asString());
		Thread.sleep(5000);
	}

	@Then("Member should able to do earn transaction with status code {int}")
	public void member_should_able_to_do_earn_transaction_with_status_code(Integer statusCode) {

		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
		
		JsonPath getjsonPathEvaluator = response.jsonPath();
		TrxId=getjsonPathEvaluator.get("data.txId");
		_TrxId = TrxId; // Set value here for transaction
		
		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("The member perfom point reversal transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		TrxId = getTrxId();
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = "Test Transaction Reversal";
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");

		System.out.println("Transaction responce" + response.asString());
	}

	@Then("Member should able to do point reversal successfull with status code {int}")
	public void member_should_able_to_do_point_reversal_successfull_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
		System.out.println("Transaction added successfully:" + response.asString());

		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		String gettxId = jsonPathEvaluator.get("data.originalTxId");
		System.out.println("TxId received from Response " + gettxId);

		Assert.assertEquals(gettxId, TrxId);
	}

	@When("The member perform point reversal  for blank memberid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_reversal_for_blank_memberid_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");
		TrxId = getTrxId();
		TrxDate = getTrxDate();
		String description = "Test Transaction Reversal";
		clientKey = "";
		String debitorId=LoadProperties().getProperty("debitorId");
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perfom point reversal without member id in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_without_member_id_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");
		TrxId = getTrxId();
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = "Test Transaction Reversal";
		clientKey = "without";
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perfom point reversal without transaction id in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_without_transaction_id_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");
		TrxId = "without";
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = "Test Transaction Reversal";
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");

	}

	@When("The member perform point reversal for blank transaction id in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_reversal_for_blank_transaction_id_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		TrxId = "";
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perfom point reversal with invalid member id in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_with_invalid_member_id_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");
		TrxId = getTrxId();
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = testData.get(rownumber).get("Description");
		String clientKey = "DUMMYXABCD_0964";
		String debitorId=LoadProperties().getProperty("debitorId");
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perform point reversal with balnk date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_point_reversal_with_balnk_date_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		TrxId = getTrxId();
		TrxDate = "";
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perfom point reversal without date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_without_date_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		TrxDate = "without";
		TrxId = getTrxId();
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@Then("Member should not able to do point reversal with status code {int}")
	public void member_should_not_able_to_do_point_reversal_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
	}

	@When("The member perfom point reversal with invalid transactionid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_with_invalid_transactionid_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		TrxId = "Intx0023"+"_" + TestUtils.getRandomValue(); 
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perfom point reversal with invalid date format in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_with_invalid_date_format_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		// TrxId = testData.get(rownumber).get("TransactiontId");
		TrxId = getTrxId();
		TrxDate = "24-02-2021";
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@When("The member perfom point reversal with same transactionid and date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_with_same_transactionid_and_date_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");
		TrxId = getTrxId();
		TrxDate = getTrxDate(); //testData.get(rownumber).get("TransactionDate");
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");

	}

	@Then("Member should able to do point reversal with status code {int}")
	public void member_should_able_to_do_point_reversal_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
	}

	@When("The member perfom point reversal with diffrent transactionid date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_point_reversal_with_diffrent_transactionid_date_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxReversal");

		TrxId = getTrxId();
		TrxDate = TestUtils.GetPreviousDateTime();
		String description = testData.get(rownumber).get("Description");
		clientKey = getsourceKey();
		String debitorId=LoadProperties().getProperty("debitorId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationReversalAPIBody(TrxId, TrxDate, clientKey, description,debitorId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx/reversal");
	}

	@Then("It should gives us the success message{string}")
	public void it_should_gives_us_the_success_message(String strMsg) {

		// check success status
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		JsonPath getjsonPathEvaluator = response.jsonPath();

		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String getErrormsg = getjsonPathEvaluator.get("data.reversalTxId");
       System.out.println("The value is"+ getErrormsg);
		Assert.assertEquals(getErrormsg, strMsg.replace("txId", TrxId));

	}
	
	@Then("Also it should gives us the error message{string}")
	public void also_it_should_gives_us_the_error_message(String strMsg) {
	   
		JsonPath getjsonPathEvaluator = response.jsonPath();

		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		String getErrormsg = getjsonPathEvaluator.get("errDetails.msgTemplate");

		Assert.assertEquals(getErrormsg, strMsg);

	}
	
	@Then("This should gives us the error message{string}")
	public void this_should_gives_us_the_error_message(String strMsg) {
	  
		JsonPath getjsonPathEvaluator = response.jsonPath();

		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		
		String getErrormsg= getjsonPathEvaluator.get("error");
		
		getErrormsg=getErrormsg.replaceAll("\r\n", "");
		//String[] splitString =getErrormsg.split("\r\n");
		System.out.println("Messge in:  "+getErrormsg);
		
		Assert.assertEquals(strMsg,getErrormsg);
		
	}


	@Then("It should gives us the error message for invalid transactionid{string}")
	public void it_should_gives_us_the_error_message_for_invalid_transactionid(String strMsg) {

		JsonPath getjsonPathEvaluator = response.jsonPath();

		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		String getErrormsg = getjsonPathEvaluator.get("errDetails.msgTemplate");

		Assert.assertEquals(getErrormsg, strMsg);

	}

	@Then("It should gives us the error message for invalid member{string}")
	public void it_should_gives_us_the_error_message_for_invalid_member(String strMsg) {

		JsonPath getjsonPathEvaluator = response.jsonPath();

		boolean getStatus = getjsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);

		String getErrormsg = getjsonPathEvaluator.get("errDetails.msgTemplate");

		Assert.assertEquals(getErrormsg, strMsg);
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
