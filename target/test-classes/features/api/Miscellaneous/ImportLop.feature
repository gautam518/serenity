#@NewVersion
@current
Feature: import for json file format
	
Scenario: bulk import for member
	Given user check for bulk import of member json file
	When user perform the bulk import of member json file
	Then bulk import of member json file is successfully with status code 200
	And member bulk import file records exists
	
Scenario: bulk import for transaction
	Given user check for bulk import of transaction json file
	When user perform the bulk import of transaction json file
	And user perform the transaction history
	Then bulk import of transaction json file is successfully with status code 200
	And transaction history is displayed successfull with transaction detail

Scenario: bulk import for product
	Given user check for bulk import of product json file
	When user perform the bulk import of product json file
	And user perform the products detail
	Then bulk import of product json file is successfully with status code 200
	And product information is displayed successfully with status code 200
	
Scenario: bulk import for voucher type
	Given user check for bulk import of voucher type json file
	When user perform the bulk import of voucher type json file
	#And user perform the products detail
	Then voucher type bulk import of json file is successfully with status code 200
	#And product information is displayed successfully with  status code 200
	
Scenario: bulk import for vouchers
	Given user check for bulk import of voucher json file
	When user perform the bulk import of voucher json file
	#And user perform the products detail
	Then bulk import of voucher json file is successfully with status code 200
	#And product information is displayed successfully with  status code 200
	
Scenario: bulk import for card
	Given user check for bulk import of card json file
	When user perform create legal entity for member card
	And enroll a member for card import
	And user perform the bulk import of card json file
	Then legal entity for member is created successfully with status code 200
	And member ernrollment is successful with status code 200
	And bulk import of card json file is successfully with status code 200	
				