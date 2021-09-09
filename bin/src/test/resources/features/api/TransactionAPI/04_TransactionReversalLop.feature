Feature: As a customer i want to do points reversal by doing transaction 

Background: Enroll member and post transactions for reversal
 Given Member wants to enrollment for transaction reversal
 When Member perform to enrollment for transaction reversal
 And Member perform the earn transaction in the system
 Then Member should be enroll successfully for transaction reversal with statuscode 200
 And Member should able to do earn transaction with status code 200
 
   
Scenario Outline: To verify that member should able to do points reversal 
	Given Member call the transaction reversal
	When The member perfom point reversal transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do point reversal successfull with status code <status_code>
	And It should gives us the success message<message>
	Examples: 
		|sheetname|rownumber|status_code|message|
		|MembereTrx|0|200|"txId-REVERSAL"|
		
Scenario Outline: Member id is not passed in the point reversal 
	Given Member call the transaction reversal
	When The member perfom point reversal without member id in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code>
	And This should gives us the error message<errormessage>
	
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|400|"Missing required parameter 'ClientId'"|
		
Scenario Outline: Member id is blank in the point reversal 
	Given Member call the transaction reversal
	When The member perform point reversal  for blank memberid in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code> 
	And Also it should gives us the error message<errormessage>
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|404|"Membership Key (or Alternate Id) '{membIdentifier}' does not exist"|
		
Scenario Outline: Transaction id is not passed in the point reversal 
	Given Member call the transaction reversal 
	When The member perfom point reversal without transaction id in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code>
	And This should gives us the error message<errormessage>
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|400|"Missing required parameter 'TxId'" |
		
Scenario Outline: Transaction id is blank in the point reversal 
	Given Member call the transaction reversal 
	When The member perform point reversal for blank transaction id in the system with details <sheetname> and rownumber <rownumber>  
	Then Member should not able to do point reversal with status code <status_code>
	And Also it should gives us the error message<errormessage>
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|400|"The transaction {txId} is not associated with client {clientId}."  |
					
		
Scenario Outline: Point reversal with invalid transactionid 
	Given Member call the transaction reversal 
	When The member perfom point reversal with invalid transactionid in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code>
	And It should gives us the error message for invalid transactionid<errormessage>
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|400| "The transaction {txId} is not associated with client {clientId}."|
Scenario Outline: Point reversal with invalid member id 
	Given Member call the transaction reversal 
	When The member perfom point reversal with invalid member id in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code>
	And It should gives us the error message for invalid member<errormessage> 
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|404|"Membership Key (or Alternate Id) '{membIdentifier}' does not exist"|  
		
Scenario Outline: Point reversal with same transactionid and date is successful(duplicate reversal) 
	Given Member call the transaction reversal 
	When The member perfom point reversal with same transactionid and date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do point reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|                 
		
Scenario Outline: Point reversal when the date is as blank 
	Given Member call the transaction reversal
	When The member perform point reversal with balnk date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code> 
	And Also it should gives us the error message<errormessage>
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|500| "Unsupported internal state - please check with log."|

Scenario Outline: Point reversal without date 
	Given Member call the transaction reversal
	When The member perfom point reversal without date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|
		
Scenario Outline: Point reversal with invalid date format 
	Given Member call the transaction reversal 
	When The member perfom point reversal with invalid date format in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point reversal with status code <status_code> 
	And Also it should gives us the error message<errormessage>
	Examples: 
		|sheetname|rownumber|status_code|errormessage|
		|MembereTrx|0|500|"Unsupported internal state - please check with log."|
		
Scenario Outline: Point reversal is successful when different date is passed insted of same transaction date 
	Given Member call the transaction reversal 
	When The member perfom point reversal with diffrent transactionid date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do point reversal with status code <status_code> 
	And It should gives us the success message<message>
	Examples: 
		|sheetname|rownumber|status_code|message|
		|MembereTrx|0|200|"txId-REVERSAL"|