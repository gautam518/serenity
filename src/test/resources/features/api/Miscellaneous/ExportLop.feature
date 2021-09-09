#@NewVersion
@current
Feature: export json file format
	
Scenario: member export
	Given user check for member export
	When user perform enrollment of a member for export
	And user perform export
	Then member is enrolled successfully for export with status code 200
	And export is successfull with status code 200
	
Scenario: transaction export
	Given user check for transaction export
	When user perform enrollment of a member for export
	And user perform transaction for export
	And user perform transaction export
	Then member is enrolled successfully for export with status code 200
	And transaction is successfully for export with status code 200
	And transaction export is successfull with status code 200	