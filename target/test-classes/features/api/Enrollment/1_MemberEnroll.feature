Feature: verify member enrollment

Scenario: verify all the request parameter in the Enrollment API
	Given user request the Enrollment API
	When user check the request with passing all the required parameter
	Then user check that enrollment should be done successfully with error code 200

Scenario: verify the mandatory field validation for the 'Key' parameter in the Enrollment API
	Given user requests the Enrollment API 
	When user check the response by passing 'key' value as null or blank
	Then user check that error message has been received with error code 400
	
Scenario: verify the mandatory field validation for the 'Type' parameter in the Enrollment API
	Given user requests the Enrollment API 
	When user check the response by passing 'Type' value as null or blank
	Then user check that error message has been received with error code 404
	
Scenario: verify the mandatory field validation for the 'Tier' parameter in the Enrollment API
	Given user requests the Enrollment API 
	When user check the response by passing 'Tier' value as null or blank
	Then user check that error message has been received with error code 404

#Scenario: validation for the 'AlternateIds' parameter in the Enrollment API
#	Given user requests the Enrollment API 
#	When user check the response by not defining 'AlternateIds' value
#	Then user check that error message has been received with error code 400

#Scenario: validation for the 'AlternateIds' parameter
#	Given user requests the Enrollment API 
#	When user check the response by not passing '{{$guid}}' value
#	Then user check that error message has been received with error code 400

#Scenario: verify the mandatory field validation for the 'AlternateIds' parameter in the Enrollment API
#	Given user requests the Enrollment API 
#	When user check the response by passing '[]' value
#	Then user check that member should get enrolled without alternateId with status code 400

Scenario: verify that PUT/memberships/{key}/details API update the details of member
	Given user requests the Enrollment API
	When user requests the Enrollment API for update
	When user update the value as address, name, DOB of new enrolled member using PUT API
	Then member is created successfully for update with status code 200
	And user check the updated details of member using GET/memberships API which should get updated

Scenario Outline:Pass all the value in request body and verify that the member is added successfully 
	Given user perform the Enrollment API
	When user create a new member by passing input data as firstName <firstname> lastName <lastname> email <email> key <key> tier <tier> userId <userId> alternateIds <alternateIds> 
	Then check that the member has been enrolled successfully with valid status code and with email <email> is created 
	
	Examples: 
		| firstname|lastname| email|key|tier|userId|alternateIds|
		| "TestName"|"TestLastName"|"test.emailtest@lp.com"|"Test_001"|"Basic"|"Test_001"|"TestAl_01"|	
		
Scenario Outline:Enroll members with different set of data so that we can use new membershipIds in integration scenarios 
	Given call the enrollment API again and again
	When check that all the new request added with all details <sheetname> and rownumber <rownumber> 
	Then member should be enrolled in the system succesfully with valid status code<status_code> 
	
	Examples: 
		|sheetname  |rownumber|status_code|
		|MembereTrx |0        |   "200"   |