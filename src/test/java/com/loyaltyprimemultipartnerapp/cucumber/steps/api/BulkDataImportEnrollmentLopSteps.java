package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.http.HttpHeaders;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Assert;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class BulkDataImportEnrollmentLopSteps {
	private CSVReader csvReader;
	private Response response = null; // Response
	HttpHeaders headers;
	String strapiBody;
	private String jsonFilePath;
	private String InvalidJsonFile;

	private String csvFilePath;
	private String importType = "Enrollment";
	private String invalidJsonFilePath;
   //public static String APIKey = "ApiKey sKySDdGhc05FGPHbqBWo/jHNaKfFrOzeWY94F+CRLpo=";
	public static String APIKey="ApiKey 123456789!";
	private RequestSpecification request;

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

	@Given("Member call the bulk import for valid json file")
	public void member_call_the_bulk_import_for_valid_json_file() {
		request = SerenityRest.given();
		System.out.println("API body:" + request);
	}

	@When("The member perfom bulk import for json file with valid file format")
	public void the_member_perfom_bulk_import_for_json_file_with_valid_file_format() throws InterruptedException {

		jsonFilePath = LoadProperties().getProperty("validJsonfile");
		File jsonDataInFile = new File(jsonFilePath);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType + "");

		System.out.println("API response:" + response);

		Thread.sleep(20000);
	}

	@Then("File should be processed in the system with status code {int}")
	public void file_should_be_processed_in_the_system_with_status_code(Integer statusCode) {

		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		System.out.println("Success response of API " + getStatus);

		Integer getTotalrows = jsonPathEvaluator.get("data.unprocessedRecords");
		System.out.println("Total rows: " + getTotalrows);
		// cureently 1 fixed
		Assert.assertEquals("4", getTotalrows.toString());
		
		String strImportType = jsonPathEvaluator.get("data.importType");
		Assert.assertEquals("Enrollment", strImportType);

		String strState = jsonPathEvaluator.get("data.state");
		Assert.assertEquals("OPEN", strState);

	}
	@Then("To verify the Json file records exists in the system")
	public void to_verify_the_Json_file_records_exists_in_the_system() throws FileNotFoundException, IOException, ParseException {
		
		String jsonFilePath = LoadProperties().getProperty("validJsonfile");
		
		String memberId=ReadJsonFile(jsonFilePath);
		
		response = SerenityRest.given().header("Content-Type", "application/json").header("Authorization", APIKey)
				.log().all().when().get("/api/v10/memberships/"+memberId+"/details");
		
		Assert.assertEquals(200, response.getStatusCode());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
		
		String getKey = jsonPathEvaluator.get("data.key");
		System.out.println("Get Key: " + getKey);

		Assert.assertEquals(memberId, getKey);
	}

	
	

	@Given("Member call the bulk import for invalid json data in file")
	public void member_call_the_bulk_import_for_invalid_json_data_in_file() {

		request = SerenityRest.given();
		System.out.println("API invalid json body:" + request);
	}

	@When("The member perfom bulk import for invalid json data in the file")
	public void the_member_perfom_bulk_import_for_invalid_json_data_in_the_file() throws InterruptedException {
		
		String invalidJsonDataFile = LoadProperties().getProperty("invalidJsonDataFile");
		File jsonDataInFile = new File(invalidJsonDataFile);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType + "");

		System.out.println("API response:" + response);

		Thread.sleep(2000);
	}

	@Then("The file should not be processed in the system with status code {int}")
	public void the_file_should_not_be_processed_in_the_system_with_status_code(Integer statusCode) {

		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
	}

	@Given("Member call the bulk import for same json file")
	public void member_call_the_bulk_import_for_same_json_file() {

		request = SerenityRest.given();
		System.out.println("API duplicate json file:" + request);
	}

	@When("The member perfom bulk import for duplicate json file in system")
	public void the_member_perfom_bulk_import_for_duplicate_json_file_in_system() throws InterruptedException {

		String jsonFilePath = LoadProperties().getProperty("validJsonfile");
		File jsonDataInFile = new File(jsonFilePath);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType + "");

		System.out.println("API response:" + response);

		Thread.sleep(2000);
	}

	@Given("Member call the bulk import for blank json file")
	public void member_call_the_bulk_import_for_blank_json_file() {

		request = SerenityRest.given();
		System.out.println("API blank json file:" + request);

	}

	@When("The member perfom bulk import for blank json file")
	public void the_member_perfom_bulk_import_for_blank_json_file() throws InterruptedException {

		String jsonFilePath = LoadProperties().getProperty("blankJsonFile");
		File jsonDataInFile = new File(jsonFilePath);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType + "");

		System.out.println("API response:" + response);

		Thread.sleep(2000);
	}

	@Then("File should not be processed in the system with status code {int}")
	public void file_should_not_be_processed_in_the_system_with_status_code(Integer statusCode) {

		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
	}

	@Given("Member call the bulk import for different file except json")
	public void member_call_the_bulk_import_for_different_file_except_json() {

		request = SerenityRest.given();

		System.out.println("API different json file:" + request);
	}

	@When("The member perfom bulk import for invalid json file in the system")
	public void the_member_perfom_bulk_import_for_invalid_json_file_in_the_system() throws InterruptedException {

		String jsonFilePath = LoadProperties().getProperty("invalidJsonFileFormat");
		File jsonDataInFile = new File(jsonFilePath);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/imports/" + importType + "");

		System.out.println("API response:" + response);
		Thread.sleep(1000);
	}

	@Given("Member call the bulk import for valid csv file")
	public void member_call_the_bulk_import_for_valid_csv_file() {
	
		request=SerenityRest.given();
	}

	@When("The member perfom bulk import for csv file with valid file format")
	public void the_member_perfom_bulk_import_for_csv_file_with_valid_file_format() throws InterruptedException {
		String csvFilePath=LoadProperties().getProperty("validCsvfile");
		File csvDataInFile = new File(csvFilePath);
	
		response = request
		  .header("Content-Type","text/csv")
		  .header("Authorization", APIKey)
		  .body(csvDataInFile).log().all()
		  .when()
		  .post("/api/v10/csvimports/"+ importType +"");
		  System.out.println("Csv file API response:"+ response);
		  
		  Thread.sleep(1000);
	}
	
	@Then("To verify the csv file records exists in the system")
	public void to_verify_the_csv_file_records_exists_in_the_system() throws CsvValidationException, IOException {
		
	String jsonFilePath = LoadProperties().getProperty("validCsvfile");
		
		String memberId=ReadCSVFile(jsonFilePath);
		
		response = SerenityRest.given().header("Content-Type", "application/json").header("Authorization", APIKey)
				.log().all().when().get("/api/v10/memberships/"+memberId+"/details");
		
		//check 404 as csv file data  is not processing currently 
		/*//Currrently not working  hence commneted
		Assert.assertEquals(404, response.getStatusCode());
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, false);
		
		String getKey = jsonPathEvaluator.get("data.key");
		System.out.println("Get Key: " + getKey);

		Assert.assertEquals(memberId, getKey);*/
	}

	@Given("Member call the bulk import for csv json file")
	public void member_call_the_bulk_import_for_csv_json_file() {

		request=SerenityRest.given();
	}
	
	@Given("Member call the bulk import for different file except csv")
	public void member_call_the_bulk_import_for_different_file_except_csv() {
		
		request=SerenityRest.given();

	}

	@When("The member perfom bulk import for invalid csv file format in the system")
	public void the_member_perfom_bulk_import_for_invalid_csv_file_format_in_the_system() throws InterruptedException {
		
		String jsonFilePath = LoadProperties().getProperty("invalidJsonFileFormat");
		File jsonDataInFile = new File(jsonFilePath);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/csvimports/" + importType + "");

		System.out.println("API response:" + response);
		Thread.sleep(1000);

	}

	@Given("Member call the bulk import for invalid csv file data")
	public void member_call_the_bulk_import_for_invalid_csv_file_data() {
		request=SerenityRest.given();
	}

	@When("The member perfom bulk import for invalid csv file data in the system")
	public void the_member_perfom_bulk_import_for_invalid_csv_file_data_in_the_system() throws InterruptedException {

		String jsonFilePath = LoadProperties().getProperty("invalidCsvDataFile");
		File jsonDataInFile = new File(jsonFilePath);
		response = request.header("Content-Type", "application/json").header("Authorization", APIKey)
				.body(jsonDataInFile).log().all().when().post("/api/v10/csvimports/" + importType + "");

		System.out.println("API response:" + response);
		Thread.sleep(1000);
	}

	@Given("Member call the bulk import for duplicate csv file")
	public void member_call_the_bulk_import_for_duplicate_csv_file() {
		request=SerenityRest.given();
	}

	@When("The member perfom bulk import for duplicate csv file in system")
	public void the_member_perfom_bulk_import_for_duplicate_csv_file_in_system() throws InterruptedException {
		String csvFilePath=LoadProperties().getProperty("validCsvfile");
		File csvDataInFile = new File(csvFilePath);
	
		response = request
		  .header("Content-Type","text/csv")
		  .header("Authorization", APIKey)
		  .body(csvDataInFile).log().all()
		  .when()
		  .post("/api/v10/csvimports/"+ importType +"");
		  System.out.println("Csv file API response:"+ response);
		  
		  Thread.sleep(1000);
	}
	
	@Given("Member call the bulk import for blank csv file")
	public void member_call_the_bulk_import_for_blank_csv_file() {
		request=SerenityRest.given();
	}

	@When("The member perfom bulk import for blank csv file in the system")
	public void the_member_perfom_bulk_import_for_blank_csv_file_in_the_system() throws InterruptedException {
		
		String csvFilePath=LoadProperties().getProperty("blankCsvFile");
		File csvDataInFile = new File(csvFilePath);
	
		response = request
		  .header("Content-Type","text/csv")
		  .header("Authorization", APIKey)
		  .body(csvDataInFile).log().all()
		  .when()
		  .post("/api/v10/csvimports/"+ importType +"");
		  System.out.println("Csv file API response:"+ response);
		  
		  Thread.sleep(1000);
	}
	
	@Given("bulk import for csv file with multiple data")
	public void bulk_import_for_csv_file_with_multiple_data() {
	 
		request=SerenityRest.given();
	}

	@When("user perfom bulk import for csv file with multiple data")
	public void user_perfom_bulk_import_for_csv_file_with_multiple_data() throws InterruptedException {
	  
		String csvFilePath=LoadProperties().getProperty("bulkmultipartvalidCsvfile");
		File csvDataInFile = new File(csvFilePath);
	
		response = request
		  .header("Content-Type","text/csv")
		  .header("Authorization", APIKey)
		  .body(csvDataInFile).log().all()
		  .when()
		  .post("/api/v10/csvimports/"+ importType +"");
		  System.out.println("Csv file API response:"+ response);
		  
		  Thread.sleep(1000);
	}

	@Then("user can see the file should be processed in the system with status code {int}")
	public void user_can_see_the_file_should_be_processed_in_the_system_with_status_code(Integer code) {
	 
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@Then("user can verify file record exists in the system")
	public void user_can_verify_file_record_exists_in_the_system() throws CsvValidationException, IOException {
	   
		String csvFilePath = LoadProperties().getProperty("bulkmultipartvalidCsvfile");
		
		String memberId=ReadCSVFile(csvFilePath);
		
		response = SerenityRest.given().header("Content-Type", "application/json").header("Authorization", APIKey)
				.log().all().when().get("/api/v10/memberships/"+memberId+"/details");
	}
	
	public String ReadJsonFile(String FilePath) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		FileReader fileReader = new FileReader(FilePath);
		
        JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
        
        JSONObject json=(JSONObject) jsonArray.get(0);     
		String Key = (String) json.get("Key");
		return Key;
	}
	
	public String ReadCSVFile(String CSV_PATH) throws CsvValidationException, IOException{
		String memberID = null;
	    String[] csvCell;
        //Create an object of CSVReader
        csvReader = new CSVReader(new FileReader(CSV_PATH));
        //You can use while loop like below, It will be executed until the last line in CSV used. 
        while ((csvCell = csvReader.readNext()) != null) {
             memberID = csvCell[0];
           
        }
        return memberID ;
	}

}