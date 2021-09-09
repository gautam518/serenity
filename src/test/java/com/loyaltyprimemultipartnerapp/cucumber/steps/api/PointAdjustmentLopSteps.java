package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import com.loyaltyprimemultipartnerapp.utlis.AIPsJsonBody;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.loyaltyprimemultipartnerapp.utlis.TestUtils;

import java.io.InputStream;
import java.util.Properties;

public class PointAdjustmentLopSteps {
	private String clientKey;// Source key
	private String TargetKey;// Source key
	private Response response = null; // Response
	HttpHeaders headers;
	String strapiBody;
	private RequestSpecification request;
	Response responseSM;
	Response response_ACCSTM;
	Response responseHst=null;
	Response responseMT=null;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	public static String gettargetKey() {
		return _targetKey;
	}

	private static String _targetKey;

	public static Float getMemberbalancebefor() {
		return _memberbalancebefor;
	}

	private static Float _memberbalancebefor;

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

	@Given("Member wants to enroll a member for point adjustment")
	public void member_wants_to_enroll_a_member_for_point_adjustment() {
		clientKey = LoadProperties().getProperty("enkey") + "_SRC" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
	}

	@When("Member perform enrollment for point adjustment")
	public void member_perform_enrollment_for_point_adjustment() {
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

	@Then("Member should be able to enroll member with status code {int}")
	public void member_should_be_able_to_enroll_member_with_status_code(Integer code) {

	}

	@Given("Member perform point adjustment")
	public void member_perform_point_adjustment() {
		request = SerenityRest.given();
		System.out.println("API body:" + request);
	}

	@When("Member performs adjust points with date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_adjust_points_with_date_receiptId_debitorId_amount_and_description(String date,
			String receiptId, String debitorId, Double amount, String description) throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Member adjustment transaction has been done successfully:" + response.asString());

		Thread.sleep(5000);
	}

	@Then("Member should get the successfull message for points adjustment with statuscode {int}")
	public void member_should_get_the_successfull_message_for_points_adjustment_with_statuscode(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("Member performs point adjustment for negative amount with date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_point_adjustment_for_negative_amount_with_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Member adjustment done successfully:" + response.asString());

		Thread.sleep(1000);
	}

	@When("Member performs point adjustment without receiptId and with date {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_point_adjustment_without_receiptId_and_with_date_debitorId_amount_and_description(
			String date, String debitorId, Double amount, String description) throws InterruptedException {
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody("", date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");
		Thread.sleep(1000);
	}

	@Then("Member should get the erorr message for points adjustment with statuscode {int}")
	public void member_should_get_the_erorr_message_for_points_adjustment_with_statuscode(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
	}

	@When("Member performs points adjustment without date and with parameters receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_without_date_and_with_parameters_receiptId_debitorId_amount_and_description(
			String receiptId, String debitorId, Double amount, String description) throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, "", amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Member adjustment:" + response.asString());

		Thread.sleep(1000);
	}

	@Then("Member should get the successful message for points adjustment with statuscode {int}")
	public void member_should_get_the_successful_message_for_points_adjustment_with_statuscode(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
	}

	@When("Member performs points adjustment without amount and with parameters date {string} receiptId {string} debitorId {string} and description {string}")
	public void member_performs_points_adjustment_without_amount_and_with_parameters_date_receiptId_debitorId_and_description(
			String date, String receiptId, String debitorId, String description) throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, "", debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Member adjustment done successfully:" + response.asString());

		Thread.sleep(1000);
	}

	@Given("Member perform point adjustment without member number in parameter")
	public void member_perform_point_adjustment_without_member_number_in_parameter() {

		request = SerenityRest.given();
	}

	@When("Member performs points adjustment without member number and with parameters date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_without_member_number_and_with_parameters_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/adjust?type=BONUS");

		System.out.println("Response" + response.asString());

		Thread.sleep(1000);
	}

	@Then("Member should get the error message for points adjustment with statuscode {int}")
	public void member_should_get_the_error_message_for_points_adjustment_with_statuscode(Integer statuscode) {

		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());
	}

	@Given("Member perform point adjustment without type in parameter")
	public void member_perform_point_adjustment_without_type_in_parameter() {
		request = SerenityRest.given();
	}

	@When("Member performs points adjustment without type and with parameters date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_without_type_and_with_parameters_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description) throws Exception {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/adjust");

		System.out.println("Response" + response.asString());

		Thread.sleep(10000);
	}

	@Given("Member perform point adjustment with innvalid type in parameter")
	public void member_perform_point_adjustment_with_innvalid_type_in_parameter() {

		request = SerenityRest.given();
	}

	@When("Member performs points adjustment with invalid type and having parameters date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_with_invalid_type_and_having_parameters_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=shsh4BONUS");

		System.out.println("Response" + response.asString());

		Thread.sleep(1000);
	}

	@Given("Member perform point adjustment with invalid member number in parameter")
	public void member_perform_point_adjustment_with_invalid_member_number_in_parameter() {

		request = SerenityRest.given();
		System.out.println("API body" + request);
	}

	@When("Member performs points adjustment with invalid member number and date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_with_invalid_member_number_and_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = "XMAXXXX_123";
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("adjustment response" + response.asString());

		Thread.sleep(500);
	}

	@When("Member perform check account balance befor adjustment")
	public void member_perform_check_account_balance_befor_adjustment() {

		clientKey = getsourceKey();
		response_ACCSTM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=bonus&limit=0");

		JsonPath getjsonVal = response_ACCSTM.jsonPath();
		Float strAcctBlance = getjsonVal.get("data.balance");
		_memberbalancebefor = strAcctBlance;
	}

	@When("Member performs points adjustment with negative amount and greater than balance and with parameters date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_with_negative_amount_and_greater_than_balance_and_with_parameters_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		Double oldBalance = getMemberbalancebefor().doubleValue();
		if (amount > oldBalance) {
			amount = amount;
		} else {
			amount = amount + oldBalance;

		}

		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Response" + response.asString());

		Thread.sleep(300);
	}

	@Then("Member should be able to get points for points adjustment with statuscode {int}")
	public void member_should_be_able_to_get_points_for_points_adjustment_with_statuscode(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@When("Member performs points adjustment with passing any value in receiptId and with date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_points_adjustment_with_passing_any_value_in_receiptId_and_with_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
		clientKey = getsourceKey();
		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Member adjustment:" + response.asString());

		Thread.sleep(300);
	}

	@When("Check member history and point redeemption")
	public void check_member_history_and_point_redeemption() throws InterruptedException {

		clientKey = getsourceKey();
		
		 responseHst = request.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");

		
	}
	@Then("Member history is disapayed successfully with status code {int}")
	public void member_history_is_disapayed_successfully_with_status_code(Integer code) {
	   
		Assert.assertEquals(code.intValue(), responseHst.getStatusCode());
	}


	@Given("Member perform point adjustment for cancelled member")
	public void member_perform_point_adjustment_for_cancelled_member() {

		request = SerenityRest.given();
	}

	@When("Member performs adjust points for cancelled member with date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void member_performs_adjust_points_for_cancelled_member_with_date_receiptId_debitorId_amount_and_description(
			String date, String receiptId, String debitorId, Double amount, String description)
			throws InterruptedException {

		clientKey = "MSHIP_ToMerge_A";// This is cancelled member Id
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		System.out.println("Member adjustment for cancelled :" + response.asString());

		Thread.sleep(10000);
	}

	@When("Member perform enrollment for source member")
	public void member_perform_enrollment_for_source_member() {

		TargetKey = LoadProperties().getProperty("enkey") + "_TARG" + TestUtils.getRandomValue();
		_targetKey = TargetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TARG_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = TargetKey;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, TargetKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		responseSM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@When("Member perform member merge from source to target member")
	public void member_perform_member_merge_from_source_to_target_member() {
		String targetclientKey = getsourceKey();
		String sourceclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.MemberMergeAPIBody(sourceclientKey, targetclientKey);

		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/memberships/merge");

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}

	@When("Target performs adjust points with date {string} receiptId {string} debitorId {string} amount {double} and description {string}")
	public void target_performs_adjust_points_with_date_receiptId_debitorId_amount_and_description(String date,
			String receiptId, String debitorId, Double amount, String description) throws InterruptedException {

		clientKey = getsourceKey();
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		response = request.spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log().all().when()
				.post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");

		Thread.sleep(10000);
	}

	@When("Check target member point history")
	public void check_target_member_point_history() {

		clientKey = getsourceKey();
		
		responseMT = request.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}
}
