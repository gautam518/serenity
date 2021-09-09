@current
Feature: import for json file format

Scenario: Include points expiration estimation in event list
	Given user performs transaction
	When user perform enrollment
	And user perform earn transaction for response monitor
	And user perform account statement for respone monitor
	And member perform event history for response monitor
	Then member enrollment is successful with status code 200 
	And member earn for response monitor is successful with status code 200
	And account statement of member is successful with status code 200
	And event history of response monitor is displayed successfully with status code 200		