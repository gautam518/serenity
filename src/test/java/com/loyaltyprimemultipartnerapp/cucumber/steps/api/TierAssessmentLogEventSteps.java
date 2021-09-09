package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

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

public class TierAssessmentLogEventSteps extends TestBase {

	String strapiBody;
	private String sourceKey;// source key
	private Response response = null; // Response
	private Response  responseTXT=null;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	@Given("user check for member tier assessment log")
	public void user_check_for_member_tier_assessment_log() {

	}

	@When("user perform member enrollmnet for tier assement log")
	public void user_perform_member_enrollmnet_for_tier_assement_log() {
		sourceKey = LoadProperties().getProperty("enkey") + "_SRBLN" + TestUtils.getRandomValue();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRBLN_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = _sourceKey;
		// _sourceLegalEt=strUserId;
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
	
	@Then("member enroll is successful for tier assement log")
	public void member_enroll_is_successful_for_tier_assement_log() {

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user perform earn transaction for tier assement log")
	public void user_perform_earn_transaction_for_tier_assement_log() throws InterruptedException {

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

	@Then("member transaction is succesful for tier assement log with status code {int}")
	public void member_transaction_is_succesful_for_tier_assement_log_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTXT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXT.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	
	@When("user perform perform event history for tier assement log")
	public void user_perform_perform_event_history_for_tier_assement_log() {

		String key = getsourceKey();
		response = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"membershipKey\", \"value\": \"" + key + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/loyaltyops/events/list");
	}

	@Then("member event detail is displayed successfully with status code {int}")
	public void member_event_detail_is_displayed_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

}
