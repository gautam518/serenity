package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.time.ZonedDateTime;
import java.util.List;

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

public class MemberNegativeBalanceTransactionsSteps extends TestBase {

	String strapiBody;
	Response response = null;
	Response responseVCGNe  = null;
	private String TrxDate;
	private String TrxId;
	private String sourceKey;// source key
	private String targetKey;// targetKey

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

	public static String getRcptId() {
		return _RcptId;
	}

	private static String _RcptId;

	public static String getsourceVoucherToken() {
		return _sourceVoucherToken;
	}

	private static String _sourceVoucherToken;

	public static String getsourceVoucherName() {
		return _sourceVoucherName;
	}

	private static String _sourceVoucherName;

	public static String getsourceVoucher() {
		return _sourceVoucher;
	}

	private static String _sourceVoucher;
	
	public static String getcancelMember() {
		return _cancelMember;
	}

	private static String _cancelMember;

	Response responseTM=null;
	Response responseVCissue = null;
	Response responseVCTpe = null;
	Response responseSRCVTOKEN = null;
	Response responseRD=null;

	@When("user perform enrollment of member")
	public void user_perform_enrollment_of_member() {

		sourceKey = LoadProperties().getProperty("enkey") + "_PSRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_PSRC_AltId" + TestUtils.getRandomValue()
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
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("member is created successfull with statuscode {int}")
	public void member_is_created_successfull_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform point adjustment")
	public void user_perform_point_adjustment() throws InterruptedException {
		String receiptId = "Recp_" + TestUtils.getRandomValue();
		String date = TestUtils.getDateTime();
		String amount = "-500";
		String debitorId = LoadProperties().getProperty("partnerId");
		String description = "Test adjustment";
		sourceKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount, debitorId, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/adjust?type=BONUS");

		Thread.sleep(10000);
	}

	@Then("member should get the points adjustment successfull with statuscode {int}")
	public void member_should_get_the_points_adjustment_successfull_with_statuscode(Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("status from Response " + getStatus);
	}

	@Given("user check the earn transaction")
	public void user_check_the_earn_transaction() {
		System.out.println("user check the earn transaction");
	}

	@When("user perform earn transaction")
	public void user_perform_earn_transaction() throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		String price = "20";
		String quantity = "1";
		String partnerId = LoadProperties().getProperty("partnerId");
		sourceKey = getsourceKey();

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
		strapiBody = apibody.TransactionAPISingleBody(TrxId, sourceKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("member should be able to see earn transaction successfully with status code {int}")
	public void member_should_be_able_to_see_earn_transaction_successfully_with_status_code(Integer code) {
		Assert.assertEquals(200, response.getStatusCode());
	}

	@Given("user check the point redemption")
	public void user_check_the_point_redemption() {
		System.out.println("user check the point redemption");
	}

	@When("user perform point redemption")
	public void user_perform_point_redemption() {
		sourceKey = getsourceKey();
		String TrxDate = TestUtils.getDateTime();// get date value from last
		_trxDate = TrxDate;
		String receiptId = "RepId_" + TestUtils.getRandomValue();
		String debitorId = LoadProperties().getProperty("debitorId");
		_RcptId = receiptId + "_" + debitorId;
		float amount = (float) 21.00;
		// _amountredem=amount;
		String description = "Test Redemption";
		strapiBody = null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, String.valueOf(amount), description);

		responseRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");
	}

	@Then("user should not be able to see member redemption with status code {int}")
	public void user_should_not_be_able_to_see_member_redemption_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseRD.getStatusCode());
	}

	@Given("user check the point redemption reversal")
	public void user_check_the_point_redemption_reversal() {
		System.out.println("user check the point redemption reversal");
	}

	@When("user perform point redemption reversal")
	public void user_perform_point_redemption_reversal() {
		TrxId = getRcptId();
		TrxDate = getTrxDate();
		String description = "Auto Redemption Reversal";
		String PointsType = LoadProperties().getProperty("pointType");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
	}

	@Then("user should not be able to see member redemption reversal with status code {int}")
	public void user_should_not_be_able_to_see_member_redemption_reversal_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("user check the point transfer")
	public void user_check_the_point_transfer() {
		System.out.println("user check the point transfer");
	}

	@When("user perform enrollment for target member")
	public void user_perform_enrollment_for_target_member() {
		targetKey = LoadProperties().getProperty("enkey") + "_TRGME" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_targetKey = targetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TRGME_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
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

	@Then("target member is created successfull with statuscode {int}")
	public void target_member_is_created_successfull_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseTM.getStatusCode());

		JsonPath jsonPathEvaluator = responseTM.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform point transfer")
	public void user_perform_point_transfer() {

		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		Integer amount = 10;
		String debitorId = LoadProperties().getProperty("debitorId");
		String description = "Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount.toString(), description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Then("user should not able to do point transfer with status code {int}")
	public void user_should_not_able_to_do_point_transfer_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("user check the member merge")
	public void user_check_the_member_merge() {
		System.out.println("user check the member merge");
	}

	@When("user perform member merge")
	public void user_perform_member_merge() {
		sourceKey = getsourceKey();
		targetKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.MemberMergeAPIBody(sourceKey, targetKey);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships/merge");
	}

	@Then("user should not able to do member merge with status code {int}")
	public void user_should_not_able_to_do_member_merge_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@Given("user check the vouchers")
	public void user_check_the_vouchers() {
		System.out.println("user check the vouchers");
	}
	
	
	@When("user perofm create voucher type to member")
	public void user_perofm_create_voucher_type_to_member() {
		sourceKey = getsourceKey();
		String strdescription = LoadProperties().getProperty("description");
		String strvcOf = LoadProperties().getProperty("VcTypeof");
		String strvalue = LoadProperties().getProperty("value");
		String name = sourceKey + "NegMBlc";

		_sourceVoucher = name;

		strapiBody = null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.CreateVoucherType(name, strdescription, strvcOf, strvalue);
		//voucher type
		responseVCTpe = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/voucher/type");
	}
	@Then("voucher type is created successfully with statuscode {int}")
	public void voucher_type_is_created_successfully_with_statuscode(Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, responseVCTpe.getStatusCode());

	}
	@When("user perofm generate voucher")
	public void user_perofm_generate_voucher() {
		/// Generate vocuhers
		String name=getsourceVoucher();
			 responseVCGNe = SerenityRest.given().urlEncodingEnabled(false).queryParam("quantity", 1)
						.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
						.post("/api/v10/voucher/" + name + "/generate");
	}
	@Then("voucher is generated successfully with statuscode {int}")
	public void voucher_is_generated_successfully_with_statuscode(Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, responseVCGNe.getStatusCode());

	}
	@When("user perofm voucher issue to member")
	public void user_perofm_voucher_issue_to_member() {
		String name=getsourceVoucher();
				responseVCissue = SerenityRest.given().urlEncodingEnabled(false).queryParam("recipient", getsourceKey())
						.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
						.post("/api/v10/voucher/" + name + "/issue");
	    
	}
	@Then("user issues the voucher successfully with statuscode {int}")
	public void user_issues_the_voucher_successfully_with_statuscode(Integer int1) {
		
		Assert.assertEquals(HttpStatus.SC_OK, responseVCissue.getStatusCode());

		JsonPath getjsonPathVal = responseVCissue.jsonPath();

		Boolean strSuccess = getjsonPathVal.get("success");
		Assert.assertEquals(true, strSuccess);

		// get token value store here
		String strSrcVCToken = getjsonPathVal.get("data.token");
		_sourceVoucherToken = strSrcVCToken;
		System.out.println("set source Token:" + strSrcVCToken);
	}
	
	@When("user perform the convert voucher")
	public void user_perform_the_convert_voucher() {
		sourceKey = getsourceKey();
		
		System.out.println("Voucher Token12 :"+ getsourceVoucherToken());
		String name=getsourceVoucher();
		Response responseMeb = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"type\", \"value\": \"" + name + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/voucher");
		
		JsonPath getjsonEval = responseMeb.jsonPath();
		String getsourceToken = getjsonEval.get("data[0].token");
		
				//getsourceVoucherToken();
		System.out.println("Voucher Token :"+ getsourceToken);
		// code to convert the voucher token
		response = SerenityRest.given().urlEncodingEnabled(false).queryParam("client", sourceKey)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + getsourceToken + "/convert");
	}

	@Then("convert voucher is successfull with statuscode {int}")
	public void convert_voucher_is_successfull_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("user check the member balance")
	public void user_check_the_member_balance() {
		System.out.println("user check the member balance");
	}

	@When("user perform membership update of member")
	public void user_perform_membership_update_of_member() {
		sourceKey = getsourceKey();
		_cancelMember = sourceKey;
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
				// .body(strapiBody)
				.log().all().when().post("/api/v10/memberships/" + sourceKey + "/cancel");
	}

	@Then("membership update of member is successful with status code {int}")
	public void membership_update_of_member_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Then("user should not be able to see member detail with status code {int}")
	public void user_should_not_be_able_to_see_member_detail_with_status_code(Integer int1) {

	}

}
