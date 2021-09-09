package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import com.loyaltyprimemultipartnerapp.testbase.TestBase;
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

public class VoucherIssueDateOnSinceDateSteps extends TestBase {

	String strapiBody;
	Response response = null;
	Response responseVCGNe = null;
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

	Response responseTM = null;
	Response responseVCissue = null;
	Response responseVCTpe = null;
	Response responseSRCVTOKEN = null;
	Response responseRD = null;

	@Given("user check the vouchers for issue date")
	public void user_check_the_vouchers_for_issue_date() {
		System.out.println("voucher issue date");
	}

	@When("user perform create voucher type for issue date")
	public void user_perform_create_voucher_type_for_issue_date() {

		sourceKey = getsourceKey();
		String strdescription = LoadProperties().getProperty("description");
		String strvcOf = LoadProperties().getProperty("VcTypeof");
		String strvalue = LoadProperties().getProperty("value");
		String name = "TST_VOUCHER_500_112";
				//sourceKey + "NegMBlc";

		_sourceVoucher = name;

		strapiBody = null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.CreateVoucherType(name, strdescription, strvcOf, strvalue);
		// voucher type
		responseVCTpe = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/voucher/type");
	}

	@Then("voucher type for issue date is created successfully with statuscode {int}")
	public void voucher_type_for_issue_date_is_created_successfully_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseVCTpe.getStatusCode());
	}

	@When("user perform generate voucher for issue date")
	public void user_perform_generate_voucher_for_issue_date() {
		/// Generate vocuhers
		String name = getsourceVoucher();
		responseVCGNe = SerenityRest.given().urlEncodingEnabled(false).queryParam("quantity", 1)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + name + "/generate");
	}

	@Then("voucher for issue date is generated successfully with statuscode {int}")
	public void voucher_for_issue_date_is_generated_successfully_with_statuscode(Integer code) {
		Assert.assertEquals(HttpStatus.SC_OK, responseVCGNe.getStatusCode());
	}

	@When("user perform enrollment of member for issue date")
	public void user_perform_enrollment_of_member_for_issue_date() {

		sourceKey = LoadProperties().getProperty("enkey") + "_VCSRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_VCSRC_AltId" + TestUtils.getRandomValue()
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

	@Then("member is created for issue date is successfull with statuscode {int}")
	public void member_is_created_for_issue_date_is_successfull_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform voucher issue to member for issue date")
	public void user_perform_voucher_issue_to_member_for_issue_date() {

		String name = getsourceVoucher();
		responseVCissue = SerenityRest.given().urlEncodingEnabled(false).queryParam("recipient", getsourceKey())
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + name + "/issue");
	}

	@Then("user issues the voucher for issue date successfully with statuscode {int}")
	public void user_issues_the_voucher_for_issue_date_successfully_with_statuscode(Integer code) {

		Assert.assertEquals(HttpStatus.SC_OK, responseVCissue.getStatusCode());

		JsonPath getjsonPathVal = responseVCissue.jsonPath();

		Boolean strSuccess = getjsonPathVal.get("success");
		Assert.assertEquals(true, strSuccess);

		// get token value store here
		String strSrcVCToken = getjsonPathVal.get("data.token");
		_sourceVoucherToken = strSrcVCToken;
		System.out.println("set source Token:" + strSrcVCToken);
	}

	@When("user perform the convert voucher for issue date")
	public void user_perform_the_convert_voucher_for_issue_date() {

		sourceKey = getsourceKey();

		System.out.println("Voucher Token12 :" + getsourceVoucherToken());
		String name = getsourceVoucher();
		Response responseMeb = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"type\", \"value\": \"" + name + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/voucher");

		JsonPath getjsonEval = responseMeb.jsonPath();
		String getsourceToken = getjsonEval.get("data[0].token");

		// getsourceVoucherToken();
		System.out.println("Voucher Token :" + getsourceToken);
		// code to convert the voucher token
		response = SerenityRest.given().urlEncodingEnabled(false).queryParam("client", sourceKey)
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.post("/api/v10/voucher/" + getsourceToken + "/convert");
	}

	@Then("convert voucher for issue date is successfull with statuscode {int}")
	public void convert_voucher_for_issue_date_is_successfull_with_statuscode(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	
	@When("user perform earn transaction for issue date")
	public void user_perform_earn_transaction_for_issue_date() throws InterruptedException {
	   
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

	@Then("earn transaction for issue date is successfully with status code {int}")
	public void earn_transaction_for_issue_date_is_successfully_with_status_code(Integer code) {
	   
		Assert.assertEquals(200, response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

}
