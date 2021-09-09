package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

public class ImportLopSteps extends TestBase {
	String jsonFilePath;
	private Response response = null; // Response
	private Response responseME = null; // Response
	private Response responseTX = null; // Response
	private Response responsePD = null; // Response
	private Response responseV = null; // Response
	private Response responseVT = null; // Response
	private Response responseM=null;
	private Response responseCard= null; // Response

	private String importType_E = "Enrollment";
	private String importType_T = "Tx";
	private String importType_P = "Product";
	private String importType_VType = "VoucherType";
	private String importVoucher = "Voucher";
	String clientKey;
	String strapiBody;
	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	public static String get_sourceLegalEt() {
		return _sourceLegalEt;
	}

	private static String _sourceLegalEt;
	
	
	
	@Given("user check for bulk import of member json file")
	public void user_check_for_bulk_import_of_member_json_file() {
		System.out.println("Check import ");
	}

	@When("user perform the bulk import of member json file")
	public void user_perform_the_bulk_import_of_member_json_file() throws InterruptedException {

		
		jsonFilePath = LoadProperties().getProperty("validJsonfile");
		File jsonDataInFile = new File(jsonFilePath);
		responseME = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile())
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/"+ importType_E+"");
	

		Thread.sleep(20000);
	}

	
	@Then("bulk import of member json file is successfully with status code {int}")
	public void bulk_import_of_member_json_file_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseME.getStatusCode());

		JsonPath jsonPathEvaluator = responseME.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Success response of API " + getStatus);

		String strImportType = jsonPathEvaluator.get("data.importType");
		Assert.assertEquals(importType_E, strImportType);

	}

	@Then("member bulk import file records exists")
	public void member_bulk_import_file_records_exists() throws FileNotFoundException, IOException, ParseException {
		String jsonFilePath = LoadProperties().getProperty("validJsonfile");

		String memberId = ReadJsonFile(jsonFilePath);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/memberships/" + memberId + "/details");

		Assert.assertEquals(200, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

		String getKey = jsonPathEvaluator.get("data.key");
		System.out.println("Get Key: " + getKey);

		Assert.assertEquals(memberId, getKey);
	}

	@Given("user check for bulk import of transaction json file")
	public void user_check_for_bulk_import_of_transaction_json_file() {
		System.out.println("Check for transaction import");
	}

	@When("user perform the bulk import of transaction json file")
	public void user_perform_the_bulk_import_of_transaction_json_file() throws InterruptedException {

		jsonFilePath = LoadProperties().getProperty("validTxJsonfile");
		File jsonDataInFile = new File(jsonFilePath);

		responseTX = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile())
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType_T + "");

		Thread.sleep(20000);
	}

	@Then("bulk import of transaction json file is successfully with status code {int}")
	public void bulk_import_of_transaction_json_file_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEvaluator = responseTX.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

	}

	@When("user perform the transaction history")
	public void user_perform_the_transaction_history() throws FileNotFoundException, IOException, ParseException {

		String jsonFilePath = LoadProperties().getProperty("validTxJsonfile");
		System.out.println("jsonFilePath " + jsonFilePath);
		String gettxId = ReadJsonFileTxId(jsonFilePath);
		System.out.println("gettxId " + gettxId);
		/*
		 * String partnerId = ReadJsonFile(jsonFilePath, "partnerId");
		 * System.out.println("partnerId " + partnerId); String txId = memberId + "_" +
		 * partnerId;
		 */
		String txId = null;
		response = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"txId\", \"value\": \"" + txId + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/tx");
	}

	

	@Then("transaction history is displayed successfull with transaction detail")
	public void transaction_history_is_displayed_successfull_with_transaction_detail() {
		Assert.assertEquals(200, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);

	}

	@Given("user check for bulk import of product json file")
	public void user_check_for_bulk_import_of_product_json_file() {
		System.out.println("product information");
	}

	@When("user perform the bulk import of product json file")
	public void user_perform_the_bulk_import_of_product_json_file() throws InterruptedException {
		jsonFilePath = LoadProperties().getProperty("validProductPath");
		File jsonDataInFile = new File(jsonFilePath);

		responsePD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile())
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType_P + "");

		Thread.sleep(20000);
	}

	@When("user perform the products detail")
	public void user_perform_the_products_detail() throws FileNotFoundException, IOException, ParseException {
		String jsonFilePath = LoadProperties().getProperty("validProductPath");
		System.out.println("jsonFilePath " + jsonFilePath);
		String getPId = ReadJsonFile(jsonFilePath);
		System.out.println("getPId " + getPId);
		response = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"key\", \"value\": \"" + getPId + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/promos/product/data");
	}

	@Then("bulk import of product json file is successfully with status code {int}")
	public void bulk_import_of_product_json_file_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responsePD.getStatusCode());

		JsonPath jsonPathEvaluator = responsePD.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Success response of API " + getStatus);

		String strImportType = jsonPathEvaluator.get("data.importType");
		Assert.assertEquals(importType_P, strImportType);
	}

	@Then("product information is displayed successfully with status code {int}")
	public void product_information_is_displayed_successfully_with_status_code(Integer int1) {
		Assert.assertEquals(200, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("user check for bulk import of voucher type json file")
	public void user_check_for_bulk_import_of_voucher_type_json_file() {
		System.out.println("print voucher");
	}

	@When("user perform the bulk import of voucher type json file")
	public void user_perform_the_bulk_import_of_voucher_type_json_file() throws InterruptedException {
		jsonFilePath = LoadProperties().getProperty("validVoucherTypePath");
		File jsonDataInFile = new File(jsonFilePath);

		responseVT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile())
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType_VType + "");

		Thread.sleep(20000);
	}

	@Then("voucher type bulk import of json file is successfully with status code {int}")
	public void voucher_type_bulk_import_of_json_file_is_successfully_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), responseVT.getStatusCode());

		JsonPath jsonPathEvaluator = responseVT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Success response of API " + getStatus);

		String strImportType = jsonPathEvaluator.get("data.importType");
		Assert.assertEquals(importType_VType, strImportType);
	}

	@Given("user check for bulk import of voucher json file")
	public void user_check_for_bulk_import_of_voucher_json_file() {
		System.out.println("vouchers");
	}

	@When("user perform the bulk import of voucher json file")
	public void user_perform_the_bulk_import_of_voucher_json_file() throws InterruptedException {
		jsonFilePath = LoadProperties().getProperty("validVoucherPath");
		File jsonDataInFile = new File(jsonFilePath);

		responseV = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecJsonFile())
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importVoucher +"");

		Thread.sleep(20000);
	}
	
	@Then("bulk import of voucher json file is successfully with status code {int}")
	public void bulk_import_of_voucher_json_file_is_successfully_with_status_code(Integer code) {
		
		Assert.assertEquals(code.intValue(), responseV.getStatusCode());

		JsonPath jsonPathEvaluator = responseV.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		
		String strImportType = jsonPathEvaluator.get("data.importType");
		Assert.assertEquals(importVoucher, strImportType);
	}
	
	@Given("user check for bulk import of card json file")
	public void user_check_for_bulk_import_of_card_json_file() {
	   
	}
	@When("user perform create legal entity for member card")
	public void user_perform_create_legal_entity_for_member_card() {
	    
		String key = TestUtils.getRandomValue() + "_Card";
		_sourceLegalEt=key;
		String batchId=TestUtils.getRandomValue()+"_JsonLegalEntity";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.BulkImportLegalEntity(key);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/imports/LegalEntity?batchId="+batchId+"");
	}

	@Then("legal entity for member is created successfully with status code {int}")
	public void legal_entity_for_member_is_created_successfully_with_status_code(Integer code) {
		Assert.assertEquals(200, response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("enroll a member for card import")
	public void enroll_a_member_for_card_import() {
		
		clientKey = LoadProperties().getProperty("enkey") + "_SRIMP" + TestUtils.getRandomValue();
		_sourceKey = clientKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRIMP_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = get_sourceLegalEt();
		//_sourceLegalEt=strUserId;
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

		responseM = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}
	
	@Then("member ernrollment is successful with status code {int}")
	public void member_ernrollment_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(200, responseM.getStatusCode());

		JsonPath jsonPathEvaluator = responseM.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform the bulk import of card json file")
	public void user_perform_the_bulk_import_of_card_json_file() {

		String key = TestUtils.getRandomValue() + "_Card";
		String Name = "CardTestJson";
		String membershipKey = getsourceKey();
		String legalEntityKey = get_sourceLegalEt();
		String validSince = TestUtils.getDateTime();
		String batchId=TestUtils.getRandomValue()+"_JsonCard";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.BulkImportCard(key, Name, membershipKey, legalEntityKey, validSince);

		responseCard = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/imports/Card?batchId="+batchId+"");
	}

	@Then("bulk import of card json file is successfully with status code {int}")
	public void bulk_import_of_card_json_file_is_successfully_with_status_code(Integer code) {
		Assert.assertEquals(200, responseCard.getStatusCode());

		JsonPath jsonPathEvaluator = responseCard.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	public String ReadJsonFileTxId(String filePath) throws FileNotFoundException, IOException, ParseException {
		String name = null;
		// JSONParser parser = new JSONParser();
		JSONParser parser = new JSONParser();
		FileReader fileReader = new FileReader(filePath);
		try {
			Object obj = parser.parse(fileReader);
			JSONObject jsonObject = (JSONObject) obj;
			name = (String) jsonObject.get("txId");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public String ReadJsonFile(String filePath) throws FileNotFoundException, IOException, ParseException {

		JSONParser parser = new JSONParser();
		FileReader fileReader = new FileReader(filePath);

		JSONArray jsonArray = (JSONArray) parser.parse(fileReader);

		JSONObject json = (JSONObject) jsonArray.get(0);
		String Key = (String) json.get("Key");
		return Key;
	}


}
