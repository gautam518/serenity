Feature: As a customer i want to do points redemption by doing transaction
 
Scenario: Enroll member and post transactions
	Given Member wants to enrollment on PCV2 for redemption
	When Member performs enrollment on PCV2 for redemption
	And The member performs transaction on PCV2 for redemption
	Then Member should be created in pcv2 for redemption with statuscode 200
	And Then transaction should be successful with status code 200
	
Scenario Outline: To verify that member should able to do points redemption 
	Given Member call the point redemption
	When The member perfom point redemption with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do point redemption successfully with status code <status_code> 
	And The redemption detail should be displayed in member account statement
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|
		
Scenario Outline: To verify that the same number of points redeem from the redemption 
	Given Member call the point redemption 
	When The member perfom point redemption with details receiptid<receiptid> date<date> debitorId<debitorId> amount<amount> and description<description>
	Then Member should able to do point redemption successfully with status code <status_code>
	Examples: 
		|receiptid|date|debitorId| amount|description|status_code|
		|"2021-04-11T00:00:00.000ZtxRed"|"2021-04-11T00:00:00.000Z"|"SHOP_A1"|10|"5% discount voucher used [E45GH7815]"|200|	
		
#To verify the transaction redemption gives error code 400 when the recipt id is passed as blank		
Scenario Outline: To verify point redemption when the receiptid is passed as blank
	Given Member call the point redemption with blank receiptid
	When The member perfom point redemption when the receiptid is passed as blank in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|
		
Scenario Outline: To verify point redemption when any value of receiptid is passed
	Given Member call the point redemption for any value of receiptid
	When The member perform point redemption when any value of receiptid is passed in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|200|
		
Scenario Outline: To verify point redemption without receiptid
	Given Member call the point redemption without receiptid
	When The member perform point redemption without receiptid in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|500|
				
#To verify the transaction redemption gives error code 404 when type is not passed		
Scenario Outline: To verify point redemption when type is not passed 
	Given Member call the transaction redemption when type is not passed 
	When The member perfom point redemption without type in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|  
#To verify the transaction redemption gives error code 500 when amount is passed as alphabet		
Scenario Outline:To verify point redemption when amount is passed as alphabet
	Given Member call the point redemption with non numeric value
	When The member perform point redemption when amount is passed as alphabet in the system with details <sheetname> and rownumber <rownumber>  
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|  
					
#To verify the transaction redemption gives error code 400 when amount is passed as 0		
Scenario Outline: To verify point redemption when amount is passed as zero
	Given Member call the point redemption when amount is zero
	When The member perfom point redemption with amount as zero in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400| 
#To verify the transaction redemption gives error code 400 when amount is not passed		
Scenario Outline: To verify point redemption when amount is not passed
	Given Member call the point redemption without amount
	When The member perfom point redemption when amount is not passed in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|  
#To verify the transaction redemption gives 200 when debitorId is passed as blank		
Scenario Outline: To verify point redemption when debitorId is passed as blank
	Given Member call the point redemption when debitorId is blank
	When The member perfom point redemption when debitorId is passed as blank in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code>  
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|404|                 
		
Scenario Outline: To verify point redemption when debitorId is not passed 
	Given Member call the point redemption without debitorId
	When The member perform point reversal when debitorId is not passed in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|	
	
Scenario Outline: To verify point redemption when date is blank
	Given Member call the point redemption when date is blank
	When The member perfom point redemption when date is blank in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|
				
Scenario Outline: To verify point redemption invalid date is passed 
	Given Member call the point redemption with invalid date
	When The member perfom point redemption with invalid date in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|400|
		
Scenario Outline: To verify point redemption when type is passed as volume 
	Given Member call the point redemption with invalid type
	When The member perfom point redemption when type is passed as volume in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should not able to do point redemption with status code <status_code> 
	Examples: 
		|sheetname|rownumber|status_code|
		|MembereTrx|0|422|		