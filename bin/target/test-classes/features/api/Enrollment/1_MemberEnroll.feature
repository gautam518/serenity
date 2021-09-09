Feature: This feature is used to add a new member in pcv2 system and also to verify that the member ia added successfully

Scenario: User calls the Enrollment API without passing the key parameter 
	Given User requests the Enrollment API 
	When User did not pass the key parameter in the API request body 
	Then the enrollment should not be successful with error code 400 
	
Scenario: User calls the Enrollment API without passing the type parameter 
	Given User requests the Enrollment API 
	When User did not pass the type parameter in the API request body 
	Then Enrollment should not be successful with error code 404
	
Scenario: User calls the Enrollment API without passing the tier parameter 
	Given User requests the Enrollment API 
	When User did not pass the userid parameter in the API request body 
	Then the enrollment should not be successful with error code 400 
	
	
Scenario Outline:Add a new member and verify that the member is added successfully 
	Given User perform the Enrollment
	When User create a new member by following input data as firstName <firstname> lastName <lastname> email <email> key <key> tier <tier> userid <userid> alternateIds <alternateids> 
	Then User verify that the member is added successfully with valida status code and with email <email> is created 
	
	Examples: 
		| firstname|lastname| email|key|tier|userid|alternateids|
		| "TestName"|"TestLastName"|"test.emailtest@lp.com"|"Test_001"|"Basic"|"Test_001"|"TestAl_01"|	
		
		
Scenario Outline:Create member with different set of data so that we can use new enrollments in integration scanrios 
	Given Member call the enrollment 
	When Add a new member in the system with details <sheetname> and rownumber <rownumber> 
	Then Member should be enrolled in the system succesfully with valid status code<status_code> 
	
	Examples: 
		|sheetname  |rownumber|status_code|
		|MembereTrx |0        |   "200"   |