@current
Feature: full membership import for json file format
	
Scenario: full membership import
	Given user check for bulk import for full membership 
	When user perform the bulk import for full membership
	Then bulk import for full membership file is successfully with status code 200
	#And user check the member bulk import file records exists
	
Scenario: check import processed
	Given user check for import processed for full membership 
	When user perform check import processed for full membership 
	Then import processed is successfully with status code 200
	
Scenario: check membership details
	Given user check for member information 
	When user perform get member details of imported member
	Then member information should be displayed successfully with status code 200	