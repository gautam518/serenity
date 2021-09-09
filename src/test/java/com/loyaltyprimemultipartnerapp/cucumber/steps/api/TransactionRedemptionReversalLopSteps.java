package com.loyaltyprimemultipartnerapp.cucumber.steps.api;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
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

public class TransactionRedemptionReversalLopSteps {
	private String PointsType;
	private String TrxId;
	private String TrxDate;
	private String description;
	private Response response = null; // Response
	private Response responseTXERDSL;
	private Response responseRDSLTX;
	HttpHeaders headers;
	String strapiBody;
	String clientKey;
	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	public static String getRcptId() {return _RcptId;}
	private static String _RcptId;
	
	public static String getTrxDate() {return _TrxDate;}
	private static String _TrxDate;
	
	public static String getOldRcptId() {return _OldRcptId;}
	private static String _OldRcptId;
	
	public static String getOldTrxDate() {return _OldTrxDate;}
	private static String _OldTrxDate;

	@Given("Member wants to enrollment for redemption reversal")
	public void member_wants_to_enrollment_for_redemption_reversal() {
		clientKey = LoadProperties().getProperty("enkey") + "_TXRDRVSL" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
	}

	@When("Member performs enrollment for redemption reversal")
	public void member_performs_enrollment_for_redemption_reversal() {
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TXRDRVSL_AltId" +TestUtils.getRandomValue()+ TestUtils.getRandomNumber();
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

		responseTXERDSL = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}
	@Then("Member should be created for redemption reversal with statuscode {int}")
	public void member_should_be_created_for_redemption_reversal_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseTXERDSL.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXERDSL.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member performs transaction for redemption reversal")
	public void member_performs_transaction_for_redemption_reversal() throws InterruptedException {
		String strTxKey = "TXRDRVSL";
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

		responseRDSLTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		System.out.println("Transaction added for redemption:" + responseRDSLTX.asString());
		Thread.sleep(2000);
	}
	
	

	@Then("Transaction should be successful for redemption reversal with status code {int}")
	public void transaction_should_be_successful_for_redemption_reversal_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTXERDSL.getStatusCode());

		JsonPath jsonPathEvaluator = responseRDSLTX.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	
	@Given("Member call the points redeem")
	public void member_call_the_points_redeem() {
		clientKey = LoadProperties().getProperty("clientId");
		System.out.println("The client ky:" + clientKey);
	}
	
	@When("Member perform the point redeem in the system")
	public void member_perform_the_point_redeem_in_the_system() {

		String TrxDate = TestUtils.getDateTime();
		_TrxDate=TrxDate;
		String receiptId = "RxptId_"+ TestUtils.getRandomValue();
		
		String debitorId =LoadProperties().getProperty("debitorId");
		String amount = "10";
		description = "Test Reedem";
		clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}

	@Then("Member should be able to redeem points with status code {int}")
	public void member_should_be_able_to_redeem_points_with_status_code(Integer statuscode) {
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");

		System.out.println("clientKey received from Response " + getCleientId);

		Assert.assertEquals(clientKey, getCleientId);
		
		String receiptId=jsonPathEvaluator.get("data.receiptId.uniqueId");
		_RcptId=receiptId;

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Response " + getStatus);
		
	}
	
	
	@Given("Member call the redemption reversal")
	public void member_call_the_redemption_reversal() {
		clientKey = getsourceKey();
		System.out.println("clientKey :"+ clientKey);	
	}

	@When("The member perfom redemption reversal transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_transaction_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();//testData.get(rownumber).get("TransactiontId");
		_OldRcptId=TrxId;
		TrxDate=getTrxDate();//testData.get(rownumber).get("TransactionDate");
		
		_OldTrxDate=TrxDate;
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Redemption reversal responce" + response.asString());
		
	}

	@Then("Member should able to do redemption reversal successfull with status code {int}")
	public void member_should_able_to_do_redemption_reversal_successfull_with_status_code(Integer statuscode) {
		
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		
		System.out.println("redemption reversal Response " + getStatus);
	   
	}
	
	@When("The member perform redemption reversal duplicate redemption reversal in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_redemption_reversal_duplicate_redemption_reversal_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId =getOldRcptId(); //testData.get(rownumber).get("TransactiontId");
		TrxDate= getOldTrxDate(); //testData.get(rownumber).get("TransactionDate");
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
		
	}

	@When("The member perfom redemption reversal with invalid type in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_with_invalid_type_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();//testData.get(rownumber).get("TransactiontId");
		TrxDate=getTrxDate();//testData.get(rownumber).get("TransactionDate");
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType")+"XYZ";
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@Then("Member should not be able to do redemption reversal with status code {int}")
	public void member_should_not_be_able_to_do_redemption_reversal_with_status_code(Integer statuscode) {
		
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
		System.out.println("Tx Redemption reversal Response " + getStatus);
	    
	}

	@When("The member perfom redemption reversal with diffrent transactionid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_with_diffrent_transactionid_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	   
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = "TXRDSVL_"+ TestUtils.getRandomValue();;
		TrxDate=TestUtils.getDateTime();
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@Then("Member should not able to do redemption reversal with status code {int}")
	public void member_should_not_able_to_do_redemption_reversal_with_status_code(Integer statuscode) {
	   
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
		System.out.println("Tx Redemption Response " + getStatus);
	}

	@When("The member perform redemption reversal for blank transactionid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_redemption_reversal_for_blank_transactionid_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = "";
		TrxDate=getTrxDate();    //testData.get(rownumber).get("TransactionDate");
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@When("The member perfom redemption reversal with invalid transactionid in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_with_invalid_transactionid_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	   
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = "TXABCDF_09876_xyz";
		TrxDate=getTrxDate();     //testData.get(rownumber).get("TransactionDate");
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@Then("Member should able to do redemption reversal with status code {int}")
	public void member_should_able_to_do_redemption_reversal_with_status_code(Integer statuscode) {
	    
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Tx Redemption Response " + getStatus);
	}
	
	@When("The member perform redemption reversal with blank date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perform_redemption_reversal_with_blank_date_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	    
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();
		TrxDate="";
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@When("The member perfom redemption reversal without date in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_without_date_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();
		TrxDate="without";
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}
	
	@When("The member perfom redemption reversal with invalid date format in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_with_invalid_date_format_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	  
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();
		TrxDate="252-02-2021";
		description="Auto Redemption Reversal";
		PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@When("The member perfom redemption reversal without point type in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_without_point_type_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws OpenXML4JException, IOException {
	   
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();
		TrxDate=getTrxDate();
		description="Auto Redemption Reversal";
		PointsType = "";
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
	}

	@When("The member perfom redemption reversal with different point type in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_redemption_reversal_with_different_point_type_in_the_system_with_details_MembereTrx_and_rownumber(Integer rownumber) throws InvalidFormatException, IOException {
	 
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"TxRedReversal");

		TrxId = getRcptId();
		TrxDate=getTrxDate();
		description="Auto Redemption Reversal";
		PointsType = "InvalidPointsType";
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
		
		System.out.println("Transaction responce" + response.asString());
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
