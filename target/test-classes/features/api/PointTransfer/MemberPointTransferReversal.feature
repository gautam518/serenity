@NewVersion
Feature: Point Transfer Reversal
Scenario: Enroll a source member
	Given source member is enroll for point transfer reversal 
	When user perform enrollment of source member for point transfer reversal
	Then source member is created for point transfer reversal with statuscode 200
		
Scenario: Enroll a target member
	Given target member is enroll for point transfer reversal 
	When user perform enrollment of target member for point transfer reversal
	Then target member is created for point transfer reversal with statuscode 200	
	
Scenario: Souce member post transactions
	Given source member post transaction for point transfer reversal
	When user perform earn transaction as source member for point transfer reversal
	Then earn transaction as source member for point transfer reversal is successfull with status code 200

Scenario: Points transfer is successful
	Given source member transfer points to target member
	When user perform point transfer for source to target member
	Then point transfer for source to target member is successful with status code 200
	 	
Scenario: Points transfer reversal for invalid TxId is unsuccessful
	Given points transfer reversal is not possible for invalid TxId 
	When user perform point transfer reversal for invalid TxId
	Then user should not be able to do point transfe reversal with status code 400
	#And the user sees a error message as "{txId}: Point redemption receipts cannot be reversed"
	And the user sees a error message as "{txId}: Point transfer receipts already adjusted or reversed or does not exists"

Scenario: Points transfer reversal for invalid points type is unsuccessful
	Given points transfer reversal is not possible for invalid points type 
	When user perform point transfer reversal for invalid points type
	Then user should not be able to do point transfe reversal with status code 404
	And the user sees a error message as "No account with points designation '{rewardType}' associated with '{mshipKey}'."
	#And the user sees a error message as "{txId}: Point transfer receipts already adjusted or reversed or does not exists"

Scenario: Points transfer reversal is successful
	Given points transfer reversal for source member is successful
	When user perform point transfer reversal for source member
	Then point transfer reversal is successful with status code 200
	And the user sees a success message and target member numbers with point type
	
Scenario: Source member can see account statement
	Given source member see his account statement 
	When user perform account statement for source member after point transfer reversal 
	Then source member can see his account statement after transfer reversal with status code 200 
	
Scenario: Target member can see account statement
	Given target member see his account statement 
	When user perform account statement for target member after point transfer reversal 
	Then user can see account statement for target member after transfer reversal with status code 200 
	And the user sees a success message with type as "CANCELLATION"

Scenario: Source member perform redemption-redemption reversal
	Given source member do redemption/redemption reversal 
	When user perform redemption for source member after point transfer reversal
	And user perform redemption reversal for source member after point transfer reversal
	Then redemption for source member after point transfer reversal is successful with status code 200
	And redemption reversal for source member after point transfer reversal is successful with status code 200
	
Scenario: Point transfer reversal with duplicate transactionId is not possible
	Given duplicate point transfer reversal is not possible for same txId
	When user perform point transfer reversal for same transactionId again
	Then point transfer reversal for duplicate transactionId should not be successful with status code 400
	And the user sees a error message as "Redemption {receiptId} already reversed"
	
Scenario Outline: Partial point transfer reversal is not allowed
	Given partial points transfer reversal is not possible
	When user perform enrollment for point transfer reversal of source member 
	And user perform enrollment for point transfer reversal of target member
	And user perform point adjustment for point transfer reversal of target member 
	And user perform point transfer of "50" points for partial point transfer reversal
	And user perform redemption of "10" points for partial point transfer reversal of target
	And user perform point transfer reversal of partial points source member
	Then source member is created successfully for point transfer reversal with statuscode 200
	And target member is created successfully for point transfer reversal with statuscode 200
	And target member point adjustment is successfull for point transfer reversal with statuscode 200
	And "50" points should be deducted from the source member balance Partial point transfer reversal
	And target member should be able to do redemption successfully for partial point transfer reversal with status code 200
	And source member should not be able to do partial point transfer reversal with status code 422
	And Response message template should show message <errormessage>
	 @current
     Examples: 
		|errormessage|
		|"Insufficient deposit to perform the operation, current balance: {balance}"|
		
    @NewVersion	
	Examples: 
		|errormessage|
		|"Insufficient deposit to perform the operation, current balance: {balance}"|
		#|"No account with points designation '{rewardType}' associated with '{mshipKey}'."|	
	
Scenario: Point transfer reversal is allowed after using points but adjusting the balance of target member
	Given Member wants to points transfer reversal
	When user perform point transfer of "50" points for source to target member
	And user perform redemption of "10" points for target member
	And user perform point adjustment of "30" points for target member
	And user perform point transfer reversal for source member after adjustment of points
	Then "50" points should be deducted from the source member balance 
	And target member should be able to do redemption successful with status code 200
	And Target member should be able to do point adjustment for point transfer reversal with status code 200
	And source member should be able to do point transfe reversal with status code 200
	
Scenario: Points transfer reversal is not possible for Cancel member
	Given point transfer reversal not possible for cancel member
	When user perform point transfer of "50" points for source to target member
	And user perform membership update for target member as cancel 
	And user perform point transfer reversal after updating membership status of target member
	Then "50" points should be deducted from the source member balance 
	Then membership update for target member is successful with status code 200 
	And Source member should not be able to do point transfe reversal with status code 400
	And the user sees a error message as "One of the memberships related to the points transfer: {txId} is no longer valid"					
	

