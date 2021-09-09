@membershipstatus
Feature: Verify membership status 

Background: Enroll a member
	Given member is enroll for membership status 
	When user perform enrollment of member for membership status 
	Then member is created for membership status with statuscode 200
	
Scenario: Member membership status
	Given user check the member status
	When user perform membership status of member
	Then member status is displayed successfully with status code 200
	
Scenario: Membership status for invalid member
	Given user check the member status
	When user perform membership status of invalid member
	Then user should not be able to see membership status with status code 404
	And user sees the error message as "No valid {entity} with key: '{key}'"
	
Scenario: Membership status for cancelled member
	Given user check the member status
	When user perform membership update for member as cancelled
	And user perform membership status of cancelled member
	Then membership update for member is successful with status code 200 
	And user should not be able to see membership status with status code 404
	And user sees the error message as "No valid {entity} with key: '{key}'"

Scenario: Membership status for blocked member
	Given user check the member status
	When user perform membership update for member as blocked 
	And user perform membership status of blocked member
	Then membership status is marked as blocked successful with status code 200  
	And user should not be able to see membership status with status code 404
	And user sees the error message as "No valid {entity} with key: '{key}'"
	
Scenario: Membership status for inactive member
	Given user check the member status
	When user perform membership update for member as inactive 
	And user perform membership status of inactive member
	Then membership status is marked as inactive successful with status code 200  
	And user should not be able to see membership status with status code 404
	And user sees the error message as "No valid {entity} with key: '{key}'"