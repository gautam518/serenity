@current 
Feature: Voucher Issuing date to be set based on since date 

Scenario: Voucher Issuing date to be set based on since date 
	Given user check the vouchers for issue date 
	When user perform create voucher type for issue date 
	And user perform generate voucher for issue date
	And user perform enrollment of member for issue date
	And  user perform earn transaction for issue date
	And user perform voucher issue to member for issue date 
	And user perform the convert voucher for issue date 
	Then voucher type for issue date is created successfully with statuscode 200 
	And voucher for issue date is generated successfully with statuscode 200
	And member is created for issue date is successfull with statuscode 200
	And earn transaction for issue date is successfully with status code 200 
	And user issues the voucher for issue date successfully with statuscode 200 
	And convert voucher for issue date is successfull with statuscode 200		