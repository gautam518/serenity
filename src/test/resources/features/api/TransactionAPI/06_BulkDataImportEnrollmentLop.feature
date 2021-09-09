Feature: As a customer i want to check bulk import enrollment for json and csv file format

Scenario: To verify the bulk import is working for json file 
	Given Member call the bulk import for valid json file 
	When The member perfom bulk import for json file with valid file format 
	Then File should be processed in the system with status code 200
	And To verify the Json file records exists in the system
	
Scenario: To verify bulk import should not process for invalid json data 
	Given Member call the bulk import for invalid json data in file 
	When The member perfom bulk import for invalid json data in the file 
	Then The file should not be processed in the system with status code 500 
	
Scenario: To verify bulk import for same json file should not be allowded in the system 
	Given Member call the bulk import for same json file 
	When The member perfom bulk import for duplicate json file in system 
	Then The file should not be processed in the system with status code 200 
	
Scenario: To verify bulk import for blank json file should not be allowded in the system 
	Given Member call the bulk import for blank json file 
	When The member perfom bulk import for blank json file 
	Then File should not be processed in the system with status code 404 
	
Scenario: To verify bulk import for different file format should not be allowded in the system 
	Given Member call the bulk import for different file except json 
	When The member perfom bulk import for invalid json file in the system 
	Then File should not be processed in the system with status code 500 
	
	# for csv starts here	
Scenario: To verify the bulk import is working for csv format with valid csv file 
	Given Member call the bulk import for valid csv file 
	When The member perfom bulk import for csv file with valid file format 
	Then File should be processed in the system with status code 200
	And To verify the csv file records exists in the system 
	
Scenario: To verify bulk import for blank csv file should not be allowded in the system 
	Given Member call the bulk import for blank csv file 
	When The member perfom bulk import for blank csv file in the system
	Then File should not be processed in the system with status code 404 
	
Scenario: To verify bulk import for different file format should not be allowded in the system 
	Given Member call the bulk import for different file except csv 
	When The member perfom bulk import for invalid csv file format in the system 
	Then File should not be processed in the system with status code 500 
	
Scenario: To verify bulk import should not process for invalid csv file data 
	Given Member call the bulk import for invalid csv file data 
	When The member perfom bulk import for invalid csv file data in the system 
	Then The file should not be processed in the system with status code 500 
	
Scenario: To verify bulk import for does't accept duplicate file in the system 
	Given Member call the bulk import for duplicate csv file 
	When The member perfom bulk import for duplicate csv file in system 
	Then The file should not be processed in the system with status code 200
	
Scenario: Bulk import is working for csv file with multiple record data 
	Given bulk import for csv file with multiple data
	When user perfom bulk import for csv file with multiple data
	Then user can see the file should be processed in the system with status code 200
	And user can verify file record exists in the system 	 
		