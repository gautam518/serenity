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
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;

public class TransactionIntegrationSteps {
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

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

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

	public static String get_TrxIdhist() {
		return _TrxIdhist;
	}

	private static String _TrxIdhist;

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

	@When("User perform enrollment for a member")
	public void user_perform_enrollment_for_a_member() {

		clientKey = LoadProperties().getProperty("enkey") + "_SRC" + TestUtils.getRandomValue();
		_sourceKey = clientKey;

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

	@Then("Member should be successfully enroll in the system with status code {int}")
	public void member_should_be_successfully_enroll_in_the_system_with_status_code(Integer int1) {
		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("the member perfom earn transaction in the system with details MembereTrx and rownumber {int}")
	public void the_member_perfom_earn_transaction_in_the_system_with_details_MembereTrx_and_rownumber(
			Integer rownumber) throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"MemberenrollTrx");
		String strTxKey = "TXINT";
		TrxId = strTxKey + "_" + TestUtils.getRandomValue();
		String partnerId = LoadProperties().getProperty("partnerId");
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

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey,
				channelkey, partnerId);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(10000);
	}

	@Then("Member should get points successfull with status code {int}")
	public void member_should_get_points_successfull_with_status_code(Integer statuscode) throws InterruptedException {
		// check success status
		Assert.assertEquals(statuscode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		Thread.sleep(1000);
	}

	@Then("The transaction detail should be displayed in member trasaction history")
	public void the_transaction_detail_should_be_displayed_in_member_trasaction_history() {
		String partnerId = LoadProperties().getProperty("partnerId");
		TrxId = getTrxId() + "_" + partnerId;
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

	@Then("The transaction reward history should be displayed")
	public void the_transaction_reward_history_should_be_displayed() {
		/// Check the member rewardsHistory
		String partnerId = LoadProperties().getProperty("partnerId");
		TrxId = getTrxId() + "_" + partnerId;
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

	@When("The member perfom the transaction with price{int} and quantity{int}")
	public void the_member_perfom_the_transaction_with_price_and_quantity(Integer _price, Integer _quantity)
			throws InterruptedException {
		String partnerId = LoadProperties().getProperty("partnerId");
		String price = _price.toString();
		String quantity = _quantity.toString();

		TrxId = "Trns_" + "_" + TestUtils.getRandomValue();

		TrxDate = TestUtils.getDateTime();
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
		Thread.sleep(10000);
	}

	@Then("The the transaction should be succesful and points recived should be rewardpoints{int} and campaigntype{string}")
	public void the_the_transaction_should_be_succesful_and_points_recived_should_be_rewardpoints_and_campaigntype(
			Integer rewardpoints, String campaigntype) {

		String responseBody = response.getBody().toString();
		System.out.println("responseBody --->" + responseBody);

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		String getBonusPointsType = jsonPathEvaluator.get("data.rewards.BONUS.pointsType");
		System.out.println("Get Bonus PointsType from Response " + getBonusPointsType);
		Assert.assertEquals(getBonusPointsType, campaigntype);

		Float getTotalBonusPoints = jsonPathEvaluator.get("data.rewards.BONUS.total");
		System.out.println("Get Bonus Points in Response " + getTotalBonusPoints.toString());
		Assert.assertEquals(getTotalBonusPoints.intValue(), rewardpoints.intValue());
	}

	@When("Member perfom the transaction with price{int} and quantity{int}")
	public void member_perfom_the_transaction_with_price_and_quantity(Integer strprice, Integer strquantity) {

		String partnerId = LoadProperties().getProperty("partnerId");
		String price = strprice.toString();
		String quantity = strquantity.toString();
		TrxId = "TXINGS_" + "_" + TestUtils.getRandomValue();
		_TrxIdval = TrxId;
		TrxDate = TestUtils.getDateTime();
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
	}

	@Then("Member should get points successfull with status code <status_code>")
	public void member_should_get_points_successfull_with_status_code_status_code() {
		// check success status
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
	}

	@Then("The transaction detail should be displayed in member deposit statement")
	public void the_transaction_detail_should_be_displayed_in_member_deposit_statement() throws InterruptedException {

		clientKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		// Txid
		TrxId = getTrxIdval();

		// match the redemption id here
		String getRedemId = null;
		List aList = response.jsonPath().getList("data.bookings.receiptId.uniqueId");
		for (int i = 0; i < aList.size(); i++) {
			if (aList.get(i).equals(TrxId)) {
				getRedemId = aList.get(i).toString();
				System.out.println("Statement TrxId value" + getRedemId);
			}
		}
		// Assert.assertEquals(getRedemId, TrxId);
		Thread.sleep(10000);
	}

	@Given("Member call the deposit statement to check balance")
	public void member_call_the_deposit_statement_to_check_balance() {
		clientKey = getsourceKey();
		System.out.println("client key :" + clientKey);
	}

	@When("Member perfom deposit statement before transaction")
	public void member_perfom_deposit_statement_before_transaction() throws InterruptedException {

		responseBFTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Thread.sleep(5000);
	}

	@Then("user can see the member balance befor transction successfully")
	public void user_can_see_the_member_balance_befor_transction_successfully() {
		Assert.assertEquals(HttpStatus.SC_OK, responseBFTX.getStatusCode());
		JsonPath jsonPathEval = responseBFTX.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);

		float getMemberBalance = (Float) jsonPathEval.get("data.balance");
		_MBalance = getMemberBalance;

	}

	@When("Member perfom the transaction for price{int} and quantity{int}")
	public void member_perfom_the_transaction_for_price_and_quantity(Integer strPrice, Integer strQuantity)
			throws InterruptedException {
		response = null;
		String partnerId = LoadProperties().getProperty("partnerId");
		String price = strPrice.toString();
		String quantity = strQuantity.toString();
		TrxId = "Trns_" + "_" + TestUtils.getRandomValue();
		_TrxIdval = TrxId;
		TrxDate = TestUtils.getDateTime();
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
		Thread.sleep(5000);
	}

	@Then("The Member transaction is successfull with status code {int}")
	public void the_Member_transaction_is_successfull_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@When("Member check deposit statement after transaction")
	public void member_check_deposit_statement_after_transaction() throws InterruptedException {
		responseAFTTRX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Thread.sleep(5000);
	}

	@Then("The member updated balance should be displayed in the deposit statement")
	public void the_member_updated_balance_should_be_displayed_in_the_deposit_statement() throws InterruptedException {

		TrxId = getTrxIdval();
		float finalAmountShouldBe = 116;// This will be tha total balance
		// get the balance befor transactionfloat getAmountRedem = getMBalance();
		float Total = finalAmountShouldBe;
		System.out.println("Balance should be :" + Total);
		// Balance check end here

		Assert.assertEquals(HttpStatus.SC_OK, responseAFTTRX.getStatusCode());

		JsonPath jsonEvl = responseAFTTRX.jsonPath();
		float _getMemberNewBalance;
		_getMemberNewBalance = (Float) jsonEvl.get("data.balance");
		// System.out.println("Member balance after transaction :" +
		// _getMemberNewBalance);

		Assert.assertEquals(Float.toString(_getMemberNewBalance), Float.toString(Total));
		Thread.sleep(5000);
	}

}
