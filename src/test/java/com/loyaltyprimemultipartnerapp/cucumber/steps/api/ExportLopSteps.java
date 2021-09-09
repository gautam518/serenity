package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class ExportLopSteps extends TestBase {
	String strapiBody;
	Response response = null;
	Response responseM = null;
	Response responseE = null;
	Response responseTx = null;
	Response responseTE= null;
	private String sourceKey;// source key
	String jsonFilePath;
	String MemberTemplate = "";
	String TxTemplate = "";

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	@Given("user check for member export")
	public void user_check_for_member_export() {

		System.out.println("user check for member export");
	}

	@When("user perform enrollment of a member for export")
	public void user_perform_enrollment_of_a_member_for_export() {

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

	@Then("member is enrolled successfully for export with status code {int}")
	public void member_is_enrolled_successfully_for_export_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);

	}

	@When("user perform export")
	public void user_perform_export() {

		String getCurrentDate = TestUtils.getDateTime();

		MemberTemplate = LoadProperties().getProperty("exportmemberTemplate");

		responseE = SerenityRest.given().queryParam("format", "JSON")
				.queryParam("filter", "[{\"property\":\"LastModified\", \"value\":\"" + getCurrentDate + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec().urlEncodingEnabled(true)).log().all().when()
				.get("/api/v10/exports/" + MemberTemplate + "");
	}

	@Then("export is successfull with status code {int}")
	public void export_is_successfull_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseE.getStatusCode());
		// JsonPath jsonPathEvaluator = responseE.jsonPath();
		// JsonPath jp = new JsonPath(responseE.asString());
	}

	@Given("user check for transaction export")
	public void user_check_for_transaction_export() {
		System.out.println("user check for transaction export");
	}

	@When("user perform transaction for export")
	public void user_perform_transaction_for_export() {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		String TrxDate = TestUtils.GetPointExpiryDateTime();
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
		responseTx = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Eror:"+ e.getMessage());
			e.printStackTrace();
		}
	}

	@Then("transaction is successfully for export with status code {int}")
	public void transaction_is_successfully_for_export_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTx.getStatusCode());
	}

	@When("user perform transaction export")
	public void user_perform_transaction_export() {

		String getCurrentDate = TestUtils.getDateTime();

		MemberTemplate = LoadProperties().getProperty("exporttransactionTemplate");

		responseTE = SerenityRest.given().queryParam("format", "JSON")
				.queryParam("filter", "[{\"property\":\"LastModified\", \"value\":\"" + getCurrentDate + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec().urlEncodingEnabled(true)).log().all().when()
				.get("/api/v10/exports/" + MemberTemplate + "");
	}

	@Then("transaction export is successfull with status code {int}")
	public void transaction_export_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTE.getStatusCode());
	}

}