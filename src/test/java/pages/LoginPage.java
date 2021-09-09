package pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class LoginPage extends PageObject {
	
	
	 	//@FindBy(id = "textfield-1025-inputEl")
	   // @FindBy(id = "textfield-1029-inputEl")
	    @FindBy(id="textfield-1025-inputEl")
	    private WebElementFacade usernameField;

	    //@FindBy(id = "textfield-1026-inputEl")
	    //@FindBy(id = "textfield-1030-inputEl")
	    @FindBy(id="textfield-1026-inputEl")
	    private WebElementFacade passwordField;

	    //@FindBy(id = "button-1028-btnInnerEl")
	    //@FindBy(id="button-1032-btnInnerEl")
	    @FindBy(id="button-1028-btnInnerEl")
	    private WebElementFacade btnLogin;

	    //@FindBy(xpath ="//div[text()='Login failed']")
	    @FindBy(id="displayfield-1027-inputEl")
	    private WebElementFacade errorMessageElement;
	    
	    //@FindBy(xpath="//a[@id='menuitem-1018-itemEl']/span")
	    //@FindBy(xpath="//div[@id='menuitem-1014']/a")
	    @FindBy(xpath="//span[text()='Logout']")
	    private WebElementFacade btnLogout;

	    //@FindBy(id="button-1015-btnInnerEl")
	   // @FindBy(xpath="//div[@id='container-1010-innerCt']/a")
	    @FindBy(xpath="//a[@id='button-1015']")
	    private WebElementFacade bntClick;
	    
	    public void login(String username, String password) {
	        usernameField.sendKeys(username);
	        passwordField.sendKeys(password);
	        btnLogin.click();
	    }

	    public String getMessageError(){
	        //waitFor(errorMessageElement);
	        return errorMessageElement.getTextContent();
	    }
	    
	    public void logout() throws InterruptedException 
	    {
	    	bntClick.click();
	    	btnLogout.click();
	    }
	}