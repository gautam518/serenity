package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

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

public class MemberTierAssessmentLopSteps extends TestBase {

	String strapiBody;
	private String sourceKey;// source key
	String strSilver = "SILVER";
	String strGold = "GOLD";
	String strBasic = "BASIC";

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	Response response = null;
	Response responseMD = null;
	Response responseTX = null;
	Response responseTXT = null;
	Response responseIVMB = null;
	Response responseRD = null;
	Response responseTXRD = null;

	@Given("member is enroll for Tier Assessment")
	public void member_is_enroll_for_Tier_Assessment() {
		System.out.println("member is enroll for Tier Assessment");
	}

	@When("user perform enrollment for tier assessment")
	public void user_perform_enrollment_for_tier_assessment() {

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

	@Then("member is created for tier assessment successfull with statuscode {int}")
	public void member_is_created_for_tier_assessment_successfull_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("member post transaction to update member tier")
	public void member_post_transaction_to_update_member_tier() throws InterruptedException {
		System.out.println("member tier");
	}

	@When("user perform earn transaction for member tier")
	public void user_perform_earn_transaction_for_member_tier() throws InterruptedException {
		String TrxId = "SRC_TX_" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		String price = "150";
		String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerIdTier");
		clientKey = getsourceKey();
		String channelkey = "";
		String productKey = "";
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");
		String _getChannelKey = "TIER";
		channelkey = _getChannelKey;
		productKey = _getProductKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);
		responseTXT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("earn transaction of member for tier is successfull with status code {int}")
	public void earn_transaction_of_member_for_tier_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTXT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXT.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform get member tier information")
	public void user_perform_get_member_tier_information() throws InterruptedException {

		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/details");
		Thread.sleep(1000);
	}

	@Then("member can see his tier detail succesfully with status code {int}")
	public void member_can_see_his_tier_detail_succesfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

		String strTier = jsonPathEvaluator.get("data.tier");
		Assert.assertEquals(strSilver, strTier);
	}
//--- for downgrade---

	@Given("member post redemption to downgrade member tier")
	public void member_post_redemption_to_downgrade_member_tier() {
		System.out.println("redemption");
	}

	@When("user perform earn transaction for member tier downgrade")
	public void user_perform_earn_transaction_for_member_tier_downgrade() throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		String price = "150";
		String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerIdTier");
		clientKey = getsourceKey();
		String channelkey = "";
		String productKey = "";
		// get dymanically the productid and channelKey here
		Response responseProductData;
		responseProductData = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when().get("/api/v10/promos/product/data");
		JsonPath jsonPathEval = responseProductData.jsonPath();
		String _getProductKey = jsonPathEval.get("data[0].key");
		String _getChannelKey = "TIER";// jsonPathEval.get("data[0].channelKey");

		channelkey = _getChannelKey;
		productKey = _getProductKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);
		responseTXRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("earn transaction of member for tier downgrade is successfull with status code {int}")
	public void earn_transaction_of_member_for_tier_downgrade_is_successfull_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTXRD.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXRD.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform redemption for tier downgrade")
	public void user_perform_redemption_for_tier_downgrade() throws InterruptedException {

		sourceKey = getsourceKey();
		String TrxDate = TestUtils.getDateTime();// get date value from last
		String receiptId = "RepId_" + TestUtils.getRandomValue();
		String debitorId = LoadProperties().getProperty("partnerIdTier");
		float amount = (float) 100.00;
		String description = "Test Redemption";
		strapiBody = null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, String.valueOf(amount), description);

		responseRD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");
		Thread.sleep(1000);
	}

	@Then("redemption of member for tier is successfull with status code {int}")
	public void redemption_of_member_for_tier_is_successfull_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseRD.getStatusCode());

		JsonPath jsonPathEvaluator = responseRD.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform get member tier information for downgrade")
	public void user_perform_get_member_tier_information_for_downgrade() throws InterruptedException {
		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/details");
		Thread.sleep(500);
	}

	@Then("member can see his tier downgrade detail succesfully with status code {int}")
	public void member_can_see_his_tier_downgrade_detail_succesfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

		// strBasic
		String strTier = jsonPathEvaluator.get("data.tier");
		Assert.assertEquals(strBasic, strTier);
	}
	/*-----Downgrade end------*/
	/*---points Normal----*/

	@Given("member post transaction for tier assessment")
	public void member_post_transaction_for_tier_assessment() {
		System.out.println("earn");
	}

	@When("user perform earn transaction for tier assessment of member")
	public void user_perform_earn_transaction_for_tier_assessment_of_member() throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		String price = "151";
		String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerIdTier");
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
		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("earn transaction for tier assessment of member is successfull with status code {int}")
	public void earn_transaction_for_tier_assessment_of_member_is_successfull_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEval = responseTX.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
	}

	@When("user run the tier assessment job")
	public void user_run_the_tier_assessment_job() throws InterruptedException {
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecQueryParm()).log().all()
				.when().get("/api/v10/loyaltyops/TierAssessment/schedule-now?schedule=%231");
		Thread.sleep(1000);
	}

	@Then("tier assessment job run sucessfully with status code {int}")
	public void tier_assessment_job_run_sucessfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@When("user again perform get member details")
	public void user_again_perform_get_member_details() throws InterruptedException {
		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/details");
		Thread.sleep(1000);
	}

	@Then("the member can see his tier detail succesfully with status code {int}")
	public void the_member_can_see_his_tier_detail_succesfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

		String strNewTier = jsonPathEvaluator.get("data.tier");
		Assert.assertEquals(strSilver, strNewTier);
	}

	/*------points end---*/

	/*----tier Upgrade-----*/

	@Given("member post transaction to upgrade member tier")
	public void member_post_transaction_to_upgrade_member_tier() {
		System.out.println("Upgrade member");
	}

	@When("user perform transaction for tier upgrade")
	public void user_perform_transaction_for_tier_upgrade() throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		String price = "100";
		String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerId");
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
		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("transaction of member for tier is successfull with status code {int}")
	public void transaction_of_member_for_tier_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user again perform transaction for tier upgrade")
	public void user_again_perform_transaction_for_tier_upgrade() throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		String price = "100";
		String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerId");
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
		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("next transaction of member for tier is successfull with status code {int}")
	public void next_transaction_of_member_for_tier_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform get member tier information for upgrade")
	public void user_perform_get_member_tier_information_for_upgrade() throws InterruptedException {
		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/details");
		Thread.sleep(1000);
	}

	@Then("member can see his tier upgrade detail succesfully with status code {int}")
	public void member_can_see_his_tier_upgrade_detail_succesfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

		// strBasic
		String strTier = jsonPathEvaluator.get("data.tier");
		Assert.assertEquals(strBasic, strTier);
	}
	/*------Tier upgrade end---*/

	/*------Tier upgrade final gold---*/

	@Given("member post transaction for member upgrade for final tier")
	public void member_post_transaction_for_member_upgrade_for_final_tier() {
		System.out.println("final tier");
	}

	@When("user perform transaction for member upgrade for final tier")
	public void user_perform_transaction_for_member_upgrade_for_final_tier() throws InterruptedException {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		String price = "1000";
		String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerIdTier");
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
		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}

	@Then("transaction of member for final tier is successfull with status code {int}")
	public void transaction_of_member_for_final_tier_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform get member tier information of final tier")
	public void user_perform_get_member_tier_information_of_final_tier() throws InterruptedException {
		sourceKey = getsourceKey();
		responseMD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/details");
		Thread.sleep(1000);
	}

	@Then("member can see his final tier status succesfully with status code {int}")
	public void member_can_see_his_final_tier_status_succesfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseMD.getStatusCode());

		JsonPath jsonPathEvaluator = responseMD.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	/*-----end-----*/
	@When("user perform get member details")
	public void user_perform_get_member_details() {
		sourceKey = getsourceKey();
		responseMD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + sourceKey + "/details");
	}

	@Then("member details should be displayed successfull with statuscode {int}")
	public void member_details_should_be_displayed_successfull_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseMD.getStatusCode());

		JsonPath jsonPathEvaluator = responseMD.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}
}
