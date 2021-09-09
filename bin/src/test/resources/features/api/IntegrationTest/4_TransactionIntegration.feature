#@FirstTest
#@issue:VAP-6
Feature: In order to Earn points
As a customer 
I want to post earn transaction and cross verify in the system

Background: Enroll a member
 Given User wants to enroll a member
 When User perform enrollment for a member
 Then Member should be successfully enroll in the system with status code 200

 Scenario Outline: To verify that member post transaction and should be displayed in transaction history
	Given Member call the earn transaction
	When the member perfom earn transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should get points successfull with status code <status_code>
	And The transaction detail should be displayed in member trasaction history
	And The transaction reward history should be displayed
 		Examples:
		|sheetname|rownumber|status_code|
        |MembereTrx|0|200|
        
 Scenario Outline: To verify that member should get the same number of points as defined in the campaign 
	Given Member call the earn transaction 
	When The member perfom the transaction with price<price> and quantity<quantity>
	Then The the transaction should be succesful and points recived should be rewardpoints<rewardpoints> and campaigntype<pointtype> 
	
	Examples: 
		| price|quantity|rewardpoints| pointtype |
		| 10    |1       |11          |  "BONUS" |
		
Scenario Outline: To verify that the member should have the recent transactions in the deposit statement
	Given Member call the earn transaction
	When Member perfom the transaction with price<price> and quantity<quantity>
	Then Member should get points successfull with status code <status_code>
	And The transaction detail should be displayed in member deposit statement
 		Examples: 
		| price|quantity|status_code|
		| 5    |1       |200|
		
Scenario Outline: Member updated balance should be displayed deposit statement
	Given Member call the deposit statement to check balance
	When Member perfom deposit statement before transaction
	And Member perfom the transaction for price<price> and quantity<quantity>
	And Member check deposit statement after transaction
	Then The member updated balance should be displayed in the deposit statement
 		Examples: 
		| price|quantity|
		| 5    |1       |		