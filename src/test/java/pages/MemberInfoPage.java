package pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

public class MemberInfoPage extends PageObject {
	
    @FindBy(xpath="//div[@id='container-1010-innerCt']/a")
    private WebElementFacade bntClick;
    
    @FindBy(xpath="//div[@id='ext-element-320']")
    private WebElementFacade bntMemberClick;
    
    @FindBy(xpath="//table[@id='gridview-2021-record-844']/tbody/tr/td[1]/div[1]")
    private WebElementFacade bntMemberName;
    
    @FindBy(id="textfield-2060-inputEl")
    private WebElementFacade memberNameText;
    public void memberInfo()
    {
    	bntMemberClick.click();
        bntMemberName.click();
    }
    public String getMemmberInfoMessag(){
        return memberNameText.getTextContent();
    }
}