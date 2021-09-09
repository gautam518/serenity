@current
Feature: import for point transfer
	
Scenario: bulk import for point transfer
	Given user check for bulk import of point transfer
	When user perform member enrollmnet for soource for bulk import of point transfer
	And user performs points adjust for bulk import of point transfer
	And user perform member enrollmnet for target for bulk import of point transfer
	And user perform the bulk import of point transfer
	Then source member is enroll successfully for bulk import of point transfer
	And point adjustment is succesful for point transfer with status code 200
	And target member is enroll successfully for bulk import of point transfer
	And bulk import of point transfer is successfully with status code 200
	And bulk import point transfer records exists