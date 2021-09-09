Feature: Verify Point Expiry 

Background: Enroll a member
	Given member is enroll for point expiry 
	When user perform enrollment of member for point expiry
	Then member is created for point expiry with statuscode 200
	
Scenario: Member earn point expiry 
	Given member post transaction for point expiry 
	When user perform earn transaction of member for point expiry
	And user perform account statement for member
	And user run the point expiry job
	Then earn transaction of member for point expiry is successfull with status code 200
	And account satement of member is displayed successfully with status code 200
	And the point expiry job run sucessfully with status code 200
	And the member can see his account statement for earn transaction succesfully with status code 200

Scenario: Member point adjustment point expiry 
	Given member post point adjustment for point expiry 
	When user perform point adjustment of member for point expiry 
	And user perform account statement for member
	And user run the point expiry job
	Then point adjustment of member for point expiry is successfull with status code 200
	And account satement of member is displayed successfully with status code 200
	And the point expiry job run sucessfully with status code 200
	And the member can see his account statement for point adjustment  succesfully with status code 200
	 	
Scenario: Member point redemption point expiry 
	Given member post point redemption for point expiry
	When user perform earn transaction of member for point expiry 
	And user perform account statement for member	
	And user perform point redemption for point expiry
	And user run the point expiry job
	Then earn transaction of member for point expiry is successfull with status code 200
	And account satement of member is displayed successfully with status code 200
	And point redemption for point expiry is successfull with status code 200
	And the point expiry job run sucessfully with status code 200
	And member can see his account statement succesfully with status code 200

Scenario: Member point redemption reversal point expiry 
	Given member post point redemption for point expiry
	When user perform earn transaction of member for point expiry
	And user perform account statement for member 	
	And user perform point redemption of member for point expiry
	And user perform point redemption reversal of member for point expiry 	
	And user run the point expiry job
	Then earn transaction of member for point expiry is successfull with status code 200
	And account satement of member is displayed successfully with status code 200
	And point redemption of member for point expiry is successfull with status code 200
	And point redemption reversal of member for point expiry is successfull with status code 200
   And the point expiry job run sucessfully with status code 200
	And the member can see his account statement succesfully with status code 200

	
Scenario: Member merge point expiry
	Given member member merge for point expiry
	When user perform enrollment of member for point expiry as target
	And user perform earn transaction of member for point expiry 
	And user perform member merge for point expiry
	And user run the point expiry job for target member
	Then target member is created for point expiry with statuscode 200
	And earn transaction of member for point expiry is successfull with status code 200
	And member merge is successfull with status code 200
	And the point expiry job for target member run sucessfully with status code 200
	And the target member can see his account statement succesfully with status code 200
	
Scenario: Point expiry of in case of point transfer is successful
	Given member point expiry for points transfer
	When user perform enrollment of target member for point expiry as target
	And user perform earn transaction of member for point expiry
	And user perform account statement for member  
	And user perform point transfer from source to target member for point expiry
	And user run the point expiry job for point transfer
	Then target member is created for point expiry with statuscode 200
	And earn transaction of member for point expiry is successfull with status code 200
	And account satement of member is displayed successfully with status code 200
	And point transfer is successfull with status code 200
	And the point expiry job for point transfer run sucessfully with status code 200
	And the members can see his account statement succesfully with status code 200	

Scenario Outline: Point expiry in diffrent date range
	Given member expiry in diffrent date range
	When user perform earn transaction on txdate<txdate> for point expiry  
	And user perform redemption on date<date> for point expiry 
	And user perform redemption reversal on date<txdate> for point expiry
	And user run the point expiry job
	Then earn transaction of member for point expiry is successfull with status code 200 
	And redemption of member for point expiry is successfull with status code 200
	And redemption reversal of member for point expiry is successfull with status code 200
	And the point expiry job run sucessfully with status code 200
	And the member can see his account statement succesfully with status code 200
	Examples: 
		| txdate|date|
		| "2021-03-24T00:00:00.000Z"|"2021-03-24T00:00:00.000Z"|
		
Scenario: Points expiry is not possible for cancel member
	Given point expiry is not possible for cancel member
	When user perform earn transaction of member for point expiry 
	And user perform membership update for the member as cancel 
	And user run the point expiry job
	Then earn transaction of member for point expiry is successfull with status code 200
	And membership update for the member is successful with status code 200 
	And the point expiry job run sucessfully with status code 200
	And the member can not see his account statement with status code 404
	
Scenario Outline: Point expiry when multiple earn transactions posted in diffrent date range
	Given member expiry in multiple date range in month
	When user perform earn transaction of txdate<txdate> and <point>points for point expiry for first time
	And user perform account statement for member multiple transactions
	And user perform earn transaction of txdate<date> and <point>points for point expiry for second time
	And user perform earn transaction of txdate<date> and <point>points for point expiry for third time
	And user perform redemption on date<date> and <point>points for point expiry
	And user run the point expiry job for multiple transactions
	Then first earn transaction of member for point expiry is successfull with status code 200
	And account satement of member for multiple transactions is displayed successfully with status code 200  
	And second earn transaction of member for point expiry is successfull with status code 200
	And third earn transaction of member for point expiry is successfull with status code 200  
	And redemption of member for point expiry is successfull with status code 200
	And the point expiry job for multiple transactions run sucessfully with status code 200
	And the member can see his all transactions account statement succesfully with status code 200
	Examples: 
		| txdate|date|point|
		#| "2021-03-24T00:00:00.000Z"|"2021-05-24T00:00:00.000Z"|21|	
		| "2021-06-04T00:00:00.000Z"|"2021-07-24T00:00:00.000Z"|21|	
		
		