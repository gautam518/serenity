package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

public class MemberMergeSteps {
	String strapiBody;
	private String sourceKey;// source key
	private String targetKey; // target key
	private String anotherKey;//new member

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

	public static Float getSoucreBalanceBeforMerge() {
		return _getSourcebalancebeforemerge;
	}

	private static Float _getSourcebalancebeforemerge;

	public static Float getSoucreRedeemBalanceBeforMerge() {
		return _getSourceRedeembalancebeforemerge;
	}

	private static Float _getSourceRedeembalancebeforemerge;

	public static Float getTargetBalanceBeforMerge() {
		return _getTargetbalancebeforemerge;
	}

	private static Float _getTargetbalancebeforemerge;

	public static Float getTargetRedeemBalanceBeforMerge() {
		return _getTargetRedeembalancebeforemerge;
	}

	private static Float _getTargetRedeembalancebeforemerge;

	public static String getSourcemembercardbeformerge() {
		return _sourcemembercardbeformerge;
	}

	private static String _sourcemembercardbeformerge;

	public static Integer getSourcetransCount() {
		return _sourcetransCount;
	}

	private static Integer _sourcetransCount;

	public static Integer getSourceredeemCount() {
		return _sourceredeemCount;
	}

	private static Integer _sourceredeemCount;

	public static Integer getTargettransCount() {
		return _targettransCount;
	}

	private static Integer _targettransCount;

	public static Integer getTargetredeemCount() {
		return _targetredeemCount;
	}

	private static Integer _targetredeemCount;
	
	public static String getsourceVoucher() {
		return _sourceVoucher;
	}

	private static String _sourceVoucher;

	public static String getsourceVoucherToken() {
		return _sourceVoucherToken;
	}

	private static String _sourceVoucherToken;

	public static String getsourceAlternateIds() {
		return _sourceAlternateIds;
	}

	private static String _sourceAlternateIds;

	public static String getsourceVoucherName() {
		return _sourceVoucherName;
	}

	private static String _sourceVoucherName;

	public static Integer gettotaleventcount() {
		return _totaleventcount;
	}

	private static Integer _totaleventcount;

	Response responseSM;// for source member
	Response responseTM;// for source member
	Response responseOTHM;// for new member
	Response responseCD;// for source member
	Response responseLE;// for source member
	Response responseLER;// for source member
	Response responseTX;// for source member
	Response responseRD;// for source member
	Response responseADJ;// for source member
	Response responseDPB_SRC;// depostit statement
	Response responseRDB_SRC;// for redeem balance
	Response responseDPB;// depostit statement
	Response responseRDB;// for redeem balance
	Response responseVCissue;
	Response responseVCTpe;
	Response responseMGE;
	Response responseTXCD;
	Response responseTMBLNAFTMEGE;
	Response responseTMREDEMBLNAFTMEGE;
	Response responseSRCGXTH;
	Response responseSRCGREDEMH;
	Response responseTAGXTH;
	Response responseTAGREDEMH;
	Response responseSRCAFTXTH;
	Response responseSRCAFTXREDEMH;
	Response responseSRCAFTXDSTM;
	Response responseTRGTVCH;
	Response responseSRCVCHAFTMERG;
	Response responseTGTALTID;
	Response responseTGTCARD;
	Response responseTargVCConvert;
	Response responseTAGEVENTHTY;
	Response responseSRCEVETHST;
	Response responseTRGALTHY;
	Response responseTRGREMARKS;
	Response responseSRCVTOKEN;
	Response responseSRCVoucherCissue;
	Response responseSRCREDREV;
	Response responseSRCMEMBSHIP;
	Response responseSRCAFTERRD;
	Response responseSRCAFTERTX;
	Response responseSRCAFTERADJ;
	Response responseSRCAFTERTXREV;
	Response response_TARGEVT;
	Response responseSMBMERGE;
	Response responseTPT;

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

	@Given("Source Member wants to enrollment on PCV{int}")
	public void source_Member_wants_to_enrollment_on_PCV(Integer int1) {
		sourceKey = LoadProperties().getProperty("enkey") + "_MEGESRC" + TestUtils.getRandomValue();
		_sourceKey = sourceKey;
	}

	@When("Source Member performs enrollment on PCV{int}")
	public void source_Member_performs_enrollment_on_PCV(Integer int1) {
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MEGE_AltId" + TestUtils.getRandomValue();
		_sourceAlternateIds = strAlternateIds;
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

	@Then("Source member should be created in pcv{int} with statuscode {int}")
	public void source_member_should_be_created_in_pcv_with_statuscode(Integer int1, Integer int2)
			throws InterruptedException {

		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Then("The source member key should be equal to input key value")
	public void the_source_member_key_should_be_equal_to_input_key_value() {

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		String getSrckey = jsonPathEvaluator.get("data.key");
		System.out.println("The value of key is:" + getSrckey);

		Assert.assertEquals(getSrckey, sourceKey);
	}

	@Given("Target Member wants to enrollment on PCV{int}")
	public void target_Member_wants_to_enrollment_on_PCV(Integer int1) {
		targetKey = LoadProperties().getProperty("enkey") + "_MEGDEST" + TestUtils.getRandomValue();
		_targetKey = targetKey; // save this member to make it target
	}

	@When("Target Member performs enrollment on PCV{int}")
	public void target_Member_performs_enrollment_on_PCV(Integer int1) {

		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MEGDEST_AltId" + TestUtils.getRandomValue();
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

	@Then("Target member should be created in pcv{int} with statuscode {int}")
	public void target_member_should_be_created_in_pcv_with_statuscode(Integer int1, Integer int2) {

		Assert.assertEquals(HttpStatus.SC_OK, responseTM.getStatusCode());
		System.out.println("target member is Added successfully:" + responseTM.asString());

		JsonPath jsonPathEvaluator = responseTM.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

	}

	@Then("The target member key should be equal to input key value")
	public void the_target_member_key_should_be_equal_to_input_key_value() {

		JsonPath jsonPathEvaluator = responseTM.jsonPath();
		String getTgkey = jsonPathEvaluator.get("data.key");
		Assert.assertEquals(getTgkey, targetKey);
	}

	@When("Source member perform create card")
	public void source_member_perform_create_card() {
		String strdescription = LoadProperties().getProperty("description");
		String strname = LoadProperties().getProperty("name");
		String strstatus = LoadProperties().getProperty("status");
		String strvalidSince = LoadProperties().getProperty("validSince");
		String strvalidUntil = LoadProperties().getProperty("validUntil");
		String strKey = getsourceKey();
		String strSourceCard = getsourceKey() + "_Card1";
		_sourcemembercardbeformerge = strSourceCard;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AddMemberCard(strdescription, strSourceCard, strKey, strname, strstatus, strvalidSince,
				strvalidUntil);

		responseCD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().put("/api/v10/memberships/" + strKey + "/legalentity/card");
	}

	@Then("Create card for source member should be successful with statuscode {int}")
	public void create_card_for_source_member_should_be_successful_with_statuscode(Integer int1) {

		Assert.assertEquals(HttpStatus.SC_OK, responseCD.getStatusCode());

		JsonPath jsonPathEvaluator = responseCD.jsonPath();

		String strStatus = jsonPathEvaluator.get("data.status");
		Assert.assertEquals("ACTIVE", strStatus);

		String key = jsonPathEvaluator.get("data.key");
		Assert.assertEquals(key, sourceKey + "_Card1");

	}
	
	@When("Source member perform create legal entity")
	public void source_member_perform_create_legal_entity() {
		String strUserId = getsourceKey() + "_LE";
		_userIdLe = strUserId;
		String strPrefix = LoadProperties().getProperty("prefix");
		String straddress1 = LoadProperties().getProperty("address1");
		String strFirstname = LoadProperties().getProperty("firstname");
		String strLastname = LoadProperties().getProperty("lastname");
		String strType = LoadProperties().getProperty("membertype");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AddLegalEntity(strUserId, strPrefix, strFirstname, strLastname, straddress1);

		responseLE = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().put("/api/v10/memberships/type/" + strType + "/legalEntity");
	}
	@Then("Legal entity for source member should be successful with statuscode {int}")
	public void legal_entity_for_source_member_should_be_successful_with_statuscode(Integer int1) {
	   
		Assert.assertEquals(HttpStatus.SC_OK, responseLE.getStatusCode());

		JsonPath jsonPathEvaluator = responseLE.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

		String userid = jsonPathEvaluator.get("data.userId");
		Assert.assertEquals(userid, _userIdLe);
	}

	@When("Source member perform create card with legal entity relation")
	public void source_member_perform_create_card_with_legal_entity_relation() {
		String strdescription = LoadProperties().getProperty("description");
		String strname = LoadProperties().getProperty("name");
		String strstatus = LoadProperties().getProperty("status");
		String strvalidSince = LoadProperties().getProperty("validSince");
		String strvalidUntil = LoadProperties().getProperty("validUntil");
		String strKey = getsourceKey();
		String strmembershipKey = getsourceKey();
		String strlegalEntityKey = getsourceKey() + "_LE";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AddLegalEntityRelation(strdescription, strKey + "_Card2", strmembershipKey,
				strlegalEntityKey, strname, strstatus, strvalidSince, strvalidUntil);
		responseLER = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().put("/api/v10/memberships/" + strKey + "/legalentity/card");
	}

	@Then("Create card and Legal entity relation for source member should be successful with statuscode {int}")
	public void create_card_and_Legal_entity_relation_for_source_member_should_be_successful_with_statuscode(
			Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, responseLER.getStatusCode());

		JsonPath jsonPathEvaluator = responseLER.jsonPath();

		String strStatus = jsonPathEvaluator.get("data.status");
		Assert.assertEquals("ACTIVE", strStatus);

		String key = jsonPathEvaluator.get("data.key");
		Assert.assertEquals(key, sourceKey + "_Card2");
	}

	@Given("Member performs transaction in PCV{int} as Source Member")
	public void member_performs_transaction_in_PCV_as_Source_Member(Integer int1) {
		sourceKey = getsourceKey();
		System.out.println("source member id:" + sourceKey);
	}

	@When("Source member call the earn transaction")
	public void source_member_call_the_earn_transaction() throws InterruptedException {

		String TrxId = "MEMGSRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.getDateTime();
		_trxDate = TrxDate; // Set value here for transaction
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

		String clientKey = getsourceKey();
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, price, quantity, productKey, channelkey,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
		Thread.sleep(10000);
	}

	@Then("Point earn for the source member should be successful with status code {int}")
	public void point_earn_for_the_source_member_should_be_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Source member perform point redemption")
	public void source_member_perform_point_redemption() {
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

	@Then("Point redemption should be successful for the source member with status code {int}")
	public void point_redemption_should_be_successful_for_the_source_member_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseRD.getStatusCode());

		JsonPath jsonPathEval = responseRD.jsonPath();
		String getCleientId = jsonPathEval.get("data.membershipKey");
		Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("Source member perform redemption reversal")
	public void source_member_perform_redemption_reversal() {
		sourceKey = getsourceKey();
		System.out.println("Get value of txvalue"+ getReceiptId());
		String TrxId = getReceiptId();
		String TrxDate = getRedTXDate();// get date value from last
		String description = "Redemption reversal test";
		String PointsType = "BONUS";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		responseSRCREDREV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/redeem/reversal");

	}

	@Then("Redemption reversal for the source member is successful with status code {int}")
	public void redemption_reversal_for_the_source_member_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseSRCREDREV.getStatusCode());

		sourceKey = getsourceKey();
		JsonPath jsonPathEvaluator = responseSRCREDREV.jsonPath();
		String getCleientId = jsonPathEvaluator.get("data.membershipKey");
		Assert.assertEquals(sourceKey, getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("Source member perform point adjustment")
	public void source_member_perform_point_adjustment() {
		String receiptId = "TextRec"+ TestUtils.getRandomValue();
		String date = TestUtils.getDateTime();
		String amount = "10";
		String debitorId = getDebitorId();
		String description = "Test Adjustment";
		String clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount, debitorId, description);

		responseADJ = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

	}

	@Then("Point adjustment for the source member is successful with status code {int}")
	public void point_adjustment_for_the_source_member_is_successful_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseADJ.getStatusCode());

		JsonPath jsonPathEvaluator = responseADJ.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Given("Source member wants to check the earn and redeem balance")
	public void source_member_wants_to_check_the_earn_and_redeem_balance() {

		sourceKey = getsourceKey();
	}

	@When("Source member call the earn and redeem balance")
	public void source_member_call_the_earn_and_redeem_balance() {

		// Deposit statement
		responseDPB_SRC = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + sourceKey + "/statement?type=BONUS");

		// For redeem balance
		responseRDB_SRC = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + sourceKey + "/redeemable?type=bonus");
	}

	@Then("Earn and redeem balance of the source member should be displayed with status code<status_code>")
	public void earn_and_redeem_balance_of_the_source_member_should_be_displayed_with_status_code_status_code() {

		Assert.assertEquals(HttpStatus.SC_OK, responseDPB_SRC.getStatusCode());
		JsonPath jsonPathEvaluator = responseDPB_SRC.jsonPath();
		Float balance_before_merge_src = jsonPathEvaluator.get("data.balance");
		_getSourcebalancebeforemerge = balance_before_merge_src;
		// Assert.assertEquals(getsourceKey(), getCleientId);
		System.out.println("_getSourcebalancebeforemerge"+_getSourcebalancebeforemerge);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		// For redeem
		Assert.assertEquals(HttpStatus.SC_OK, responseRDB_SRC.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseRDB_SRC.jsonPath();
		Float redeem_balance_before_merge_src = jsonPathEvaluator1.get("data.balance");
		_getSourceRedeembalancebeforemerge = redeem_balance_before_merge_src;
		// Assert.assertEquals(getsourceKey(), getCleientId);
		System.out.println("_getSourceRedeembalancebeforemerge"+_getSourceRedeembalancebeforemerge);

		boolean getstatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getstatus, true);

	}

	@When("Target member call the earn and redeem balance")
	public void target_member_call_the_earn_and_redeem_balance() {

		targetKey = gettargetKey();
		// Deposit statement
		responseDPB = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + targetKey + "/statement?type=BONUS");

		// For redeem balance
		responseRDB = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + targetKey + "/redeemable?type=bonus");
	}

	@Then("Earn and redeem balance of the target member should be displayed with status code<status_code>")
	public void earn_and_redeem_balance_of_the_target_member_should_be_displayed_with_status_code_status_code() {
		Assert.assertEquals(HttpStatus.SC_OK, responseDPB.getStatusCode());
		JsonPath jsonPathEvaluator = responseDPB.jsonPath();
		Float target_balance_before_merge_src = jsonPathEvaluator.get("data.balance");
		_getTargetbalancebeforemerge = target_balance_before_merge_src;
		// Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		// For redeem
		Assert.assertEquals(HttpStatus.SC_OK, responseRDB.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseRDB.jsonPath();
		Float target_redeem_balance_before_merge_src = jsonPathEvaluator1.get("data.balance");
		_getTargetRedeembalancebeforemerge = target_redeem_balance_before_merge_src;
		// Assert.assertEquals(getsourceKey(), getCleientId);

		boolean getstatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getstatus, true);
	}
	
	@When("Source member perform generate a voucher token")
	public void source_member_perform_generate_a_voucher_token() {
	    
		sourceKey = getsourceKey();
		String strdescription = LoadProperties().getProperty("description");
		String strvcOf = LoadProperties().getProperty("VcTypeof");
		String strvalue = LoadProperties().getProperty("value");
		String strname = sourceKey + "InMerge";

		_sourceVoucherName = strname;

		strapiBody = null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.CreateVoucherType(strname, strdescription, strvcOf, strvalue);

		responseVCTpe = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/voucher/type");
		/// Generate vocuhers
		responseSRCVTOKEN = SerenityRest.given().urlEncodingEnabled(false).queryParam("quantity", 10)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + strname + "/generate");
	}

	@Then("Source member should be able to generate token successfully  with status code {int}")
	public void source_member_should_be_able_to_generate_token_successfully_with_status_code(Integer code) {

		Assert.assertEquals(HttpStatus.SC_OK, responseSRCVTOKEN.getStatusCode());

		JsonPath getjsonPathVal = responseSRCVTOKEN.jsonPath();

		Boolean strSuccess = getjsonPathVal.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Source member perform voucher issue")
	public void source_member_perform_voucher_issue() {
		sourceKey = getsourceKey();
		String strdescription = LoadProperties().getProperty("description");
		String strvcOf = LoadProperties().getProperty("VcTypeof");
		String strvalue = LoadProperties().getProperty("value");
		String name = sourceKey + "Merge";

		_sourceVoucher = name;

		strapiBody = null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.CreateVoucherType(name, strdescription, strvcOf, strvalue);

		responseVCTpe = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/voucher/type");

		/// Generate vocuhers
		Response responseVCGNe = SerenityRest.given().urlEncodingEnabled(false).queryParam("quantity", 10)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + name + "/generate");

		/// voucher/{type}/issue
		responseVCissue = SerenityRest.given().urlEncodingEnabled(false).queryParam("recipient", getsourceKey())
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + name + "/issue");
	}

	@Then("Voucher should be assign to source member successfully with status cod<status_code>")
	public void voucher_should_be_assign_to_source_member_successfully_with_status_cod_status_code() {

		Assert.assertEquals(HttpStatus.SC_OK, responseVCTpe.getStatusCode());

		Assert.assertEquals(HttpStatus.SC_OK, responseVCissue.getStatusCode());

		JsonPath getjsonPathVal = responseVCissue.jsonPath();

		Boolean strSuccess = getjsonPathVal.get("success");
		Assert.assertEquals(true, strSuccess);

		// get token value store here
		String strSrcVCToken = getjsonPathVal.get("data.token");
		System.out.println("source Token:" + strSrcVCToken);
		_sourceVoucherToken = strSrcVCToken;
	}

	@Given("Target member wants to convert the issued voucher of the source member")
	public void target_member_wants_to_convert_the_issued_voucher_of_the_source_member() {
		targetKey = gettargetKey();
		String getsourceToken = getsourceVoucherToken();
		System.out.println("Chek toke value:" + getsourceToken);
		System.out.println("Target Key: " + targetKey);
	}

	@When("Target member perform the convert voucher")
	public void target_member_perform_the_convert_voucher() {

		targetKey = gettargetKey();
		String getsourceToken = getsourceVoucherToken();
		// code the source token here with target member
		responseTargVCConvert = SerenityRest.given().urlEncodingEnabled(false).queryParam("client", targetKey)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + getsourceToken + "/convert");
	}

	@Then("Target member should be able to convert the voucher successful with status code {int}")
	public void target_member_should_be_able_to_convert_the_voucher_successful_with_status_code(Integer int1) {

		Assert.assertEquals(HttpStatus.SC_OK, responseTargVCConvert.getStatusCode());

		JsonPath jsonPathEvaluator = responseTargVCConvert.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

		// get token value store here
		String strState = jsonPathEvaluator.get("data.state");
		Assert.assertEquals(strState, "Converted");
	}

	@Given("Source member wants to merge with the target member")
	public void source_member_wants_to_merge_with_the_target_member() {
		sourceKey = getsourceKey();
		System.out.println("Get the source member id:" + sourceKey);
		targetKey = gettargetKey();

		System.out.println("Get the target member id:" + targetKey);
	}

	@When("Source member perform the member merge with the target member")
	public void source_member_perform_the_member_merge_with_the_target_member() {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.MemberMergeAPIBody(sourceKey, targetKey);

		responseMGE = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships/merge");
	}

	@Then("Member merge should be successful with status code <statuscode>")
	public void member_merge_should_be_successful_with_status_code_statuscode() {

		Assert.assertEquals(HttpStatus.SC_OK, responseMGE.getStatusCode());
		System.out.println("Member Merge successfully:" + responseMGE.asString());

		JsonPath jsonPathEvaluator = responseMGE.jsonPath();

		String SourceId = jsonPathEvaluator.get("data.source.key");
		Assert.assertEquals(sourceKey, SourceId);

		// Check source status after merge
		String getSourceStatus = jsonPathEvaluator.get("data.source.contractStatus");
		Assert.assertEquals(getSourceStatus, "CANCELLED");

		String getTargetId = jsonPathEvaluator.get("data.target.key");
		Assert.assertEquals(targetKey, getTargetId);

		// Check target status after merge
		String getTargetStatus = jsonPathEvaluator.get("data.target.contractStatus");
		Assert.assertEquals(getTargetStatus, "ACTIVE");
	}

	@Given("Source member wants to earn points by doing transaction with source card")
	public void source_member_wants_to_earn_points_by_doing_transaction_with_source_card() {

		sourceKey = getsourceKey();
		String getSourceCard = getSourcemembercardbeformerge();
		System.out.println("Card no befor merge: " + getSourceCard);
	}

	@When("Source member perform earn transaction with the source card")
	public void source_member_perform_earn_transaction_with_the_source_card() {

		String TrxId = "SRC_TXWithCard" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.getDateTime();
		String price = "10";
		String quantity ="1";
		String channelkey = "";
		String productKey = "";
		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");
		String _getChannelKey = jsonPathEval.get("data[0].channelKey");
		String partnerId=LoadProperties().getProperty("partnerId");

		channelkey = _getChannelKey;
		productKey = _getProductKey;
		String clientKey = getSourcemembercardbeformerge();// pass the source cafd number here

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);

		responseTXCD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Earn transaction should not be successful for the source member with status code {int}")
	public void earn_transaction_should_not_be_successful_for_the_source_member_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTXCD.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXCD.jsonPath();
		/*Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);*/
		

	}

	@Given("Target member wants to check balance")
	public void target_member_wants_to_check_balance() {
		targetKey = gettargetKey();
		System.out.println("The source mameber balance:" + targetKey);

	}

	@When("Target member perform the check balance")
	public void target_member_perform_the_check_balance() {
		targetKey = gettargetKey();
		// Deposit statement
		responseTMBLNAFTMEGE = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + targetKey + "/statement?type=BONUS");
	}

	@Then("The target member balance should be equal to the sum of balance of both source and target member")
	public void the_target_member_balance_should_be_equal_to_the_sum_of_balance_of_both_source_and_target_member() {

		Assert.assertEquals(HttpStatus.SC_OK, responseTMBLNAFTMEGE.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseTMBLNAFTMEGE.jsonPath();

		Float target_member_balance_after_merge = jsonPathEvaluator1.get("data.balance");
		Float TotalTargetMembeBalnce = getSoucreBalanceBeforMerge() + getTargetBalanceBeforMerge();

		Assert.assertEquals(TotalTargetMembeBalnce, target_member_balance_after_merge); // check balance equal
		
		//Assert.assertEquals("302.0", target_member_balance_after_merge); // check balance equal
		//Float Val=(float) 302.0;
		//Assert.assertEquals(Val, target_member_balance_after_merge); // check balance
		
		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Given("Target member wants to check redeemable balance")
	public void target_member_wants_to_check_redeemable_balance() {

		targetKey = gettargetKey();
		System.out.println("The member balnce:" + targetKey);

	}

	@When("Target member perform to check redeemable balance")
	public void target_member_perform_to_check_redeemable_balance() {

		targetKey = gettargetKey();
		// For redeem balance
		responseTMREDEMBLNAFTMEGE = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log()
				.all().when().get("/api/v10/deposits/" + targetKey + "/redeemable?type=bonus");
	}

	@Then("The target member redeemable balance should be equal to the sum of redeemable balance of both source and target member")
	public void the_target_member_redeemable_balance_should_be_equal_to_the_sum_of_redeemable_balance_of_both_source_and_target_member() {

		Assert.assertEquals(HttpStatus.SC_OK, responseTMREDEMBLNAFTMEGE.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseTMREDEMBLNAFTMEGE.jsonPath();

		Float target_member_Redeem_balance_after_merge = jsonPathEvaluator1.get("data.balance");

		Float TotalTargetMembeRedeemBalnce = getSoucreRedeemBalanceBeforMerge() + getTargetRedeemBalanceBeforMerge();

		Assert.assertEquals(TotalTargetMembeRedeemBalnce, target_member_Redeem_balance_after_merge); // check balance
		
		//Float Val=(float) 282.0;
		//Assert.assertEquals(Val, target_member_Redeem_balance_after_merge); // check balance
		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("Source member call the transaction and redeem history")
	public void source_member_call_the_transaction_and_redeem_history() {
		sourceKey = getsourceKey();
		responseSRCGXTH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/tx");

		responseSRCGREDEMH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/tx/rewardsHistory");
	}

	@When("Target member call the transaction and redeem history")
	public void target_member_call_the_transaction_and_redeem_history() {

		targetKey = gettargetKey();
		responseTAGXTH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + targetKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/tx");

		responseTAGREDEMH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + targetKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/tx/rewardsHistory");

	}

	@Then("Source member transaction and redeem history should be displayed with status code {int}")
	public void source_member_transaction_and_redeem_history_should_be_displayed_with_status_code(Integer int1) {

		JsonPath jsonPathValidator = responseSRCGXTH.jsonPath();
		Integer sourcetxhistorycount = jsonPathValidator.get("total");
		_sourcetransCount = sourcetxhistorycount;
		
		JsonPath jsonPathValidator1 = responseSRCGREDEMH.jsonPath();
		Integer sourceredeemcount = jsonPathValidator1.get("total");
		_sourceredeemCount = sourceredeemcount;

	}

	@Then("Target member transaction and redeem history should be displayed with status code {int}")
	public void target_member_transaction_and_redeem_history_should_be_displayed_with_status_code(Integer int1) {

		JsonPath jsonPathValidator = responseTAGXTH.jsonPath();
		Integer targettxhistorycount = jsonPathValidator.get("total");
		_targettransCount = targettxhistorycount;
		JsonPath jsonPathValidator1 = responseTAGREDEMH.jsonPath();
		Integer targetredeemcount = jsonPathValidator1.get("total");
		_targetredeemCount = targetredeemcount;
	}

	@Then("Target member total transactions history count should be equal to the sum of the source and target member after merge")
	public void target_member_total_transactions_history_count_should_be_equal_to_the_sum_of_the_source_and_target_member_after_merge() {

		Integer TotalTxtCount = getTargettransCount() + getSourcetransCount();
		Integer GetTotalAfter = MemberMergeIntegrationTransactionSteps.getAfterMergeTargettransCount();
		Assert.assertEquals(TotalTxtCount, GetTotalAfter);
		//Integer Val=4;
		//Assert.assertEquals(Val, GetTotalAfter);
	}

	@Then("Target member total redemption history count should be equal to the sum of the source and target member after merge")
	public void target_member_total_redemption_history_count_should_be_equal_to_the_sum_of_the_source_and_target_member_after_merge() {
		Integer TotalRedeemCount = getSourceredeemCount() + getTargetredeemCount();
		Integer GetTotalRedemAfter = MemberMergeIntegrationTransactionSteps.getAfterMergeTargetredeemCount();
		Assert.assertEquals(TotalRedeemCount, GetTotalRedemAfter);
		//Integer Val=4;
		//Assert.assertEquals(Val, GetTotalRedemAfter);
	}

	@Given("Source member wants to see transaction history after merge")
	public void source_member_wants_to_see_transaction_history_after_merge() {
		sourceKey = getsourceKey();
	}

	@When("Source member perform transaction history after merging")
	public void source_member_perform_transaction_history_after_merging() {

		sourceKey = getsourceKey();
		responseSRCAFTXTH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + targetKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/tx");
	}

	@Then("Source member should be able to see the transaction history successfully with status code {int}")
	public void source_member_should_be_able_to_see_the_transaction_history_successfully_with_status_code(
			Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, responseSRCAFTXTH.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseSRCAFTXTH.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Given("Source member wants to see reward history after merge")
	public void source_member_wants_to_see_reward_history_after_merge() {
		sourceKey = getsourceKey();
	}

	@When("Source member perform reward  history after merging")
	public void source_member_perform_reward_history_after_merging() {

		sourceKey = getsourceKey();
		responseSRCAFTXREDEMH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/tx/rewardsHistory");
	}

	@Then("Source member should be able to see the reward history successfully with status code {int}")
	public void source_member_should_be_able_to_see_the_reward_history_successfully_with_status_code(Integer int1) {

		Assert.assertEquals(HttpStatus.SC_OK, responseSRCAFTXREDEMH.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseSRCAFTXREDEMH.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Given("Source member wants to see deposit statement after merge")
	public void source_member_wants_to_see_deposit_statement_after_merge() {
		sourceKey = getsourceKey();
	}

	@When("Source member perform deposit statement after merging")
	public void source_member_perform_deposit_statement_after_merging() {

		sourceKey = getsourceKey();
		responseSRCAFTXDSTM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/deposits/" + sourceKey + "/statement?type=BONUS");
	}

	@Then("Source member should not be able to see the deposit statement with status code {int}")
	public void source_member_should_not_be_able_to_see_the_deposit_statement_with_status_code(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), responseSRCAFTXDSTM.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseSRCAFTXDSTM.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, false);
	}

	@Given("Target member wants to see the vouchers")
	public void target_member_wants_to_see_the_vouchers() {
		targetKey = gettargetKey();
	}

	@When("Target member perform voucher history after merge")
	public void target_member_perform_voucher_history_after_merge() {
		targetKey = gettargetKey();

		responseTRGTVCH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"issuedTo\", \"value\": \"" + targetKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/voucher");
	}

	@Then("Target member should get all the vouchers history of the source member after merging")
	public void target_member_should_get_all_the_vouchers_history_of_the_source_member_after_merging() {

		Assert.assertEquals(HttpStatus.SC_OK, responseTRGTVCH.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseTRGTVCH.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Then("Target member vouchers should have the same status as they have with the source member befor merging")
	public void target_member_vouchers_should_have_the_same_status_as_they_have_with_the_source_member_befor_merging()
			throws InterruptedException {

		JsonPath jsonPathEvaluator1 = responseTRGTVCH.jsonPath();

		// match for the type start here*/
		String getTargetVCType = null;// match the issuedTo
		String getSourceVCType = getsourceVoucher();
		List<String> vcTypeIds = jsonPathEvaluator1.get("data.type");
		getTargetVCType = vcTypeIds.get(0);
		Assert.assertEquals(getTargetVCType, getSourceVCType);
		// match for the type end here*/

		// match for the issuedTo start here*/
		String getissuedTo = null;
		String IssueToVC = gettargetKey();
		List<String> vcIssueIds = jsonPathEvaluator1.getList("data.issuedTo");
		getissuedTo = vcIssueIds.get(0);
		Assert.assertEquals(getissuedTo, IssueToVC);
		// match for the issuedTo end here*/

		// match for the revokedFrom start here*/
		String strrevokedFrom = null;
		String strgetIssueToVC = getsourceKey();
		List<String> lstIssueToVcs = jsonPathEvaluator1.getList("data.revokedFrom");
		strrevokedFrom = lstIssueToVcs.get(0);
		Assert.assertEquals(strrevokedFrom, strgetIssueToVC);
		// match for the revokedFrom end here*/

		// match for the revocationReason start here*/
		String stReasonTgVc = null;
		String strReason = "Membership merge";
		List<String> lstTgReasonVc = jsonPathEvaluator1.getList("data.revocationReason");
		stReasonTgVc = lstTgReasonVc.get(0);
		Assert.assertEquals(stReasonTgVc, strReason);
		// match for the revocationReason end here*/
	}

	@Given("Source member wants to see his voucher history after merge")
	public void source_member_wants_to_see_his_voucher_history_after_merge() {
		sourceKey = getsourceKey();
	}

	@When("Source member perform voucher history after merge")
	public void source_member_perform_voucher_history_after_merge() {

		sourceKey = getsourceKey();
		responseSRCVCHAFTMERG = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"revokedFrom\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/voucher");
	}

	@Then("Source member should be able to see his vouchers history after merge with status code {int}")
	public void source_member_should_be_able_to_see_his_vouchers_history_after_merge_with_status_code(
			Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), responseSRCVCHAFTMERG.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseSRCVCHAFTMERG.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Given("Target member wants to check cards and legal Entity information")
	public void target_member_wants_to_check_cards_and_legal_Entity_information() {
		targetKey = gettargetKey();
	}

	@When("Target member perform the cards history")
	public void target_member_perform_the_cards_history() {

		targetKey = gettargetKey();
		responseTGTCARD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + targetKey + "/cards");
	}

	@When("Target member perform the Legal Entity history")
	public void target_member_perform_the_Legal_Entity_history() {
		targetKey = gettargetKey();
		responseTGTALTID = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("api/v10/memberships/" + targetKey + "/details");
	}

	@Then("Target member should have all the source member cards in detail with status code {int}")
	public void target_member_should_have_all_the_source_member_cards_in_detail_with_status_code(Integer statuscode)
			throws ParseException, InterruptedException {

		Assert.assertEquals(statuscode.intValue(), responseTGTCARD.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseTGTCARD.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);

		String strMemberkey = null;
		targetKey = gettargetKey();
		// Need to put the integration code here as facing some issue in nested json
		// extraction.
		List<String> getmembershipIds = jsonPathEvaluator1.getList("data.membershipKey");
		System.out.println("The target card history membershipKey:" + getmembershipIds);
		for (String val : getmembershipIds) {
			if (val.equals(targetKey)) {// here check the field contains or not
				strMemberkey = val;
				break;
			}
		}
		Assert.assertEquals(targetKey, strMemberkey);
		Thread.sleep(1000);
	}

	@Then("Target member should have all the source member legal entity in detail with status code {int}")
	public void target_member_should_have_all_the_source_member_legal_entity_in_detail_with_status_code(
			Integer statuscode) {
		Assert.assertEquals(statuscode.intValue(), responseTGTALTID.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseTGTALTID.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@Given("Target member wants to check the event merge")
	public void target_member_wants_to_check_the_event_merge() {
		targetKey = gettargetKey();
	}

	@When("Target member perform event history")
	public void target_member_perform_event_history() {
		targetKey = gettargetKey();
		responseTAGEVENTHTY = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"membershipKey\", \"value\": \"" + targetKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/loyaltyops/events/list");

	}

	@Then("Target member should have all the events history with status code {int}")
	public void target_member_should_have_all_the_events_history_with_status_code(Integer statuscode)
			throws InterruptedException {

		Assert.assertEquals(statuscode.intValue(), responseTAGEVENTHTY.getStatusCode());

		JsonPath jsonPtEval = responseTAGEVENTHTY.jsonPath();
		targetKey = gettargetKey();
		boolean getstatus = jsonPtEval.get("success");
		Assert.assertEquals(getstatus, true);

		String str_data1 = null;
		String str_data2 = null;

		List<String> trnsIds = jsonPtEval.getList("data.transferredTo");
		System.out.println("The events history transferredTo: " + trnsIds);
		for (String id : trnsIds) {
			if (id.equals(targetKey)) {// here check the field contais or not
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
		Assert.assertEquals(targetKey, str_data1);
		Assert.assertEquals("Membership merge", str_data2);

	}

	@Given("Source member wants to check his event history")
	public void source_member_wants_to_check_his_event_history() {

		sourceKey = getsourceKey();
		System.out.println("The value of source: " + sourceKey);
	}

	@When("Source member perform event history")
	public void source_member_perform_event_history() {

		sourceKey = getsourceKey();
		responseSRCEVETHST = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"membershipKey\", \"value\": \"" + sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/loyaltyops/events/list");
	}

	@Then("Source member should be able to see all his event history after merge with status code {int}")
	public void source_member_should_be_able_to_see_all_his_event_history_after_merge_with_status_code(
			Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), responseSRCEVETHST.getStatusCode());

		JsonPath jsonPtEval = responseSRCEVETHST.jsonPath();
		targetKey = gettargetKey();
		sourceKey = getsourceKey();
		boolean getstatus = jsonPtEval.get("success");
		Assert.assertEquals(getstatus, true);

		String str_data1 = null;
		String str_data2 = null;
		String strMemberkey = null;

		Integer totalevent = jsonPtEval.getList("data").size();
		_totaleventcount = totalevent;
		System.out.println("Total source event count:" + totalevent);

		List<String> trnsIds = jsonPtEval.getList("data.transferredTo");
		System.out.println("The source events history transferredTo: " + trnsIds);
		for (String id : trnsIds) {
			if (id.equals(targetKey)) {// here check the field contains or not
				str_data1 = id;
				break;
			}
		}
		List<String> reasonsIds = jsonPtEval.getList("data.transferReason");
		System.out.println("The source events history transferReason:" + reasonsIds);
		for (String val : reasonsIds) {
			if (val.equals("Membership merge")) {// here check the field contains or not
				str_data2 = val;
				break;
			}
		}

		List<String> getmembershipIds = jsonPtEval.getList("data.membershipKey");
		System.out.println("The source events history membershipKey:" + getmembershipIds);
		for (String val : getmembershipIds) {
			if (val.equals(sourceKey)) {// here check the field contains or not
				strMemberkey = val;
				break;
			}
		}

		Assert.assertEquals(targetKey, str_data1);
		Assert.assertEquals("Membership merge", str_data2);
		Assert.assertEquals(sourceKey, strMemberkey);
	}

	@Given("Target member wants to check alternateIds information")
	public void target_member_wants_to_check_alternateIds_information() {
		targetKey = gettargetKey();
	}

	@When("Target member perform the alternateIds information")
	public void target_member_perform_the_alternateIds_information() {
		targetKey = gettargetKey();

		responseTRGALTHY = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("api/v10/memberships/" + targetKey + "/details");
	}

	@Then("Target member should have all the source member alternateIds detail with status code {int}")
	public void target_member_should_have_all_the_source_member_alternateIds_detail_with_status_code(
			Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), responseTRGALTHY.getStatusCode());
		JsonPath jsPathEval = responseTRGALTHY.jsonPath();

		boolean getstatus = jsPathEval.get("success");
		Assert.assertEquals(getstatus, true);

		String strTrgMemberCard = null;
		String getSourceAlterIds = getsourceAlternateIds();
		// check here for mateIds
		targetKey = gettargetKey();
		String strTrgmateIds = null;
		System.out.println("check alternatId:" + getSourceAlterIds);

		List<String> altnerntIds = jsPathEval.getList("data.alternateIds");
		System.out.println("The target alternateId " + altnerntIds);
		for (String id : altnerntIds) {
			if (id.equals(getSourceAlterIds)) {// here check the field contains or not
				strTrgMemberCard = id;
				break;
			}
		}
		Assert.assertEquals(strTrgMemberCard, getSourceAlterIds);

		List<String> getmateIds = jsPathEval.getList("data.mateIds");
		System.out.println("The target mateIds " + getmateIds);
		for (String id : getmateIds) {
			if (id.equals(targetKey)) {// here check the field contains or not
				strTrgmateIds = id;
				break;
			}
		}

		Assert.assertEquals(targetKey, strTrgmateIds);// check mateids contains the target key

	}

	@Given("Target member wants to check member merge")
	public void target_member_wants_to_check_member_merge() {
		targetKey = gettargetKey();
	}

	@When("Target member perform check remarks")
	public void target_member_perform_check_remarks() {
		targetKey = gettargetKey();

		responseTRGREMARKS = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("api/v10/memberships/" + targetKey + "/remarks");
	}

	@Then("Target member should be able to get successful remark for member merge with status code {int}")
	public void target_member_should_be_able_to_get_successful_remark_for_member_merge_with_status_code(
			Integer statuscode) {

		targetKey = gettargetKey();
		sourceKey = getsourceKey();
		Assert.assertEquals(statuscode.intValue(), responseTRGREMARKS.getStatusCode());
		JsonPath jsPathEval = responseTRGREMARKS.jsonPath();

		boolean getstatus = jsPathEval.get("success");
		Assert.assertEquals(getstatus, true);

		String strSummry = null;
		String stMatchText = "Membership merge";
		List<String> lstsummry = jsPathEval.get("data.summary");
		System.out.println("The summary :" + lstsummry);
		for (String id : lstsummry) {
			if (id.equals(stMatchText)) {// here check the field contains or not
				strSummry = id;
				break;
			}
		}
		Assert.assertEquals(strSummry, stMatchText);
/*
		sourceKey = getsourceKey();
		String stSourceKey = null;
		List<String> sourcekey = jsPathEval.getList("data.details.SourceKey");
		System.out.println("Source Key :" + sourcekey);
		for (String id : sourcekey) {
			if (id.equals(sourceKey)) {// here check the field contains or not
				stSourceKey = id;
				break;
			}
		}
		Assert.assertEquals(stSourceKey, sourceKey);

		targetKey = gettargetKey();
		String stTargetKey = null;
		List<String> tarKey = jsPathEval.get("data.details.TargetKey");
		System.out.println("Source Key :" + tarKey);
		for (String id : tarKey) {
			if (id.equals(targetKey)) {// here check the field contains or not
				stTargetKey = id;
				break;
			}
		}
		System.out.println("Target Key :" + stTargetKey);
		Assert.assertEquals(stTargetKey, targetKey);

		String strStatus = null;
		List<String> status = jsPathEval.get("data.details.Status");
		System.out.println("Status :" + status);
		for (String id : status) {
			if (id.equals("SUCCESSFUL")) {// here check the field contains or not
				strStatus = id;
				break;
			}
		}
		Assert.assertEquals(strStatus, "SUCCESSFUL");
		*/

		String getText = null;
		String resText = "Membership with key: " + sourceKey + " got merged into: " + targetKey
				+ " (points transferred)";
		List<String> lstText = jsPathEval.get("data.text");
		System.out.println("The summary :" + lstText);
		for (String id : lstText) {
			if (id.equals(resText)) {// here check the field contains or not
				getText = id;
				break;
			}
		}

		Assert.assertEquals(getText, resText);
	}

	@Given("Target member want to issue a voucher after merge")
	public void target_member_want_to_issue_a_voucher_after_merge() {
		targetKey = gettargetKey();
		String getSourceVoucherName = getsourceVoucherName();
		System.out.println("The voucher token :" + getSourceVoucherName);
	}

	@When("Target member perform a issue token after merge")
	public void target_member_perform_a_issue_token_after_merge() {

		targetKey = gettargetKey();
		String getSourceVoucherName = getsourceVoucherName();
		System.out.println("The voucher token :" + getSourceVoucherName);

		responseSRCVoucherCissue = SerenityRest.given().urlEncodingEnabled(false).queryParam("recipient", targetKey)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + getSourceVoucherName + "/issue");
	}

	@Then("Target member should be able to issue the voucher successfully with status code {int}")
	public void target_member_should_be_able_to_issue_the_voucher_successfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseSRCVoucherCissue.getStatusCode());

		JsonPath getjsonPathVal = responseSRCVoucherCissue.jsonPath();

		Boolean strSuccess = getjsonPathVal.get("success");
		Assert.assertEquals(true, strSuccess);

	}

	@Given("Source member wants to see his membership status")
	public void source_member_wants_to_see_his_membership_status() {
		sourceKey = getsourceKey();
		System.out.println("The souce key :" + sourceKey);
	}

	@When("Source member perform membership status")
	public void source_member_perform_membership_status() {

		sourceKey = getsourceKey();

		responseSRCMEMBSHIP = SerenityRest.given().urlEncodingEnabled(false).queryParam("filter",
				"[{\"property\": \"contractStatus\", \"value\": \"CANCELLED\"},{\"property\":\"key\",\"value\":\""
						+ sourceKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/memberships");
	}

	@Then("Source member should be able to see the membership status as canceled with status code {int}")
	public void source_member_should_be_able_to_see_the_membership_status_as_canceled_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseSRCMEMBSHIP.getStatusCode());

		JsonPath getjsonVal = responseSRCMEMBSHIP.jsonPath();

		Boolean strSuccess = getjsonVal.get("success");
		Assert.assertEquals(true, strSuccess);

		String resText = "CANCELLED";
		String getText = null;
		List<String> lstText = getjsonVal.get("data.contractStatus");
		System.out.println("The summary :" + lstText);
		for (String id : lstText) {
			if (id.equals(resText)) {// here check the field contains or not
				getText = id;
				break;
			}
		}
		Assert.assertEquals(getText, resText);
	}

	@Given("Source member wants to earn points")
	public void source_member_wants_to_earn_points() {
		sourceKey = getsourceKey();
		System.out.println("source key: " + sourceKey);
	}

	@When("Source member performs earn points after merging")
	public void source_member_performs_earn_points_after_merging() {

		String TrxId = "MRGSRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.getDateTime();
		_trxDate = TrxDate; // Set value here for transaction
		String price = "10";
		String quantity = "1";
		sourceKey = getsourceKey();
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
				channelkey, "");

		responseSRCAFTERTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Source member should not be able to earn point after merging and show status code {int}")
	public void source_member_should_not_be_able_to_earn_point_after_merging_and_show_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseSRCAFTERTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseSRCAFTERTX.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Source member wants to redeem points")
	public void source_member_wants_to_redeem_points() {
		sourceKey = getsourceKey();
		System.out.println("source key: " + sourceKey);
	}

	@When("Source member performs redeem points after merging")
	public void source_member_performs_redeem_points_after_merging() {
		String TrxDate = LoadProperties().getProperty("redeemdate");// get date value from last
		_RedTXDate = TrxDate;
		// String receiptId = LoadProperties().getProperty("receiptId") +
		// TestUtils.getRandomValue();
		String receiptId = TrxDate + TestUtils.getRandomValue();
		_receiptId = receiptId;
		String debitorId = LoadProperties().getProperty("debitorId");
		_debitorId = debitorId;
		String amount = LoadProperties().getProperty("redeemamount");
		_amount = amount;
		String description = "Test Redeem";
		String clientKey = getsourceKey();
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);

		responseSRCAFTERRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/deposits/" + clientKey + "/redeem?type=BONUS");
	}

	@Then("Source member should not be able to redeem point after merging and show status code {int}")
	public void source_member_should_not_be_able_to_redeem_point_after_merging_and_show_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseSRCAFTERRD.getStatusCode());

		JsonPath jsonPathEvaluator = responseSRCAFTERRD.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Source member wants to adjust points")
	public void source_member_wants_to_adjust_points() {
		sourceKey = getsourceKey();
		System.out.println("source key: " + sourceKey);
	}

	@When("Source member performs adjust points after merging")
	public void source_member_performs_adjust_points_after_merging() {

		String receiptId = getReceiptId();
		String date = LoadProperties().getProperty("adjustmentdate");
		String amount = "10";
		String debitorId = getDebitorId();
		String description = LoadProperties().getProperty("adjustmentdesc");
		String clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount, debitorId, description);

		responseSRCAFTERADJ = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				.body(strapiBody).log().all().when().post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");
	}

	@Then("Source member should not be able to points adjustment after merging and show status code {int}")
	public void source_member_should_not_be_able_to_points_adjustment_after_merging_and_show_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseSRCAFTERADJ.getStatusCode());

		JsonPath jsonPathEvaluator = responseSRCAFTERADJ.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Source member wants to reverse points")
	public void source_member_wants_to_reverse_points() {
		sourceKey = getsourceKey();
		System.out.println("source key: " + sourceKey);
	}

	@When("Source member performs point reverse after merging")
	public void source_member_performs_point_reverse_after_merging() {
		sourceKey = getsourceKey();
		String description = LoadProperties().getProperty("Description");
		AIPsJsonBody apibody = new AIPsJsonBody();
		sourceKey = getsourceKey();
		_TrxId = getTrxId();
		_trxDate = getTrxDate();
		String debitorId=LoadProperties().getProperty("debitorId");

		strapiBody = apibody.TranscationReversalAPIBody(_TrxId, _trxDate, sourceKey, description,debitorId);

		responseSRCAFTERTXREV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				.body(strapiBody).log().all().when().post("/api/v10/tx/reversal");
	}

	@Then("Source member should not be able to do points reverse after merging and show status code {int}")
	public void source_member_should_not_be_able_to_do_points_reverse_after_merging_and_show_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseSRCAFTERTXREV.getStatusCode());

		JsonPath jsonPathEvaluator = responseSRCAFTERTXREV.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Target member wants to check his total event count")
	public void target_member_wants_to_check_his_total_event_count() {
		targetKey = gettargetKey();
		System.out.println("target key: " + targetKey);
	}

	@When("Target member perform event count in remark api")
	public void target_member_perform_event_count_in_remark_api() {

		targetKey = gettargetKey();

		response_TARGEVT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("api/v10/memberships/" + targetKey + "/remarks");

	}

	@Then("Target member should have the same number of events count as of source member after merge with status code {int}")
	public void target_member_should_have_the_same_number_of_events_count_as_of_source_member_after_merge_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), response_TARGEVT.getStatusCode());

		JsonPath getjsonPathVal = response_TARGEVT.jsonPath();

		Boolean strSuccess = getjsonPathVal.get("success");
		Assert.assertEquals(true, strSuccess);

		List<String> _lstdata = getjsonPathVal.get("data.details[0].MergedEventRecords");

		System.out.println("lst data" + _lstdata);
		Integer TotalCount = _lstdata.size();// get the total count

		System.out.println("total count:" + TotalCount);
		Integer getsourceEventCount = gettotaleventcount();

		Assert.assertEquals(TotalCount, getsourceEventCount);

	}

	@Given("Source member wants to merge with source member")
	public void source_member_wants_to_merge_with_source_member() {
		sourceKey = getsourceKey();
	}

	@When("Source member perform merge source member")
	public void source_member_perform_merge_source_member() {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.MemberMergeAPIBody(sourceKey, sourceKey);

		responseSMBMERGE = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/memberships/merge");
	}

	@Then("Merging of same member should not be allowed and show status code {int}")
	public void merging_of_same_member_should_not_be_allowed_and_show_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseSMBMERGE.getStatusCode());
		JsonPath jsonPathEvaluator1 = responseSMBMERGE.jsonPath();

		boolean getstatus = jsonPathEvaluator1.get("success");
		Assert.assertEquals(getstatus, false);
	}

	@Then("Error message should be {string}")
	public void error_message_should_be(String strMsg) {
		
		JsonPath jsonPathEval = responseSMBMERGE.jsonPath();
		String getErrorMsg = jsonPathEval.get("error");
		Assert.assertEquals(getErrorMsg, strMsg);

	}
	
	@When("member performs enrollment of another member")
	public void member_performs_enrollment_of_another_member() {
		
		anotherKey = LoadProperties().getProperty("enkey") + "_MEGOTH" + TestUtils.getRandomValue();
		_anotherKey = anotherKey; // save this member to make it target
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MEGDOTH_AltId" + TestUtils.getRandomValue();
		String strUserId = anotherKey;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, anotherKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		responseOTHM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("another member is created with statuscode {int}")
	public void another_member_is_created_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseOTHM.getStatusCode());
		
		JsonPath jsonPathEval = responseOTHM.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	
	@Given("Check the source member points expiry")
	public void check_the_source_member_points_expiry() {
	    System.out.println("source member points expiry");
	}

	@When("Source member perform merge with target member")
	public void source_member_perform_merge_with_target_member() {
	    
	}

	@Then("Points transferred to target member should have same expiry as defined initially")
	public void points_transferred_to_target_member_should_have_same_expiry_as_defined_initially() {
	    
	}

	@Given("Target member wants to transfer points to another member")
	public void target_member_wants_to_transfer_points_to_another_member() {
	 targetKey=gettargetKey();
	 anotherKey=getanotherKey();
	}

	@When("Target member perform points transfer")
	public void target_member_perform_points_transfer() {
		
		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		String amount = "10";
		String debitorId=LoadProperties().getProperty("debitorId");
		String description ="Test Transfer";
		String clientKey = gettargetKey();
		String destclientKey = getanotherKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount, description);

		responseTPT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	    
	}
	
	@Then("point transfer from target to another member is successful with status code {int}")
	public void point_transfer_from_target_to_another_member_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTPT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTPT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	
}