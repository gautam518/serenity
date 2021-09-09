package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.util.List;
import org.apache.http.HttpHeaders;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import java.util.Properties;
import java.io.InputStream;

import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class MemberMergeIntegrationTransactionSteps {
	private String targetclientKey;// Source key

	HttpHeaders headers;
	String strapiBody;
	private Response responseTXH = null; // Response
	private Response responseREDEMH = null; // Response
	
	public static Integer getAfterMergeTargettransCount() {return _aftermergetargettransCount;}
	private static Integer _aftermergetargettransCount;
	public static Integer getAfterMergeTargetredeemCount() {return _aftermergetargetredeemCount;}
	private static Integer _aftermergetargetredeemCount;
	
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

	@Given("Target member wants to check transaction history")
	public void target_member_wants_to_check_transaction_history() {

		targetclientKey = MemberMergeSteps.gettargetKey();
		System.out.println("The Target member key :" + targetclientKey);
	}

	@When("Target member perform the transaction history")
	public void target_member_perform_the_transaction_history() {

		targetclientKey = MemberMergeSteps.gettargetKey();
		responseTXH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + targetclientKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when().get("/api/v10/tx");

	}

	@Then("Transaction history of the target member should include all the transaction detail of source member with status code {int}")
	public void transaction_history_of_the_target_member_should_include_all_the_transaction_detail_of_source_member_with_status_code(
			Integer statuscode) {

		// check success status
		Assert.assertEquals(statuscode.intValue(), responseTXH.getStatusCode());
		JsonPath jsonPathValidator = responseTXH.jsonPath();
		Integer targetMemberTxhistoycountAfterMerg=jsonPathValidator.get("total");
		_aftermergetargettransCount=targetMemberTxhistoycountAfterMerg;
		
		
	}
	@Then("Transaction history of the target member should contain the fields {string} and {string} after merging")
	public void transaction_history_of_the_target_member_should_contain_the_fields_and_after_merging(
			String strtransferredTo, String strtransferredReason) throws ParseException, InterruptedException {
		JsonPath jsonPathValidator = responseTXH.jsonPath();
		String str_data1 = null;
		String str_data2 = null;
		List<String> trnsIds = jsonPathValidator.getList("data.transferredTo");
		System.out.println("The list: " + trnsIds);
		for (String id : trnsIds) {
			if (id.equals(targetclientKey)) {// here check the field contais or not
				str_data1 = id;
				break;
			}
		}
		List<String> reasonsIds = jsonPathValidator.getList("data.transferReason");
		System.out.println("The list reason: " + reasonsIds);
		for (String val : reasonsIds) {
			if (val.equals("Membership merge")) {// here check the field contais or not
				str_data2 = val;
				break;
			}
		}
		Thread.sleep(2000);
		Assert.assertEquals(targetclientKey, str_data1);
		Assert.assertEquals("Membership merge", str_data2);
	}

	@Given("Target member wants to check reward history")
	public void target_member_wants_to_check_reward_history() {

		targetclientKey = MemberMergeSteps.gettargetKey();
		System.out.println("The Target member key :" + targetclientKey);
	}

	@When("Target member perform the reward history")
	public void target_member_perform_the_reward_history() {

		targetclientKey = MemberMergeSteps.gettargetKey();
		responseREDEMH = SerenityRest.given().urlEncodingEnabled(false)
				.queryParam("filter", "[{\"property\": \"client\", \"value\": \"" + targetclientKey + "\"}]")
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/tx/rewardsHistory");
	}

	@Then("Reward history of the target member should include all the transaction detail of source member with status code {int}")
	public void reward_history_of_the_target_member_should_include_all_the_transaction_detail_of_source_member_with_status_code(
			Integer statuscode) {
		// check success status
		Assert.assertEquals(statuscode.intValue(), responseREDEMH.getStatusCode());
		JsonPath jsonPathValidator = responseREDEMH.jsonPath();
		Integer targetMemberRedeemcountAfterMerg=jsonPathValidator.get("total");
		_aftermergetargetredeemCount=targetMemberRedeemcountAfterMerg;
	}

	@Then("Reward history of the target member should contain the fields {string} and {string} after merging")
	public void reward_history_of_the_target_member_should_contain_the_fields_and_after_merging(String strTransferredTo,
			String strTransferReason) {
		// Creation of JsonPath object
		targetclientKey = MemberMergeSteps.gettargetKey();
		JsonPath jsonPathValidator1 = responseREDEMH.jsonPath();
		String str_data1 = null;
		String str_data2 = null;
		List<String> trsfIds = jsonPathValidator1.getList("data.transferredTo");
		System.out.println("The list: " + trsfIds);
		for (String id : trsfIds) {
			if (id.equals(targetclientKey)) {// here check the field contais or not
				str_data1 = id;
				break;
			}
		}
		List<String> reasonsIds = jsonPathValidator1.getList("data.transferReason");
		System.out.println("The list reason: " + reasonsIds);
		for (String val : reasonsIds) {
			if (val.equals("Membership merge")) {// here check the field contais or not
				str_data2 = val;
				break;
			}
		}
		Assert.assertEquals(targetclientKey, str_data1);
		Assert.assertEquals("Membership merge", str_data2);
	}
}
