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

import com.loyaltyprimemultipartnerapp.testbase.TestBase;
import com.loyaltyprimemultipartnerapp.utlis.AIPsJsonBody;
import com.loyaltyprimemultipartnerapp.utlis.ExcelReader;
import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;
import com.loyaltyprimemultipartnerapp.utlis.TestUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class MemberEnrollSteps extends TestBase {

	String strapiBody;
	String sourceKeyVal = null;
	String strTypeval = null;
	String strTierVal = null;
	private String sourceKey;// source key

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	public static String getsourceKeyUpdate() {
		return _sourceKeyUpdate;
	}

	private static String _sourceKeyUpdate;
	

	public static String getsourcememberId() {
		return sourcememberId;
	}

	private static String sourcememberId;

	public static String getTargetmemberId() {
		return targetmemberId;
	}

	private static String targetmemberId;

	Response response = null;
	Response responseUpdate= null;

	@Given("user request the Enrollment API")
	public void user_request_the_Enrollment_API() {
		System.out.println("enrollment");
	}

	@When("user check the request with passing all the required parameter")
	public void user_check_the_request_with_passing_all_the_required_parameter() {
		sourceKey = LoadProperties().getProperty("enkey") + "_ESRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_ESRC_AltId" + TestUtils.getRandomValue()
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

	@Then("user check that enrollment should be done successfully with error code {int}")
	public void user_check_that_enrollment_should_be_done_successfully_with_error_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("user requests the Enrollment API")
	public void user_requests_the_Enrollment_API() {
		System.out.println("check key validateion");
	}

	@When("user check the response by passing {string} value as null or blank")
	public void user_check_the_response_by_passing_value_as_null_or_blank(String strMsg) {
		sourceKeyVal = LoadProperties().getProperty("enkey") + "_ESRCVal" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKeyVal;
		String strSince = TestUtils.getDateTime();
		strTypeval = LoadProperties().getProperty("membertype");
		strTierVal = LoadProperties().getProperty("tier");

		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_ESRC_AltIdVal" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = sourceKeyVal;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strTypeval, "MUR", "ACTIVE", "Test", "", strTierVal,
				sourceKeyVal, strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds,
				strPromoterId, "", strBirth);
		if (strMsg.equalsIgnoreCase("Key")) {
			strapiBody = strapiBody.replace(sourceKeyVal, "");
		} else if (strMsg.equalsIgnoreCase("Type")) {
			strapiBody = strapiBody.replace(strTypeval, "");
		} else if (strMsg.equalsIgnoreCase("Tier")) {
			strapiBody = strapiBody.replace(strTierVal, "");
		}
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("user check that error message has been received with error code {int}")
	public void user_check_that_error_message_has_been_received_with_error_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess);
	}

	@When("user check the response by not defining {string} value")
	public void user_check_the_response_by_not_defining_value(String strMsg) {
		sourceKey = LoadProperties().getProperty("enkey") + "_ESRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = "";
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

	@When("user check the response by not passing {string} value")
	public void user_check_the_response_by_not_passing_value(String strMsg) {
		sourceKey = LoadProperties().getProperty("enkey") + "_ESRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = strMsg;
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

	@When("user check the response by passing {string} value")
	public void user_check_the_response_by_passing_value(String strMsg) {
		sourceKey = LoadProperties().getProperty("enkey") + "_ESRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = strMsg;
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
	
	@Then("user check that member should get enrolled without alternateId with status code {int}")
	public void user_check_that_member_should_get_enrolled_without_alternateId_with_status_code(Integer code) {
		
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(false, strSuccess); 
	}
	
	@When("user requests the Enrollment API for update")
	public void user_requests_the_Enrollment_API_for_update() {
		sourceKey = LoadProperties().getProperty("enkey") + "_ESRC" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		_sourceKeyUpdate = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "ALT_ESRC" + TestUtils.getRandomValue()
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

	@Then("member is created successfully for update with status code {int}")
	public void member_is_created_successfully_for_update_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess); 
	}

	@When("user update the value as address, name, DOB of new enrolled member using PUT API")
	public void user_update_the_value_as_address_name_DOB_of_new_enrolled_member_using_PUT_API() {		
		
		String strKey=getsourceKeyUpdate();
		String strName = "UpAutname";
		String strBirth = LoadProperties().getProperty("birth");
		String strAddress1 = "updateaddress1";
		String strZip="80337";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.UpDateEnrollmentBody(strName,strBirth,strAddress1,strZip);

		responseUpdate = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().put("/api/v10/memberships/"+strKey+"/details");
	}

	@Then("user check the updated details of member using GET\\/memberships API which should get updated")
	public void user_check_the_updated_details_of_member_using_GET_memberships_API_which_should_get_updated() {

		Assert.assertEquals(HttpStatus.SC_OK, responseUpdate.getStatusCode());

		JsonPath jsonPathEvaluator = responseUpdate.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess); 
	}

	@Given("user perform the Enrollment API")
	public void user_perform_the_Enrollment_API() {
		System.out.println("enrollment");
	}

	@When("user create a new member by passing input data as firstName {string} lastName {string} email {string} key {string} tier {string} userId {string} alternateIds {string}")
	public void user_create_a_new_member_by_passing_input_data_as_firstName_lastName_email_key_tier_userId_alternateIds(
			String firstName, String lastname, String email, String key, String tier, String userid,
			String alternateIds) {

		key = TestUtils.getRandomValue() + "ME_" + key;
		alternateIds = TestUtils.getRandomValue() + "_" + alternateIds;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_MPTSRC_AltId" + TestUtils.getRandomValue();
		String strUserId = key;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = firstName;
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = email;
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, key,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("check that the member has been enrolled successfully with valid status code and with email {string} is created")
	public void check_that_the_member_has_been_enrolled_successfully_with_valid_status_code_and_with_email_is_created(
			String strEmail) {

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
		System.out.println("Member is Added successfully:" + response.asString());

		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		String getEmaild = jsonPathEvaluator.get("data.owner.properties.Email");
		System.out.println("Email id received from Response " + getEmaild);

		Assert.assertEquals(getEmaild, strEmail);

	}

	@Given("call the enrollment API again and again")
	public void call_the_enrollment_API_again_and_again() {
		System.out.println("enrollment");
	}

	@When("check that all the new request added with all details MembereTrx and rownumber {int}")
	public void check_that_all_the_new_request_added_with_all_details_MembereTrx_and_rownumber(Integer rownumber)
			throws InvalidFormatException, IOException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData("src/test/resources/testdata/MemTransaction.xlsx",
				"Member");

		sourceKey = testData.get(rownumber).get("Key") + "_" + TestUtils.getRandomValue();
		sourcememberId = sourceKey; // set the key value here

		String tier = testData.get(rownumber).get("Tier");
		String firstName = testData.get(rownumber).get("FirstName");
		// String LastName = testData.get(rowNumber).get("LastName");
		String prefix = testData.get(rownumber).get("Prefix");
		String mobile = testData.get(rownumber).get("Mobile");
		String city = testData.get(rownumber).get("City");
		String email = testData.get(rownumber).get("Email");
		String userId = sourceKey;// testData.get(rowNumber).get("UserId");
		String alternateIds = testData.get(rownumber).get("AlternateIds") + "XAL_" + TestUtils.getRandomValue();
		String since = testData.get(rownumber).get("Since");
		String type = testData.get(rownumber).get("Type");
		String ccy = testData.get(rownumber).get("Ccy");
		String contractStatus = testData.get(rownumber).get("ContractStatus");
		String name = testData.get(rownumber).get("Name");
		String program = testData.get(rownumber).get("Program");
		String promoterId = testData.get(rownumber).get("PromoterId");
		String memberType = testData.get(rownumber).get("MemberType");
		String dateofBirth = testData.get(rownumber).get("DateofBirth");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(since, type, ccy, contractStatus, name, program, tier, sourceKey,
				firstName, prefix, mobile, city, email, userId, alternateIds, promoterId, memberType, dateofBirth);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
		Thread.sleep(1000);
	}

	@Then("member should be enrolled in the system succesfully with valid status code{string}")
	public void member_should_be_enrolled_in_the_system_succesfully_with_valid_status_code(String code) {
		System.out.println("responseBody --->" + response.asString());
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		String key = response.jsonPath().getString("data.key");
		System.out.println("Get Source member key received from Response " + key);

		Assert.assertEquals(key, sourcememberId);
	}

}