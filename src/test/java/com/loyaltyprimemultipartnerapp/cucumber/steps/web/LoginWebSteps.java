package com.loyaltyprimemultipartnerapp.cucumber.steps.web;

import java.time.Duration;

import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.pages.PageObject;
import pages.LoginPage;


public class LoginWebSteps extends PageObject{
	
	LoginPage loginPage_pageobject;
	
	
	@Given("User should be on the login page of PCv{int} admin portal")
	public void user_should_be_on_the_login_page_of_PCv_admin_portal(Integer int1) {
	     withTimeoutOf(Duration.ofSeconds(30));
			loginPage_pageobject.open();
			System.out.println("Open URL here");
	}
	
	@When("User enters correct username {string} and password {string} and clicks on login button")
	public void user_enters_correct_username_and_password_and_clicks_on_login_button(String username, String password) {
		loginPage_pageobject.login(username,password);
	}
	
	@Then("User should login successfully and navigate to the dashboard page.")
	public void user_should_login_successfully_and_navigate_to_the_dashboard_page() {
	    SoftAssertions softAssertion = new SoftAssertions();
			WebDriverWait wait = new WebDriverWait(getDriver(),2);
	        wait.until(ExpectedConditions.titleContains("Prime Cloud"));
	        softAssertion.assertAll();
	}

	@Then("User should select profile dropdown and Click on logout button")
	public void user_should_select_profile_dropdown_and_Click_on_logout_button() throws InterruptedException {
	    loginPage_pageobject.logout();
	}

	@Then("User should logout of Pcv{int} admin and see login page.")
	public void user_should_logout_of_Pcv_admin_and_see_login_page(Integer int1) {
	    System.out.println("LogOut Successfully");
	}

	
	@When("User enters incorrect username {string} and password {string} and clicks on login button")
	public void user_enters_incorrect_username_and_password_and_clicks_on_login_button(String username, String password) {
		loginPage_pageobject.login(username,password);
	}

	@Then("User should not be able to login")
	public void user_should_not_be_able_to_login() {
	    String expectedText="Login failed";
		      String actualText=loginPage_pageobject.getMessageError();
		      Assert.assertEquals(expectedText,actualText);
	}

	@Then("Message should be displayed : login failed")
	public void message_should_be_displayed_login_failed() {
	   System.out.println("LogOut Successfully:"+loginPage_pageobject.getMessageError());
	}
	
	@When("User does not put Username and clicks on login button")
	public void user_does_not_put_Username_and_clicks_on_login_button() {
	}

	@Then("User should see error message {string}.")
	public void user_should_see_error_message(String string) {
	}
	
	
	
}
