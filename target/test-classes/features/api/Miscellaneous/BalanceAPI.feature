@current
Feature: balance api
	
Scenario: To get member balance api
	Given user check for member balance api
	When user perform member enrollmnet for balance
	And user performs points adjust for balance
	And user perform get member balance 
	Then member enroll is successful for member balance
	And point adjustment is succesful for member balance with status code 200
	And member balance is displayed successfully with status code 200