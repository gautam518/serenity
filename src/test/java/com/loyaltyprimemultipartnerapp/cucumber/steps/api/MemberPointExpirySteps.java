package com.loyaltyprimemultipartnerapp.cucumber.steps.api;

import java.time.ZonedDateTime;
import java.util.List;

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

public class MemberPointExpirySteps extends TestBase {

	String strapiBody;
	Response response = null;// for source member
	Response responseTX = null;
	Response responseACC = null;
	Response responseACCBFOR = null;
	Response response_OLD  = null;
	Response responseMU= null;
	Response responseRmsal=null;
	Response responseTRGT=null;
	Response responseME=null;
	Response responsePT=null;
	Response responseACCSR=null;
	Response responseACCTGT=null;
	Response responseT1=null;
	Response responseT2=null;
	Response responseT3=null;
	Response responsePL=null;
	String descText = "Points expired after 2 month(s) and 0 day(s).";
	String strType = "-";
	private String sourceKey;// source key
	private String targetKey; // target key
	private String TrxDate;
	private String TrxId;
	float redeemAmount=0;;
	float redemReversalAmount=0;
	private String sourceIntKey;// source key

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;
	
	public static String getsourceIntKey() {
		return _sourceIntKey;
	}

	private static String _sourceIntKey;
	
	
	public static String gettargetKey() {
		return _targetKey;
	}

	private static String _targetKey;

	public static String getTrxId() {
		return _TrxId;
	}

	private static String _TrxId;

	public static String getTrxDate() {
		return _trxDate;
	}

	private static String _trxDate;

	public static float getTotalPointExpiry() {
		return _totalPointExpiry;
	}

	public static float _totalPointExpiry;
	
	public static float gettotalpointExpiryDiffDate() {
		return _totalpointExpiryDiffDate;
	}
	public static float _totalpointExpiryDiffDate;
	
	public static float getamountredem() {
		return _amountredem;
	}
	public static float _amountredem;
	
	public static float getamountredemDiffDate() {
		return _amountredemDiffDate;
	}
	public static float _amountredemDiffDate;
	
	public static String getRcptId() {return _RcptId;}
	private static String _RcptId;
	
	public static float getamountPT() {
		return _amountPT;
	}
	public static float _amountPT;

	@Given("member is enroll for point expiry")
	public void member_is_enroll_for_point_expiry() {
		sourceKey = LoadProperties().getProperty("enkey") + "_PSRC" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_PSRC_AltId" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
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
	}

	@When("user perform enrollment of member for point expiry")
	public void user_perform_enrollment_of_member_for_point_expiry() {
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("member is created for point expiry with statuscode {int}")
	public void member_is_created_for_point_expiry_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@Given("member post transaction for point expiry")
	public void member_post_transaction_for_point_expiry() {
	System.out.println("earn transaction");	
	}

	@When("user perform earn transaction of member for point expiry")
	public void user_perform_earn_transaction_of_member_for_point_expiry() throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = TestUtils.GetPointExpiryDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		String price = "20";
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

	@Then("earn transaction of member for point expiry is successfull with status code {int}")
	public void earn_transaction_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseTX.getStatusCode());

		JsonPath jsonPathEval = responseTX.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform account statement for member")
	public void user_perform_account_statement_for_member() throws InterruptedException {
		String clientKey = getsourceKey();
		response_OLD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Thread.sleep(500);
	}

	@Then("account satement of member is displayed successfully with status code {int}")
	public void account_satement_of_member_is_displayed_successfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response_OLD.getStatusCode());
		JsonPath jsonPathEva = response_OLD.jsonPath();
		float val = 0;
		List<Float> getallvalues = jsonPathEva.getList("data.bookings.amount");
		for (int i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
			// if(!(getallvalues.get(i)==100))//checking if value
			if ((getallvalues.get(i) >= 0) && (getallvalues.get(i) != 100)) {
				val = val + getallvalues.get(i);
			}
		}

		_totalPointExpiry = val;
		System.out.println("transaction : Which should expiry :" + val);
	}

	@When("user run the point expiry job")
	public void user_run_the_point_expiry_job() throws InterruptedException {
		

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecQueryParm()).log().all()
				.when().get("/api/v10/loyaltyops/PointsExpiration/schedule-now?schedule=%231");
		Thread.sleep(10000);

		String clientKey = getsourceKey();
		responseACC=null;
		responseACC = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
	}

	@Then("the point expiry job run sucessfully with status code {int}")
	public void the_point_expiry_job_run_sucessfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@Then("the member can see his account statement for earn transaction succesfully with status code {int}")
	public void the_member_can_see_his_account_statement_for_earn_transaction_succesfully_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());
		
		JsonPath jsonPathEval = responseACC.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
		float getReverseAmount = 0;
		/*String getExpDetail = "";
		
		List<String> lstgetdesIds = jsonPathEval.getList("data.bookings.description");
		for (String val : lstgetdesIds) {
			if (val.equals(descText)) {// here check the field contains or not
				getExpDetail = val;
				break;
			}
		}

		Assert.assertEquals(getExpDetail, descText);*/
		float getmembersExp = 0;
		float val = 0;
		
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) { // // value or not
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
	
		getmembersExp = val;
		System.out.println("Now Balance:" + getmembersExp);
		
		String getAmount = String.valueOf(getmembersExp).replace("-", "");
		System.out.println("Now Balance Final:" + getAmount);
		float getTotalEarn = getTotalPointExpiry();
		Assert.assertEquals(getAmount, String.valueOf(getTotalEarn));
	}

	@Given("member post point adjustment for point expiry")
	public void member_post_point_adjustment_for_point_expiry() {
		String receiptId = "TextRept_" + TestUtils.getRandomValue();
		String date = TestUtils.GetPointExpiryDateTime();
		String amount = "10";
		String debitorId = LoadProperties().getProperty("partnerId");
		String description = "Test adjustment";
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);
	}

	@When("user perform point adjustment of member for point expiry")
	public void user_perform_point_adjustment_of_member_for_point_expiry() throws InterruptedException {
		sourceKey = getsourceKey();
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/adjust?type=BONUS");

		Thread.sleep(1000);
	}

	@Then("point adjustment of member for point expiry is successfull with status code {int}")
	public void point_adjustment_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@Then("the member can see his account statement for point adjustment  succesfully with status code {int}")
	public void the_member_can_see_his_account_statement_for_point_adjustment_succesfully_with_status_code(
			Integer code) {
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());
		// Need to put the integration code here as facing some issue in nested json
		//extraction.
		JsonPath jsonPathEval = responseACC.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
		float getReverseAmount = 0;float getmembersExp = 0;
		float val = 0;
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) { // // value or not
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
		getmembersExp = val;
		System.out.println("Balance:" + getmembersExp);
		getReverseAmount = getmembersExp;
		String getAmount = String.valueOf(getReverseAmount).replace("-", "");
		float getTotalEarn = getTotalPointExpiry();
		Assert.assertEquals(getAmount, String.valueOf(getTotalEarn));
		
	}

	@Given("member post point redemption for point expiry")
	public void member_post_point_redemption_for_point_expiry() {
		
		System.out.println("Redeem points");
	}

	@When("user perform point redemption of member for point expiry")
	public void user_perform_point_redemption_of_member_for_point_expiry() {
		sourceKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();// get date value from last
		_trxDate=TrxDate;
		String receiptId = "RepId_"+ TestUtils.getRandomValue();
		String debitorId = LoadProperties().getProperty("debitorId");
		_RcptId=receiptId+"_"+debitorId;
		float amount = (float) 21.00;
		//_amountredem=amount;
		String description = "Test Redemption";
		strapiBody=null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, String.valueOf(amount), description);
		
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");
	}

	@Then("point redemption of member for point expiry is successfull with status code {int}")
	public void point_redemption_of_member_for_point_expiry_is_successfull_with_status_code(Integer int1) {
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	@When("user perform point redemption for point expiry")
	public void user_perform_point_redemption_for_point_expiry() {
		sourceKey = getsourceKey();
		TrxDate = TestUtils.getDateTime();// get date value from last
		_trxDate=TrxDate;
		String receiptId = "RepId_"+ TestUtils.getRandomValue();
		String debitorId = LoadProperties().getProperty("debitorId");
		_RcptId=receiptId+"_"+debitorId;
		float amount = (float) 21.00;
		_amountredem=amount;
		String description = "Test Redemption";
		strapiBody=null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, String.valueOf(amount), description);
		
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");
	}

	@Then("point redemption for point expiry is successfull with status code {int}")
	public void point_redemption_for_point_expiry_is_successfull_with_status_code(Integer int1) {
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	@Then("member can see his account statement succesfully with status code {int}")
	public void member_can_see_his_account_statement_succesfully_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());
		JsonPath jsonPathEval = responseACC.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
		float getReverseAmount = 0;float getmembersExp = 0;
		float val = 0;
		String descText = "Points expired after 2 month(s) and 0 day(s).";
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) { // // value or not
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
		
		getmembersExp = val;
		System.out.println("Balance:" + getmembersExp);
		getReverseAmount = getmembersExp;
		String getAmount = String.valueOf(getReverseAmount).replace("-", "");
		float getTotalEarn = getTotalPointExpiry()-getamountredem();
				//-getamountredem();

		Assert.assertEquals(String.valueOf(getTotalEarn),getAmount);
	}

	@Then("the member can see his account statement succesfully with status code {int}")
	public void the_member_can_see_his_account_statement_succesfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());
		JsonPath jsonPathEval = responseACC.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
		float getReverseAmount = 0;float getmembersExp = 0;
		float val = 0;
		String descText = "Points expired after 2 month(s) and 0 day(s).";
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) { // // value or not
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
		
		getmembersExp = val;
		System.out.println("Balance:" + getmembersExp);
		getReverseAmount = getmembersExp;
		String getAmount = String.valueOf(getReverseAmount).replace("-", "");
		float getTotalEarn = getTotalPointExpiry();
				//-getamountredem();

		Assert.assertEquals(String.valueOf(getTotalEarn),getAmount);
	}

	@When("user perform point redemption reversal of member for point expiry")
	public void user_perform_point_redemption_reversal_of_member_for_point_expiry() {
		
		TrxId = getRcptId();
		TrxDate=getTrxDate();
		String description="Auto Redemption Reversal";
		String PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		responseRmsal = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
	}

	@Then("point redemption reversal of member for point expiry is successfull with status code {int}")
	public void point_redemption_reversal_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
		
		Assert.assertEquals(code.intValue(), responseRmsal.getStatusCode());

		JsonPath jsonPathEvaluator = responseRmsal.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("member member merge for point expiry")
	public void member_member_merge_for_point_expiry() {
		System.out.println("Enroll target member");
	}

	@When("user perform enrollment of member for point expiry as target")
	public void user_perform_enrollment_of_member_for_point_expiry_as_target() {
		targetKey = LoadProperties().getProperty("enkey") + "_PTRGT" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_targetKey = targetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_PTRGT_AltId" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		String strUserId = targetKey;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, targetKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);
		responseTRGT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}
	@Then("target member is created for point expiry with statuscode {int}")
	public void target_member_is_created_for_point_expiry_with_statuscode(Integer code) {
		Assert.assertEquals(code.intValue(), responseTRGT.getStatusCode());

		JsonPath jsonPathEvaluator = responseTRGT.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	@When("user perform member merge for point expiry")
	public void user_perform_member_merge_for_point_expiry() {
		sourceKey=getsourceKey();
		targetKey=gettargetKey();
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.MemberMergeAPIBody(sourceKey, targetKey);

		responseME = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships/merge");
	}
	@Then("member merge is successfull with status code {int}")
	public void member_merge_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseME.getStatusCode());

		JsonPath jsonPathEvaluator = responseME.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	
	@When("user run the point expiry job for target member")
	public void user_run_the_point_expiry_job_for_target_member() throws InterruptedException {
		String Url = "/api/v10/loyaltyops/PointsExpiration/schedule-now?schedule=%231";

		System.out.println("The url of api is: " + Url);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecQueryParm()).log().all()
				.when().get(Url);
		Thread.sleep(1000);

		String clientKey = gettargetKey();
		responseACC = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
	}
	@Then("the point expiry job for target member run sucessfully with status code {int}")
	public void the_point_expiry_job_for_target_member_run_sucessfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}
	
	@Then("the target member can see his account statement succesfully with status code {int}")
	public void the_target_member_can_see_his_account_statement_succesfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());
		JsonPath jsonPathEval = responseACC.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
		float getReverseAmount = 0;float getmembersExp = 0;
		float val = 0;
		String descText = "Points expired after 2 month(s) and 0 day(s).";
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) { // // value or not
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
		
		getmembersExp = val;
		System.out.println("Balance:" + getmembersExp);
		getReverseAmount = getmembersExp;
		String getAmount = String.valueOf(getReverseAmount).replace("-", "");
		float getTotalEarn = getTotalPointExpiry();

		Assert.assertEquals(String.valueOf(getTotalEarn),getAmount);
	}

	@Given("member point expiry for points transfer")
	public void member_point_expiry_for_points_transfer() {
		System.out.println("Point expiry");
	}

	@When("user perform enrollment of target member for point expiry as target")
	public void user_perform_enrollment_of_target_member_for_point_expiry_as_target() {
		
		targetKey = LoadProperties().getProperty("enkey") + "_TRGPT" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		_targetKey = targetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TRGPT_AltId" + TestUtils.getRandomValue()+TestUtils.getRandomNumber();
		String strUserId = targetKey;
		String strPrefix = LoadProperties().getProperty("prefix");
		String strFirstName = LoadProperties().getProperty("firstname");
		String strBirth = LoadProperties().getProperty("birth");
		String strEmail = LoadProperties().getProperty("email");
		String strPhone = LoadProperties().getProperty("phone");
		String strAddress1 = LoadProperties().getProperty("address1");

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.EnrollmentAPIBody(strSince, strType, "MUR", "ACTIVE", "Test", "", strTier, targetKey,
				strFirstName, strPrefix, strPhone, strAddress1, strEmail, strUserId, strAlternateIds, strPromoterId, "",
				strBirth);
		responseTRGT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@When("user perform point transfer from source to target member for point expiry")
	public void user_perform_point_transfer_from_source_to_target_member_for_point_expiry() {
		String receiptId = "TestRes_" + TestUtils.getRandomValue() + TestUtils.getRandomNumber();
		String date = TestUtils.getDateTime();
		Integer amount = 10;
		_amountPT=amount;
		String debitorId=LoadProperties().getProperty("debitorId");
		String description ="Test Transfer";
		String clientKey = getsourceKey();
		String destclientKey = gettargetKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.PointTransferAPIBody(date, receiptId, debitorId, amount.toString(), description);

		responsePT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("api/v10/Deposits/" + clientKey + "/Transfer/" + destclientKey + "?type=BONUS");
	}

	@Then("point transfer is successfull with status code {int}")
	public void point_transfer_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responsePT.getStatusCode());

		JsonPath jsonPathEvaluator = responsePT.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}
	
	@When("user run the point expiry job for point transfer")
	public void user_run_the_point_expiry_job_for_point_transfer() throws InterruptedException {
		
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecQueryParm()).log().all()
				.when().get("/api/v10/loyaltyops/PointsExpiration/schedule-now?schedule=%231");
		Thread.sleep(1000);

		String clientKey = getsourceKey();////source account statement
		responseACCSR = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Thread.sleep(1000);
		
		String targetKey=gettargetKey();//Target account statement
		responseACCTGT = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + targetKey + "/statement?type=BONUS");
		
	}

	@Then("the point expiry job for point transfer run sucessfully with status code {int}")
	public void the_point_expiry_job_for_point_transfer_run_sucessfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

	}
	@Then("the members can see his account statement succesfully with status code {int}")
	public void the_members_can_see_his_account_statement_succesfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseACCSR.getStatusCode());
		
		Assert.assertEquals(code.intValue(), responseACCTGT.getStatusCode());
		
		JsonPath jsonPathEval = responseACCSR.jsonPath();
		float getSourceExp = 0;float val = 0;
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) {
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
	
		getSourceExp = val;
		System.out.println("Source Balance:" + getSourceExp);
	
		JsonPath jsonPathEvalTargt = responseACCTGT.jsonPath();
		float getTargetExp = 0;float valTgt = 0;
		
		List<Float> lstgetallvalues = jsonPathEvalTargt.getList("data.bookings.amount");
		List<String> lstgetdesIds = jsonPathEvalTargt.getList("data.bookings.description");
		int j=0;
		for (j = 0; j < lstgetdesIds.size(); j++) {
			if (lstgetdesIds.get(j).equals(descText)) {
				for (j = 0; j < lstgetallvalues.size(); j++) {// iterate over all values to get max value
					if ((String.valueOf(lstgetallvalues.get(j)).contains(strType))&& lstgetdesIds.get(j).equals(descText)) {
						valTgt = valTgt + lstgetallvalues.get(j);
					}
				}
			
			}
		}
	
		getTargetExp = valTgt;
		System.out.println("Target Balance:" + getTargetExp);
		
		float getTotalAmount=getSourceExp+getTargetExp;
		String actualTotalExpiryPoint=String.valueOf(getTotalAmount).replace("-", "");
		System.out.println("Total Points will expiry Balance:" + getTargetExp);
		
		float getTotalEarn = getTotalPointExpiry();//-getamountPT();
		Assert.assertEquals(String.valueOf(getTotalEarn),actualTotalExpiryPoint);
	}

	@Given("member expiry in diffrent date range")
	public void member_expiry_in_diffrent_date_range() {
		System.out.println("point expiry for diffrent dates");
	}

	@When("user perform earn transaction on txdate{string} for point expiry")
	public void user_perform_earn_transaction_on_txdate_for_point_expiry(String trxDate) throws InterruptedException {

		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = trxDate;//TestUtils.GetPointExpiryDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		String price = "20";
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

	@When("user perform redemption on date{string} for point expiry")
	public void user_perform_redemption_on_date_for_point_expiry(String trxDate) {
		sourceKey = getsourceKey();
		TrxDate ="2021-03-24T00:00:00.000Z"; //trxDate;//TestUtils.getDateTime();// get date value from last
		_trxDate=TrxDate;
		String receiptId = "RepId_"+ TestUtils.getRandomValue();
		String debitorId = LoadProperties().getProperty("debitorId");
		_RcptId=receiptId+"_"+debitorId;
		String amount = "21";
		String description = "Test Redemption";
		strapiBody=null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount, description);
		
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");
	}
	@Then("redemption of member for point expiry is successfull with status code {int}")
	public void redemption_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
		
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@When("user perform redemption reversal on date{string} for point expiry")
	public void user_perform_redemption_reversal_on_date_for_point_expiry(String trxDate) {
		TrxId = getRcptId();
		TrxDate=getTrxDate();
		String description="Auto Redemption Reversal";
		String PointsType = LoadProperties().getProperty("pointType");
		
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.TranscationRedemptionReversalAPIBody(TrxId, TrxDate, PointsType, description);

		responseRmsal = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/redeem/reversal");
	}
	@Then("redemption reversal of member for point expiry is successfull with status code {int}")
	public void redemption_reversal_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
		
		Assert.assertEquals(code.intValue(), responseRmsal.getStatusCode());

		JsonPath jsonPathEvaluator = responseRmsal.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Given("point expiry is not possible for cancel member")
	public void point_expiry_is_not_possible_for_cancel_member() {
		System.out.println("point expiry is not possible for cancel member");
	}

	@When("user perform membership update for the member as cancel")
	public void user_perform_membership_update_for_the_member_as_cancel() {
		 sourceKey=getsourceKey();
		    responseMU = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec())
					.log().all().when()
					.post("/api/v10/memberships/"+sourceKey+"/cancel");
	}

	@Then("membership update for the member is successful with status code {int}")
	public void membership_update_for_the_member_is_successful_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseMU.getStatusCode());

		JsonPath jsonPathEval = responseMU.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@Then("the member can not see his account statement with status code {int}")
	public void the_member_can_not_see_his_account_statement_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());

		JsonPath jsonPathEvaluator = responseACC.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(false,getStatus);
		
	}

	@Given("member expiry in multiple date range in month")
	public void member_expiry_in_multiple_date_range_in_month() {
		System.out.println("member expiry in multiple date range in month");
	}

	@When("user perform earn transaction of txdate{string} and {int}points for point expiry for first time")
	public void user_perform_earn_transaction_of_txdate_and_points_for_point_expiry_for_first_time(String txdate, Integer amount) throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = txdate;//trxDate;//TestUtils.GetPointExpiryDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		Integer price = amount; String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();

		String channelkey = ""; String productKey = "";
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
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price.toString(), productKey,
				channelkey, partnerId);
		responseT1 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
		
	}
	@Then("first earn transaction of member for point expiry is successfull with status code {int}")
	public void first_earn_transaction_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
	
		Assert.assertEquals(code.intValue(), responseT1.getStatusCode());

		JsonPath jsonPathEval = responseT1.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}
	
	@When("user perform account statement for member multiple transactions")
	public void user_perform_account_statement_for_member_multiple_transactions() throws InterruptedException {
		String clientKey = getsourceKey();
		response_OLD=null;
		response_OLD = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
		Thread.sleep(500);
	}

	@Then("account satement of member for multiple transactions is displayed successfully with status code {int}")
	public void account_satement_of_member_for_multiple_transactions_is_displayed_successfully_with_status_code(Integer code) {
	  
		Assert.assertEquals(code.intValue(), response_OLD.getStatusCode());
		JsonPath jsonPathEva = response_OLD.jsonPath();
		float val = 0;
		List<Float> getallvalues = jsonPathEva.getList("data.bookings.amount");
		for (int i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
			// if(!(getallvalues.get(i)==100))//checking if value
			if ((getallvalues.get(i) >= 0) && (getallvalues.get(i) != 100)) {
				val = val + getallvalues.get(i);
			}
		}

		_totalPointExpiry = val;
		_totalpointExpiryDiffDate=val;
		System.out.println("transaction : Which should expiry :" + val);
	}

	@When("user perform earn transaction of txdate{string} and {int}points for point expiry for second time")
	public void user_perform_earn_transaction_of_txdate_and_points_for_point_expiry_for_second_time(String txdate, Integer amount) throws InterruptedException {
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = txdate;//trxDate;//TestUtils.GetPointExpiryDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		Integer price = amount; String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();

		String channelkey = ""; String productKey = "";
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
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price.toString(), productKey,
				channelkey, partnerId);
		responseT2 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}
	@Then("second earn transaction of member for point expiry is successfull with status code {int}")
	public void second_earn_transaction_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), responseT2.getStatusCode());

		JsonPath jsonPathEval = responseT2.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}

	@When("user perform earn transaction of txdate{string} and {int}points for point expiry for third time")
	public void user_perform_earn_transaction_of_txdate_and_points_for_point_expiry_for_third_time(String txdate, Integer amount) throws InterruptedException {
	    
		String TrxId = "SRC_TX" + TestUtils.getRandomValue();
		_TrxId = TrxId; // Set value here for transaction
		String TrxDate = txdate;//trxDate;//TestUtils.GetPointExpiryDateTime();
		_trxDate = TrxDate; // Set value here for transaction

		Integer price = amount; String quantity = "1";
		String clientKey = getsourceKey();
		String partnerId = LoadProperties().getProperty("partnerId");
		clientKey = getsourceKey();

		String channelkey = ""; String productKey = "";
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
		strapiBody = apibody.TransactionAPISingleBody(TrxId, clientKey, TrxDate, quantity, price.toString(), productKey,
				channelkey, partnerId);
		responseT3 = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/tx");

		Thread.sleep(1000);
	}
	@Then("third earn transaction of member for point expiry is successfull with status code {int}")
	public void third_earn_transaction_of_member_for_point_expiry_is_successfull_with_status_code(Integer code) {
	    
		Assert.assertEquals(code.intValue(), responseT3.getStatusCode());

		JsonPath jsonPathEval = responseT3.jsonPath();
		Boolean strSuccess = jsonPathEval.get("success");
		Assert.assertEquals(true, strSuccess);
	}
//user perform redemption on date<date> and <point>points for point expiry
	@When("user perform redemption on date{string} and {int}points for point expiry")
	public void user_perform_redemption_on_date_and_points_for_point_expiry(String strDate, Integer strAmount) {
	   
		sourceKey = getsourceKey();
		TrxDate = strDate;//TestUtils.getDateTime();
		String receiptId = "RepId_"+ TestUtils.getRandomValue();
		String debitorId = LoadProperties().getProperty("debitorId");
		Integer amount = strAmount;
		_amountredemDiffDate=amount;
		String description = "Test Redemption";
		strapiBody=null;
		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.RedeeemAPIBody(receiptId, TrxDate, debitorId, amount.toString(), description);
		
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + sourceKey + "/redeem?type=BONUS");
	}	
	
	@When("user run the point expiry job for multiple transactions")
	public void user_run_the_point_expiry_job_for_multiple_transactions() throws InterruptedException {
	    
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpecQueryParm()).log().all()
				.when().get("/api/v10/loyaltyops/PointsExpiration/schedule-now?schedule=%231");
		Thread.sleep(1000);

		String clientKey = getsourceKey();
		responseACC = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).log().all().when()
				.get("/api/v10/deposits/" + clientKey + "/statement?type=BONUS");
	}

	@Then("the point expiry job for multiple transactions run sucessfully with status code {int}")
	public void the_point_expiry_job_for_multiple_transactions_run_sucessfully_with_status_code(Integer code) {
		Assert.assertEquals(code.intValue(), response.getStatusCode());
	}

	@Then("the member can see his all transactions account statement succesfully with status code {int}")
	public void the_member_can_see_his_all_transactions_account_statement_succesfully_with_status_code(Integer code) {
	   
		Assert.assertEquals(code.intValue(), responseACC.getStatusCode());
		JsonPath jsonPathEval = responseACC.jsonPath();
		boolean getStatus = jsonPathEval.get("success");
		Assert.assertEquals(getStatus, true);// status check
		
		float getReverseAmount = 0;float getmembersExp = 0;
		float val = 0;
		String descText = "Points expired after 2 month(s) and 0 day(s).";
		List<Float> getallvalues = jsonPathEval.getList("data.bookings.amount");
		List<String> getdesIds = jsonPathEval.getList("data.bookings.description");
		int i=0;
		for (i = 0; i < getdesIds.size(); i++) {
			if (getdesIds.get(i).equals(descText)) {
				for (i = 0; i < getallvalues.size(); i++) {// iterate over all values to get max value
					if ((String.valueOf(getallvalues.get(i)).contains(strType))&& getdesIds.get(i).equals(descText)) { // // value or not
						val = val + getallvalues.get(i);
					}
				}
			
			}
		}
		
		getmembersExp = val;
		System.out.println("Balance:" + getmembersExp);
		getReverseAmount = getmembersExp;
		String getAmount = String.valueOf(getReverseAmount).replace("-", "");
		System.out.println("Here:1=" +gettotalpointExpiryDiffDate());
		
		System.out.println("Here:2=" +getamountredemDiffDate());
		float getTotalEarn = gettotalpointExpiryDiffDate()-getamountredemDiffDate();//getTotalPointExpiry();

		Assert.assertEquals(String.valueOf(getTotalEarn),getAmount);
	}
	
}