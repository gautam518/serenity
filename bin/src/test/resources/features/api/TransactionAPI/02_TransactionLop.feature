@FirstTest
@issue:VAP-6
Feature: As a customer i want to earn points by doing transaction 


Scenario: Enroll member into system to perform transaction
 Given User wants to enroll a member to perform transaction
 When User performs enrollment to do transaction
 Then User should be able to enroll member to do transaction successfully with status code 200
 
Scenario Outline: Member should earn points when perform transaction
	Given Member call the earn transaction
	When Member perfom transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should be able to get points successfully with status code <status_code>
 		Examples:
		|sheetname|rownumber|status_code|
        |MembereTrx|0|200|
        
Scenario Outline: To verify that when member number is passed as blank will give error message
    Given Member call the earn transaction when member is blank
	When the member perfom earn transaction with balnk member number in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and message<message>
 		Examples:
		|sheetname|rownumber|status_code|message|
        |MembereTrx|0|404|"No valid MembershipDoc with key: ''"|

Scenario Outline: To verify that when product key is passed as blank will give error message
    Given Member call the earn transaction when productkey is blank
	When the member perfom earn transaction when productkey is blank in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
	#And It should gives us the error message<errormessage>
 		Examples:
		|sheetname|rownumber|status_code|status|errormessage|
        |MembereTrx|0|400| "false"|"Missing product master data for channel: {channelKey} / ProductKey: {productKey}"|


Scenario Outline: To verify that when member forget to pass channelkey will give error message
    Given Member call the earn transaction channel key is not passed in transaction
	When the member perfom earn transaction when channel key is not passed in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
	And It should gives us the error message<errormessage>
 		Examples:
		|sheetname|rownumber|status_code|status|errormessage|
        |MembereTrx|0|400| "false"|"Missing channel master data for: {channelKey}"|

Scenario Outline: To verify that when amount is passed as zero in transaction is succesfull
    Given Member call the earn transaction with amount is passed as zero
	When the member perfom earn transaction when amount is passed as zero in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|200|"true"|

Scenario Outline: To verify that when amount is passed as negative in transaction is successfull
    Given Member call the earn transaction with negative amount in transaction
	When the member perfom earn transaction when negative amount is passed in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|200|"true"|

Scenario Outline: To verify that when member forget to pass amount in transaction is successfull
    Given Member call the earn transaction when amount is not passed in transaction
	When the member perfom earn transaction when amount is not passed in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|200|"true"|
        
Scenario Outline: To verify that when member forget to pass date in transaction is successfull
    Given Member call the earn transaction without date in transaction
	When the member perfom earn transaction without date in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|200|"true"|

Scenario Outline: To verify that when date is passed as blank in transaction will give error message
    Given Member call the earn transaction with blank date in transaction
	When the member perfom earn transaction with blank date in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
	And It should gives us the errormessage<errormessage>
 		Examples:
		|sheetname|rownumber|status_code|status|errormessage|
        |MembereTrx|0|400|"false"|"Invalid value for parameter 'Invalid transaction'"|
        
Scenario Outline: To verify that when duplicate transaction should not be processed
    Given Member call the earn transaction
	When the member perfom earn transaction for duplicate transactionid in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
	And It should gives us the duplicate message<errormessage>
 		Examples:
		|sheetname|rownumber|status_code|status|errormessage|
        |MembereTrx|0|400|"false"|"Transaction receipt {txId} already processed"|

Scenario Outline: To verify that transaction should not be processed for invalid date format
    Given Member call the earn transaction with invalid date format in transaction
	When the member perfom earn transaction with invalid date format in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|400|"false"|

Scenario Outline: To verify that special characters are allowed in transactionid
    Given Member call the earn transaction
	When Member perfom earn transaction with special characters in transactionid in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
 		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|400|"false"|
        
Scenario Outline: To verify that transaction should be processed when transactionid passed as blank
    Given Member call the earn transaction with blank transaction id in transaction
	When the member perfom earn transaction with blank transaction id in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|200|"true"|

Scenario Outline: To verify that whenTransaction should not be processed for invalid partner
    Given Member call the earn transaction with invalid partner value in transaction
	When the member perfom earn transaction with invalid partner value in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
	And It should gives us the invalid partner message<errormessage>
 		Examples:
		|sheetname|rownumber|status_code|status|errormessage|
        |MembereTrx|0|404|"false"|"Membership Key (or Alternate Id) '{membIdentifier}' does not exist"|
        

Scenario Outline: Transaction should not be successful when partner value is passed as blank in transaction
Given Member call the earn transaction with blank partner value in transaction
	When the member perfom earn transaction with blank partner value in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then It should be equal to status code <status_code> and status<status>
 		Examples:
		|sheetname|rownumber|status_code|status|
        |MembereTrx|0|400|"false"|

Scenario Outline: To verify that cancelled member should not be allowed to do transactions
	Given Member call the earn transaction for cancelled member
	When Member performs enrollment to do transaction and mark it cancelled
	And the member perfom earn transaction for cancelled member in transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should be created and marked as cancelled with status 200
	And It should be equal to status code <status_code> and status<status>
	And It should gives us the error message for cancelled member<errormessage>
 		Examples:
		|sheetname|rownumber|status_code|status|errormessage|
        |MembereTrx|0|404|"false"|"No valid MembershipDoc with key: 'cleientkey'"|       