Feature: As a customer i want to do redemption reversal

#Background: Member do redeem points transaction
 #Given Member call the points redeem 
 #When  Member perform the point redeem in the system
 #Then Member should be able to redeem points with status code 200
 
Background: Enroll member and post transactions
	Given Member wants to enrollment for redemption reversal
	When Member performs enrollment for redemption reversal
	And Member performs transaction for redemption reversal
	And Member perform the point redeem in the system
	Then Member should be created for redemption reversal with statuscode 200
	And Transaction should be successful for redemption reversal with status code 200
	And Member should be able to redeem points with status code 200 

Scenario Outline: To verify that member should able to do redemption reversal 
	Given Member call the redemption reversal
	When The member perfom redemption reversal transaction in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do redemption reversal successfull with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|
		
Scenario Outline: Duplicate redemption reversal in the system
	Given Member call the redemption reversal
	When The member perform redemption reversal duplicate redemption reversal in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not be able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|
		
Scenario Outline: Invalid point type is passed in the redemption reversal 
	Given Member call the redemption reversal
	When The member perfom redemption reversal with invalid type in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not be able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|
	
Scenario Outline: To verify the redemption reversal when diffrent transactionid is passed
	Given Member call the redemption reversal 
	When The member perfom redemption reversal with diffrent transactionid in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|  
	
Scenario Outline: Transactionid is blank in the redemption reversal 
	Given Member call the redemption reversal
	When The member perform redemption reversal for blank transactionid in the system with details <sheetname> and rownumber <rownumber>  
	Then Member should not able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|  
					
	
Scenario Outline: Redemption reversal with invalid transactionid 
	Given Member call the redemption reversal
	When The member perfom redemption reversal with invalid transactionid in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404| 
		
#Scenario Outline: Redemption reversal with same transactionid and date is successful(duplicate redemption reversal) 
#	Given Member call the redemption reversal
#	When The member perfom redemption reversal with same transactionid and date in the system with details <sheetname> and rownumber <rownumber> 
#	Then Member should able to do redemption reversal with status code <status_code> 
#	Examples: 
#		|sheetname|rownumber|status_code|
#		|MembereTrx|0|500|
		

	
Scenario Outline: Redemption reversal when the date is as blank 
	Given Member call the redemption reversal
	When The member perform redemption reversal with blank date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not be able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|500|

		

Scenario Outline: Redemption reversal without date 
	Given Member call the redemption reversal
	When The member perfom redemption reversal without date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do redemption reversal successfull with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|
		
Scenario Outline: Redemption reversal with invalid date format 
	Given Member call the redemption reversal 
	When The member perfom redemption reversal with invalid date format in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|500|
		
Scenario Outline: Redemption reversal without point type
	Given Member call the redemption reversal 
	When The member perfom redemption reversal without point type in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do redemption reversal with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|

Scenario Outline: Redemption reversal with different point type
	Given Member call the redemption reversal 
	When The member perfom redemption reversal with different point type in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do redemption reversal with status code <status_code>  
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|