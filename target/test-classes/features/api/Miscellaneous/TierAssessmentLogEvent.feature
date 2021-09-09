@current
Feature: Tier assessment log in event
	
Scenario: tier assessment log in event
	Given user check for member tier assessment log
	When user perform member enrollmnet for tier assement log
	And user perform earn transaction for tier assement log
	And user perform perform event history for tier assement log
	Then member enroll is successful for tier assement log
	And member transaction is succesful for tier assement log with status code 200
	And member event detail is displayed successfully with status code 200