package com.loyaltyprimemultipartnerapp.cucumber.steps.web;

import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Then;
import net.thucydides.core.pages.PageObject;
import pages.LoginPage;
import pages.SearchMemberPage;

public class SearchMemberSteps extends PageObject{

	LoginPage loginPage_pageobject;
	SearchMemberPage searchmemberPage_pageobject;
	
	@Then("^User should login successfully and should able to search member$")
	public void MemberDashboardInfo() {
		
		WebDriverWait wait = new WebDriverWait(getDriver(),2);
        wait.until(ExpectedConditions.titleContains("Prime Cloud"));
        searchmemberPage_pageobject.getMemberInfo();
       String expectedText="avava_12399";
	    String actualText=searchmemberPage_pageobject.getMemmberInfoMessag();
		     
		      Assert.assertEquals(expectedText,actualText);
		
	}
}