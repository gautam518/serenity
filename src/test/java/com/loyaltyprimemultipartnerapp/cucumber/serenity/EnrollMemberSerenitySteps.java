package com.loyaltyprimemultipartnerapp.cucumber.serenity;

import com.loyaltyprimemultipartnerapp.utlis.ReuseableSpecifications;

import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

public class EnrollMemberSerenitySteps {
	
	private String BASE_URI="https://loyalty-prime-dev.cloud/sbx009/api/v10/memberships"; 

	@Step("Getting information of members info api")
	public void getMemberByAPIEndPoints(){
		SerenityRest
		.rest()
		.given()
		.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
		.when().baseUri(BASE_URI);
		
	}
	@Step("Creating member request")
	public ValidatableResponse createMember(){
		return SerenityRest.rest().given()
				.spec(ReuseableSpecifications.getGenericRequestSpec()).log().all()
				.when()
				.post(BASE_URI)
				.then();
	}
}
