package pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class SearchMemberPage extends PageObject {
	
	
    @FindBy(id = "textfield-1025-inputEl")
    private WebElementFacade usernameField;

    @FindBy(id = "textfield-1026-inputEl")
    private WebElementFacade passwordField;


    @FindBy(id="button-1028-btnInnerEl")
    private WebElementFacade btnLogin;

    @FindBy(xpath ="displayfield-1027-inputEl")
    private WebElementFacade errorMessageElement;
    
    @FindBy(xpath="//span[text()='Logout']")
    private WebElementFacade btnLogout;

    @FindBy(xpath="//a[@id='button-1015']")
    private WebElementFacade bntClick;
    
    @FindBy(xpath="//div[@id='ext-element-831']")
    private WebElementFacade bntMemberClick;
    
    @FindBy(xpath="//input[@id='textfield-1093-inputEl']")
    private WebElementFacade memberSearch;
    
    
    @FindBy(xpath="//table[@id='gridview-1610-record-439']/tbody/tr/td[1]/div")
    private WebElementFacade clickMemberKey;
    
    @FindBy(id="textfield-2060-inputEl")
    private WebElementFacade memberNameText;
    
    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        btnLogin.click();
        
    }
    public void getMemberInfo()
    {
    	bntMemberClick.click();
    	memberSearch.typeAndEnter("ram");
    	clickMemberKey.click();
    }
    
    public String getMemmberInfoMessag(){
        return memberNameText.getTextContent();
    }
    
    
    public void logout() throws InterruptedException 
    {
    	bntClick.click();
    	btnLogout.click();
    }
}