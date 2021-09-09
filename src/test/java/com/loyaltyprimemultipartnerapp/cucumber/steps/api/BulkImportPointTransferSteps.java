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

public class BulkImportPointTransferSteps extends TestBase {

	String jsonFilePath;
	String strapiBody;
	private String sourceKey;// source key
	private String targetKey;// target member
	private Response response = null; // Response

	String importType = "PointTransfer";
	String batchId = "12345";
	String strKey = "fullImport_mship001_key";

	public static String getsourceKey() {
		return _sourceKey;
	}

	private static String _sourceKey;

	public static String gettargetKey() {
		return _targetKey;
	}

	private static String _targetKey;

	@Given("user check for bulk import of point transfer")
	public void user_check_for_bulk_import_of_point_transfer() {
		System.out.println("bulk import point transfer csv");
	}

	@When("user perform member enrollmnet for soource for bulk import of point transfer")
	public void user_perform_member_enrollmnet_for_soource_for_bulk_import_of_point_transfer() {

		sourceKey = LoadProperties().getProperty("enkey") + "_SRIMP" + TestUtils.getRandomValue();
		_sourceKey = sourceKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_SRIMP_AltId" + TestUtils.getRandomValue()
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

	@Then("source member is enroll successfully for bulk import of point transfer")
	public void source_member_is_enroll_successfully_for_bulk_import_of_point_transfer() {

		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user performs points adjust for bulk import of point transfer")
	public void user_performs_points_adjust_for_bulk_import_of_point_transfer() {
		String date = TestUtils.getDateTime();
		String receiptId = "TesADjpt" + TestUtils.getRandomNumber();
		String debitorId = LoadProperties().getProperty("debitorId");
		Double amount = 100.0;
		String description = "Test Adjustment";
		String clientKey = getsourceKey();

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.AjustmentAPIBody(receiptId, date, amount.toString(), debitorId, description);

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/deposits/" + clientKey + "/adjust?type=BONUS");
	}

	@Then("point adjustment is succesful for point transfer with status code {int}")
	public void point_adjustment_is_succesful_for_point_transfer_with_status_code(Integer code) {

		Assert.assertEquals(code.intValue(), response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		boolean getStatus = jsonPathEvaluator.get("success");
		Assert.assertEquals(getStatus, true);
	}

	@When("user perform member enrollmnet for target for bulk import of point transfer")
	public void user_perform_member_enrollmnet_for_target_for_bulk_import_of_point_transfer() {

		targetKey = LoadProperties().getProperty("enkey") + "_TRM" + TestUtils.getRandomValue();
		_targetKey = targetKey;
		String strSince = TestUtils.getDateTime();
		String strType = LoadProperties().getProperty("membertype");
		String strTier = LoadProperties().getProperty("tier");
		String strPromoterId = LoadProperties().getProperty("promoterId");
		String strAlternateIds = LoadProperties().getProperty("enkey") + "_TRM_AltId" + TestUtils.getRandomValue()
				+ TestUtils.getRandomNumber();
		String strUserId = _targetKey;
		// _sourceLegalEt=strUserId;
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

		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/memberships");
	}

	@Then("target member is enroll successfully for bulk import of point transfer")
	public void target_member_is_enroll_successfully_for_bulk_import_of_point_transfer() {
		Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

		JsonPath jsonEval = response.jsonPath();

		boolean getstatus = jsonEval.get("success");
		Assert.assertEquals(getstatus, true);
	}

	@When("user perform the bulk import of point transfer")
	public void user_perform_the_bulk_import_of_point_transfer() throws InterruptedException {
		String sourceKey = getsourceKey();
		String pointsType = "BONUS";
		String targetMshipKey = gettargetKey();
		String accountId = "";
		String debitorId = LoadProperties().getProperty("debitorId");
		String date = TestUtils.getDateTime();
		String receiptId = "TSTREPX_" + TestUtils.getRandomValue();
		String amount = "5";
		String description = "Test Bulk point transfer";

		AIPsJsonBody apibody = new AIPsJsonBody();
		strapiBody = apibody.BulkImportPointTransfer(sourceKey, pointsType, targetMshipKey, accountId, debitorId, date,
				receiptId, amount, description);
		response = SerenityRest.given().spec(ReuseableSpecifications.getGenericRequestSpec()).body(strapiBody).log()
				.all().when().post("/api/v10/csvimports/" + importType + "");

		Thread.sleep(1000);
	}

	@Then("bulk import of point transfer is successfully with status code {int}")
	public void bulk_import_of_point_transfer_is_successfully_with_status_code(Integer code) throws InterruptedException {
		Assert.assertEquals(code.intValue(), response.getStatusCode());

		JsonPath jsonPathEvaluator = response.jsonPath();
		Boolean strSuccess = jsonPathEvaluator.get("success");
		Assert.assertEquals(true, strSuccess);
		Thread.sleep(1000);
	}

	@Then("bulk import point transfer records exists")
	public void bulk_import_point_transfer_records_exists() {

	}

}
