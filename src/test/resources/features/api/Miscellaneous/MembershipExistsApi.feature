@current
Feature: Membership exists
	
Scenario: to check membership exists
	Given user check membership exists api
	When user perform member enrollmnet for membership exists
	And user performs get membership exists
	Then member enroll is successful for membership exists
	And member exists is successful with status code 200