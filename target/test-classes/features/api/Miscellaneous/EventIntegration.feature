@current 
Feature: to check the event listing of api 

Background: Enroll member for event 
	Given user check for member 
	When user perform enrollment for events 
	Then enrollment is successful with status code 200 
	
Scenario: events list 
	Given user check for event listing of api 
	When member perform event history 
	Then event history is displayed successfully with status code 200 
	
	
Scenario: post transaction and check event list 
	Given user check for event listing of api 
	When member perform event history 
	Then event history is displayed successfully with status code 200 

Scenario: To verify that the source member perform transactions
	Given user performs transactions
	When user perform earn transaction for events
	And member perform event history 
	Then member earn is successful with status code 200
	And event history is displayed successfully with status code 200 

Scenario: redemption and redemption reversal in event list
	Given user performs redemption and redemption reversal
	When user perform earn transaction for events
	And user perform redemption for events
	And user perform redemption reversal for events
	And member perform event history
	Then member earn is successful with status code 200 
	And redemption is successful with status code 200 
	And redemption reversal is successful with status code 200
	And event history is displayed successfully with status code 200				
