package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.InputStream;
import java.util.Properties;

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

public class PartialTransactionSteps {

	String strapiBody;
	private String sourceKey;// source key
	private String TrxDate;
	private String TrxId;

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	Response responseSM;
	Response responseTX1;
	Response responseTX2;
	Response responseTX3;
	Response responseTX4;

	Response responseTX12;
	Response responseTX21;
	Response responseTX31;
	Response responseTX41;

	Response responseTX411;
	Response responseTX311;
	Response responseTX212;
	Response responseTX121;
	Response responseTX1212;
	Response responseTX2121;
	Response responseTX3112;
	Response responseTX4112;
	
	Response responseTXSNGP;
	Response responseTXIPT;
	Response responseTXMLT;
	Response responseTXMET;
	Response responseTXWPDT;

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

	@Given("Enroll a member for partial transaction")
	public void enroll_a_member_for_partial_transaction() {
		sourceKey = LoadProperties().getProperty("enkey") + "_SRCP" + TestUtils.getRandomValue();
	}

	@When("Member performs enrollment for partial transaction")
	public void member_performs_enrollment_for_partial_transaction() {
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRC_AltId" + TestUtils.getRandomValue();
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

	@Then("Member should be enroll successfully with status code {int}")
	public void member_should_be_enroll_successfully_with_status_code(Integer int1) {

		Assert.assertEquals(HttpStatus.SC_OK, responseSM.getStatusCode());

		JsonPath jsonPathEvaluator = responseSM.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("Member call the earn transaction for one valid product item")
	public void member_call_the_earn_transaction_for_one_valid_product_item() {

		sourceKey = sourceKey;
		System.out.println("MemberId:" + sourceKey);
	}

	@When("Member perfom earn transaction for one valid product item with assumeMissingProductItemsQualified as true")
	public void member_perfom_earn_transaction_for_one_valid_product_item_with_assumeMissingProductItemsQualified_as_true() {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX1 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Transaction with assumeMissingProductItemsQualified as true should be successfull with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_true_should_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX1.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX1.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("Member call the earn transaction for one invalid product item with assumeMissingProductItemsQualified as true")
	public void member_call_the_earn_transaction_for_one_invalid_product_item_with_assumeMissingProductItemsQualified_as_true() {

		sourceKey = sourceKey;
		System.out.println("MemberId:" + sourceKey);
	}

	@When("Member perfom earn transaction for one invalid product item with assumeMissingProductItemsQualified as true")
	public void member_perfom_earn_transaction_for_one_invalid_product_item_with_assumeMissingProductItemsQualified_as_true() {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = "XYZ"; // LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX2 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Transaction with assumeMissingProductItemsQualified as true should not be successfull with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_true_should_not_be_successfull_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), responseTX2.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX2.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Member call the earn transaction for multiple valid product item with assumeMissingProductItemsQualified as true")
	public void member_call_the_earn_transaction_for_multiple_valid_product_item_with_assumeMissingProductItemsQualified_as_true() {
		sourceKey = sourceKey;
		System.out.println("MemberId:" + sourceKey);

	}

	@When("Member perfom earn transaction for multiple valid product item with assumeMissingProductItemsQualified as true")
	public void member_perfom_earn_transaction_for_multiple_valid_product_item_with_assumeMissingProductItemsQualified_as_true() {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_productKey3");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX3 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Transaction with assumeMissingProductItemsQualified as true should be successfull for multiple products with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_true_should_be_successfull_for_multiple_products_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX3.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX3.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("Member call the earn transaction for mixture of existing and non-existing product item with assumeMissingProductItemsQualified as true")
	public void member_call_the_earn_transaction_for_mixture_of_existing_and_non_existing_product_item_with_assumeMissingProductItemsQualified_as_true() {

		sourceKey = sourceKey;
		System.out.println("MemberId:" + sourceKey);
	}

	@When("Member perfom earn transaction for mixture of existing and non-existing product item with assumeMissingProductItemsQualified as true")
	public void member_perfom_earn_transaction_for_mixture_of_existing_and_non_existing_product_item_with_assumeMissingProductItemsQualified_as_true() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_noproduct");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX4 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}
	
	@Then("Transaction with assumeMissingProductItemsQualified as true should be successfull for mixture product with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_true_should_be_successfull_for_mixture_product_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseTX4.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX4.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	
	@When("Member perfom earn transaction for one valid product item with assumeMissingProductItemsQualified as false")
	public void member_perfom_earn_transaction_for_one_valid_product_item_with_assumeMissingProductItemsQualified_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX12 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Transaction with assumeMissingProductItemsQualified as false for one existing product should be successfull with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_false_for_one_existing_product_should_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX12.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX12.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("Member call the earn transaction for one invalid product item")
	public void member_call_the_earn_transaction_for_one_invalid_product_item() {
		sourceKey = getsourceKey();
		System.out.println("The Value is:" + sourceKey);
	}

	@When("Member perfom earn transaction for one invalid product item with assumeMissingProductItemsQualified as false")
	public void member_perfom_earn_transaction_for_one_invalid_product_item_with_assumeMissingProductItemsQualified_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = "TestXYZ"; // LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX21 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Transaction with assumeMissingProductItemsQualified as false for single invalid product should not be successfull with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_false_for_single_invalid_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX21.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX21.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Member call the earn transaction for multiple valid product item")
	public void member_call_the_earn_transaction_for_multiple_valid_product_item() {

		sourceKey = getsourceKey();
		System.out.println("The Value is:" + sourceKey);
	}

	@When("Member perfom earn transaction for multiple valid product item with assumeMissingProductItemsQualified as false")
	public void member_perfom_earn_transaction_for_multiple_valid_product_item_with_assumeMissingProductItemsQualified_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_productKey3");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = getsourceKey();
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX31 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");
	}

	@Then("Transaction with assumeMissingProductItemsQualified as false for multiple valid product should not be successfull with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_false_for_multiple_valid_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX31.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX31.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@Given("Member call the earn transaction for mixture of existing and non-existing product item")
	public void member_call_the_earn_transaction_for_mixture_of_existing_and_non_existing_product_item() {

		sourceKey = getsourceKey();
		System.out.println("The Value is:" + sourceKey);
	}

	@When("Member perfom earn transaction for mixture of existing and non-existing product item with assumeMissingProductItemsQualified as false")
	public void member_perfom_earn_transaction_for_mixture_of_existing_and_non_existing_product_item_with_assumeMissingProductItemsQualified_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_noproduct");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX41 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

	}

	@Then("Transaction with assumeMissingProductItemsQualified as false for mixture of existing and non-existing product should not be successfull with status code {int}")
	public void transaction_with_assumeMissingProductItemsQualified_as_false_for_mixture_of_existing_and_non_existing_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX31.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX31.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@When("Member perfom earn transaction for one valid product item with IgnoreMissingProductItems as true")
	public void member_perfom_earn_transaction_for_one_valid_product_item_with_IgnoreMissingProductItems_as_true() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX121 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction with IgnoreMissingProductItems as true for one existing product should be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_true_for_one_existing_product_should_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX121.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX121.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member perfom earn transaction for one invalid product item with IgnoreMissingProductItems as true")
	public void member_perfom_earn_transaction_for_one_invalid_product_item_with_IgnoreMissingProductItems_as_true() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = "XYZ"; // LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX212 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction with IgnoreMissingProductItems as true for single invalid product should not be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_true_for_single_invalid_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX212.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX212.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@When("Member perfom earn transaction for multiple valid product item with IgnoreMissingProductItems as true")
	public void member_perfom_earn_transaction_for_multiple_valid_product_item_with_IgnoreMissingProductItems_as_true() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_productKey3");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX311 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction with IgnoreMissingProductItems as true for multiple valid product should not be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_true_for_multiple_valid_product_should_not_be_successfull_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), responseTX311.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX311.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);

	}

	@When("Member perfom earn transaction for mixture of existing and non-existing product item with IgnoreMissingProductItems as true")
	public void member_perfom_earn_transaction_for_mixture_of_existing_and_non_existing_product_item_with_IgnoreMissingProductItems_as_true() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_noproduct");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX411 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction with IgnoreMissingProductItems as true for mixture of existing and non-existing product should not be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_true_for_mixture_of_existing_and_non_existing_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX411.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX411.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@When("Member perfom earn transaction for one valid product item with IgnoreMissingProductItems as false")
	public void member_perfom_earn_transaction_for_one_valid_product_item_with_IgnoreMissingProductItems_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1,partnerId);
		responseTX1212 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction with IgnoreMissingProductItems as false for one existing product should be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_false_for_one_existing_product_should_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX1212.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX1212.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

	}

	@When("Member perfom earn transaction for one invalid product item with IgnoreMissingProductItems as false")
	public void member_perfom_earn_transaction_for_one_invalid_product_item_with_IgnoreMissingProductItems_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = "XYZ"; // LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");

		responseTX2121 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");

	}

	@Then("Transaction with IgnoreMissingProductItems as false for single invalid product should not be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_false_for_single_invalid_product_should_not_be_successfull_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), responseTX2121.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX2121.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);

	}

	@When("Member perfom earn transaction for multiple valid product item with IgnoreMissingProductItems as false")
	public void member_perfom_earn_transaction_for_multiple_valid_product_item_with_IgnoreMissingProductItems_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_productKey3");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX3112 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");

	}

	@Then("Transaction with IgnoreMissingProductItems as false for multiple valid product should not be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_false_for_multiple_valid_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX3112.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX3112.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);

	}

	@When("Member perfom earn transaction for mixture of existing and non-existing product item with IgnoreMissingProductItems as false")
	public void member_perfom_earn_transaction_for_mixture_of_existing_and_non_existing_product_item_with_IgnoreMissingProductItems_as_false() {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_noproduct");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTX4112 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");

	}

	@Then("Transaction with IgnoreMissingProductItems as false for mixture of existing and non-existing product should not be successfull with status code {int}")
	public void transaction_with_IgnoreMissingProductItems_as_false_for_mixture_of_existing_and_non_existing_product_should_not_be_successfull_with_status_code(
			Integer code) {

		Assert.assertEquals(code.intValue(), responseTX4112.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX4112.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);

	}
	
	@When("Member perfom earn transaction for one valid product item for TxEvaluator")
	public void member_perfom_earn_transaction_for_one_valid_product_item_for_TxEvaluator() {
	 
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, partnerId);
		responseTXSNGP = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction should be successfull with status code {int}")
	public void transaction_should_be_successfull_with_status_code(Integer code) {
	   
		Assert.assertEquals(code.intValue(), responseTXSNGP.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXSNGP.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("Member perfom earn transaction for one invalid product item for TxEvaluator")
	public void member_perfom_earn_transaction_for_one_invalid_product_item_for_TxEvaluator() {
	    
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = "XYZ"; // LoadProperties().getProperty("productKey");
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, partnerId);

		responseTXIPT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
		
	}

	@Then("Transaction with TxEvaluator should not be successfull with status code {int}")
	public void transaction_with_TxEvaluator_should_not_be_successfull_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseTXIPT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXIPT.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@When("Member perfom earn transaction for multiple valid product item for TxEvaluator")
	public void member_perfom_earn_transaction_for_multiple_valid_product_item_for_TxEvaluator() {
	    
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_productKey3");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTXMLT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction with TxEvaluator should be successfull for multiple products with status code {int}")
	public void transaction_with_TxEvaluator_should_be_successfull_for_multiple_products_with_status_code(Integer code) {
	  
		Assert.assertEquals(code.intValue(), responseTXMLT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXMLT.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@When("Member perfom earn transaction for mixture of existing and non-existing product item")
	public void member_perfom_earn_transaction_for_mixture_of_existing_and_non_existing_product_item() {
	  
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = LoadProperties().getProperty("partial_productKey1");
		String channelKey1 = LoadProperties().getProperty("partial_channelKey");
		String productKey2 = LoadProperties().getProperty("partial_productKey2");
		String channelKey2 = LoadProperties().getProperty("partial_channelKey");
		String productKey3 = LoadProperties().getProperty("partial_noproduct");
		String channelKey3 = LoadProperties().getProperty("partial_channelKey");

		String clientKey = sourceKey;
		String partnerId=LoadProperties().getProperty("partnerId");

		AIPsJsonBody apibody = new AIPsJsonBody();

		strapiBody = apibody.TransactionAPIBody(TrxId, clientKey, TrxDate,partnerId, quantity, price, productKey1, channelKey1,
				productKey2, channelKey2, productKey3, channelKey3);

		responseTXMET = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
		
	}

	@Then("Transaction with TxEvaluator should be successfull for mixture product with status code {int}")
	public void transaction_with_TxEvaluator_should_be_successfull_for_mixture_product_with_status_code(Integer code) {
	  
		Assert.assertEquals(code.intValue(), responseTXMET.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXMET.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}
	
	@When("Member perfom earn transaction without passing product key")
	public void member_perfom_earn_transaction_without_passing_product_key() {
	    
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = LoadProperties().getProperty("date");
		String price = LoadProperties().getProperty("price");
		String quantity = LoadProperties().getProperty("quantity");
		String productKey1 = "without";
		String channelKey1 = LoadProperties().getProperty("channelKey");

		String clientKey = sourceKey;

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price, productKey1,
				channelKey1, "");
		
		
		responseTXWPDT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody)
				.log().all().when().post("/api/v10/tx");
	}

	@Then("Transaction without passing product key should be successfull with status code {int}")
	public void transaction_without_passing_product_key_should_be_successfull_with_status_code(Integer code) {
	 
		Assert.assertEquals(code.intValue(), responseTXWPDT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTXWPDT.jsonPath();

		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}
	
}
