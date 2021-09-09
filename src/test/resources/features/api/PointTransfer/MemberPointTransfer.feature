Feature: In order to check point transafer 
	As a customer 
I want to do point transfer and cross verify in the system

Scenario: Enroll members in the system and post transactions for the members 
	Given Enroll member in the PCV2 as source member
	And Enroll member in the PCV2 as target member
	When Member performs enrollment in PCV2 as source member
	And Member performs enrollment in PCV2 as target member
	And Member call the earn transaction as source member 
	Then Member should be created in PCV2 as source member with statuscode 200 
	And Member should be created in PCV2 as target member with statuscode 200 
	And Member point earn for the source member should be successful with status code 200 
	
	
Scenario: Source and target member check the account statement before point transfer 
	Given Source member wants to see account statement before point transfer 
	When Source member perform account statement before point transfer
	And Target member perform account statement before point transfer 
	Then Source member should be able to see the account statement before point transfer with status code 200 
	And Target member should be able to see the account statement before point transfer with status code 200

Scenario: Check point transfer source to target and target to source
	Given Source member wants to do point transfer
	When Source member perform point transafer to target member for "10" points
	And Source member again perform point transafer to target member "5"  points
	And Target member perform point transafer to source member for "5" points
	Then "10" points should be deducted from the source member original balance
	And Again "5" should be deducted from the source member original balance
	And Target member balance should be "5" points less from the original balance	
	
Scenario: Expiry of points persistance is successful
	Given Member wants to transfer points in PCV2
	When Member perform point transfer from source to target member
	Then Point transfer should be successful with status code 200
	And The response message template should contain the source and target member numbers with point type	 
	
Scenario: To verify that the member cannot transfer the Blocked points 
	Given Members want to transfer blocked points PCV2 
	When Member perform the point transfer for blocked points type
	Then Member should be able to transfer blocked points type with status code 200. 
	#And The message template is ""
@NewVersion	
Scenario: Member cannot transfer points between US and DE program
	Given Members want to transfer point from US to DE program 
	When Member performs enrollment in PCV2 as for US program 
	And Member perform the point transfer from US to DE program 
	Then Member should be created in PCV2 for US program with statuscode 200 
	And Member should not be able to transfer points with status code 400
	And Message teamplate should display the error message "Invalid value for parameter 'Provided memberships are not of the same type'"
	
Scenario: Source member can see account statement after point transfer 
	Given Source member wants to see account statement after point transfer 
	When Source member perform account statement after point transfer 
	Then Source member should be able to see the account statement after point transfer with status code 200 
	
Scenario: Target member can see account statement after point transfer 
	Given Target member wants to see account statement after point transfer 
	When Target member perform account statement after point transfer 
	Then Target member should be able to see the account statement after point transfer with status code 200 
	And This should show the message template with type as "TRANSFER"
@NewVersion
Scenario: Member cannot transfer points between US and DE member type 
	Given Members want to transfer point of diffrent member type 
	When Member perform the point transfer diffrent member type 
	Then Member should not be able to transfer points with status code 400
	And Message teamplate should display the error message "Invalid value for parameter 'Provided memberships are not of the same type'"
	
@NewVersion
Scenario: Target member can not reverse point after point transfer 
	Given Target member wants to do the points reversal after point transfer 
	When Target member perform points reversal after point transfer 
	Then Target member should not be able to reverse point after point transfer and show status code 400 
	And Message teamplate should display the message "The transaction {txId} is not associated with client {clientId}."
	
Scenario: Target member can do redemption reversal after point transfer 
	Given Target member wants to redemption reversal after point transfer 
	When source member perform point redemption 
	And Target member performs redemption reversal after point transfer 
	Then Source member point redemption should be successful with status code 200
	And Target member should be able to do redemption reversal after point transfer with status code 200
	
Scenario: To verify that target member can transfer points to source member 
	Given Target member wants to tranfer points to source member 
	When Target member perform point transfer to source member 
	Then Target member should be able to transfer points successfully with status code 200

Scenario: Events merged to target member correctly after point transfer
	Given Target member wants to check the event after point transfer
	When Target member perform event history after point transfer
	Then Target member should have all events history after point transfe with status code 200
	#And Target member should contain the fields "transferredTo" and "transferredFrom" after point transfer 