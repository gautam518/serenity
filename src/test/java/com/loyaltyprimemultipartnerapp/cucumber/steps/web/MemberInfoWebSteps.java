package com.loyaltyprimemultipartnerapp.cucumber.steps.web;

import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Then;
import net.thucydides.core.pages.PageObject;
import pages.MemberInfoPage;

public class MemberInfoWebSteps extends PageObject{
	
	MemberInfoPage memberInfoPage_pageobject;

	@Then("User should login successfully and navigate to the member info page.")
	public void MemberDashboardInfo() {
		WebDriverWait wait = new WebDriverWait(getDriver(),2);
        wait.until(ExpectedConditions.titleContains("Prime Cloud"));
        
		memberInfoPage_pageobject.memberInfo();
       String expectedText="SPONSOR-US";
	    String actualText=memberInfoPage_pageobject.getMemmberInfoMessag();
		      //assertEquals(expectedText, actualText);
		      Assert.assertEquals(expectedText,actualText);
		
	}

}
