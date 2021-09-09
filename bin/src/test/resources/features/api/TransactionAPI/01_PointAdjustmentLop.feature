Feature: To verify the point adjustment is working properly for a member 


Scenario: Enroll a member for point adjustment
 Given Member wants to enroll a member for point adjustment 
 When Member perform enrollment for point adjustment 
 Then Member should be able to enroll member with status code 200
 
Scenario Outline: Point adjustment is working properly 
	Given Member perform point adjustment 
	When Member performs adjust points with date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the successfull message for points adjustment with statuscode <statuscode> 
	
	Examples: 
		|  date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 200|
		
		
Scenario Outline: Point adjustment for a negative amount is working properly 
	Given Member perform point adjustment 
	When Member performs point adjustment for negative amount with date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the successfull message for points adjustment with statuscode <statuscode> 
	Examples: 
		|  date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|-50.0|"Test Adjustment"| 200|
		
Scenario Outline: Point adjustment without passing receiptId should give error message 
	Given Member perform point adjustment 
	When Member performs point adjustment without receiptId and with date <date> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the erorr message for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     |debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"|"SHOP_A1"|50.0|"Test Adjustment"| 400|
		
		
Scenario Outline: Point adjustment without passing date is successful 
	Given Member perform point adjustment 
	When Member performs points adjustment without date and with parameters receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the successful message for points adjustment with statuscode <statuscode> 
	Examples: 
		| receiptId|debitorId|amount|description      |statuscode|
		| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 200|
		
Scenario Outline: Point adjustment without amount is successful 
	Given Member perform point adjustment 
	When Member performs points adjustment without amount and with parameters date <date> receiptId <receiptId> debitorId <debitorId> and description <description> 
	Then Member should get the successful message for points adjustment with statuscode <statuscode> 
	Examples: 
		|  date                     | receiptId|debitorId|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|"Test Adjustment"| 200|
		
Scenario Outline: Point adjustment when member number is not passed in parameter gives error message 
	Given Member perform point adjustment without member number in parameter 
	When Member performs points adjustment without member number and with parameters date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 404|
		
		
Scenario Outline: Point adjustment gives error when type is not passed in parameter 
	Given Member perform point adjustment without type in parameter 
	When Member performs points adjustment without type and with parameters date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		|  date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 404|
		
		
Scenario Outline: Point adjustment gives us bad request when invalid type and numeric values in parameter 
	Given Member perform point adjustment with innvalid type in parameter 
	When Member performs points adjustment with invalid type and having parameters date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 500|
		
		
Scenario Outline: Point adjustment gives bad request when passsed an invalid member number 
	Given Member perform point adjustment with invalid member number in parameter 
	When Member performs points adjustment with invalid member number and date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 404|
		
Scenario Outline: Point adjustment should not happens if the amount passed is in negative and greater than member account balance 
	Given Member perform point adjustment 
	When Member perform check account balance befor adjustment
	And Member performs points adjustment with negative amount and greater than balance and with parameters date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should be able to get points for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|-110.0|"Test Adjustment"| 200|
		
		
Scenario Outline: Point adjustment with passing any value (including special keyword) in receiptId is successful 
	Given Member perform point adjustment 
	When Member performs points adjustment with passing any value in receiptId and with date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "###@adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 200|
		
		
		
Scenario Outline: Points adjustment is happening properly check member history and point redeem 
	Given Member perform point adjustment 
	When Member performs adjust points with date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	And Check member history and point redeemption 
	Then Member should get the successful message for points adjustment with statuscode <statuscode> 
	Examples: 
		|  date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 200|
		
Scenario Outline: To verify the target member get the same point balance as the source member after merge 
	Given Member perform point adjustment 
	When Member perform enrollment for source member 
	And Member perform member merge from source to target member
	And Target performs adjust points with date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	And Check target member point history 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		|  date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 200|		
		
		
Scenario Outline: Cancelled member should not be able to do point adjustment 
	Given Member perform point adjustment for cancelled member
	When Member performs adjust points for cancelled member with date <date> receiptId <receiptId> debitorId <debitorId> amount <amount> and description <description> 
	Then Member should get the error message for points adjustment with statuscode <statuscode> 
	Examples: 
		| date                     | receiptId|debitorId|amount|description      |statuscode|
		| "2020-10-10T00:00:00.000Z"| "adj1"   |"SHOP_A1"|50.0|"Test Adjustment"| 404|
