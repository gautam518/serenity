Feature: Verify membership status 

Background: Enroll a member
	Given member is enroll for membership status 
	When user perform enrollment of member
	And user perform point adjustment 
	Then member is created successfull with statuscode 200
	And member should get the points adjustment successfull with statuscode 200 
	
Scenario: earn transation
	Given user check the earn transaction
	When user perform earn transaction
	Then member should be able to see earn transaction successfully with status code 200
	
Scenario: redemption of points for negative balance
	Given user check the point redemption
	When user perform point redemption 
	Then user should not be able to see member redemption with status code 422
	
#Scenario: redemption reversal of points for negative balance
#	When user perform point redemption 
#	And user perform point redemption reversal  
#	Then user should not be able to see member redemption with status code 422
#	And user should not be able to see member redemption reversal with status code 404	
	
Scenario: point transafer for negative balance
	Given user check the point transfer 
	When user perform enrollment for target member
	And user perform point transfer
	Then target member is created successfull with statuscode 200
	And user should not able to do point transfer with status code 422
	
#member merge
Scenario: member merge for negative balance
	Given user check the member merge  
	When user perform enrollment for target member
	And user perform member merge
	Then target member is created successfull with statuscode 200
	And user should not able to do member merge with status code 200
	
#vouchers
Scenario: Voucher for negative balance
    Given user check the vouchers  
	When user perofm create voucher type to member
	And user perofm generate voucher 
	When user perofm voucher issue to member
	And user perform the convert voucher 
	Then voucher type is created successfully with statuscode 200
	And voucher is generated successfully with statuscode 200
	And user issues the voucher successfully with statuscode 200
	And convert voucher is successfull with statuscode 200
	
Scenario: Cancel member balance
    Given user check the member balance
	When user perform membership update of member
	Then membership update of member is successful with status code 200 
	And user should not be able to see member detail with status code 404
	#And user sees the error message as "No valid {entity} with key: '{key}'"	