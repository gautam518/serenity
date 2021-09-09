@current 
Feature: to check the point transfer for accountids 

Scenario: Enroll member for point transfer for accountids
	Given user check for member for accountids
	When user perform enrollment for accountids as source member
	And user perform enrollment for accountids as target member
	And user performs points adjust for accountids of source member
	And user perform get accountids for source member
	And user perform point transfer from source to target member for accountids
	Then enrollment for accountids is successful with status code 200
	And enrollment for accountids of target member is successful with status code 200
	And point adjustment is succesful for source member with status code 200
	And accountids details is successful with status code 200
	And point transfer is successful with status code 200