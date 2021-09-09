Feature: This feature is used to make transaction into pc v2 system and also to verify them.

Background: Enroll member into system
 Given User wants to enroll a member
 When User performs enrollment
 Then User should be able to enroll member successfully with status code 200

Scenario: User Should be able to post a new Transaction record in v2 system 
	Given User must have the valid Post Transaction request parameters and authorization APIkey 
	When User makes Transaction API Request using POST method, valid parameters and valid APIkey for clientId "M10_57719" and txId "tx_9008" 
	Then User should see success as response and transaction must be added to the system. 
	
	
Scenario Outline: User Should be able to verify the response on posting a new Transaction 
	Given User must have the valid Post Transaction request parameters and authorization APIkey 
	When User makes Transaction API Request using POST method, valid parameters and valid APIkey 
	Then User should receive the clientId as <clientId> and should have program as <program> in Response 
	
	Examples: check example1 values 
		| clientId        | program   |
		| "TestMember_132021" | "US-PRG" |
		
		
Scenario Outline: User should not be able to post Duplicate transactions in v2 system 
	Given User must have the valid Post Transaction request parameters and authorization APIkey 
	When User makes the POST  Transaction request for transaction ID <trxId> which is already existing in the system 
	Then User should not be able to post the trx and receive status as <success status> 
	
	Examples: check example1 values 
		| trxId                 | success status |
		| "txav_4001234"        | false          |
		
Scenario Outline: Member perofm transaction to earn points 
	Given Member call the earn transaction 
	When Add the earn transaction to the system with details <sheetname> and rownumber <rownumber> 
	Then The earn transaction should be successfull with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|
		
Scenario Outline: Member can do redeemption 
	Given Member call the redeemption 
	When Add new redeemption to the system with details <SheetName> and rownumber <RowNumber> 
	Then Redeemption should display with added details 
	
	Examples: 
		|SheetName|RowNumber|
		|MembereTrx|0|	
		
Scenario Outline: Member can do Adjustment 
	Given Member call the Adjustment 
	When Add member points adjustment 
	Then Adjustment should happens with valid data 
	
	Examples: 
		|SheetName|RowNumber|
		|MembereTrx|0|
		
Scenario: Member can do transaction reversal in the system 
	Given Member call the transaction reversal 
	When Member post the earn transaction in system with valid input
	And Member post the transaction reversal in system with valid input 
	Then Earn transaction should be successfull with status code 200 
	And Transaction reversal should be successfull with valid response
	
	
	