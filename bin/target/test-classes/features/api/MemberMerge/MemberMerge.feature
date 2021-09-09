@functional 
Feature: In order to check member merge 
	As a customer 
I want to do member merge and cross verify in the system


Scenario: Enroll source and target member and add cards to source member 
	Given Source Member wants to enrollment on PCV2 
	And Target Member wants to enrollment on PCV2 
	When Source Member performs enrollment on PCV2 
	And Target Member performs enrollment on PCV2 
	And Source member perform create card 
	And Source member perform create leagel entity 
	And Source member perform create acrd with legal entity realtion 
	Then Source member should be created in pcv2 with statuscode 200
	And The source member key should be equal to input key value
	And Target member should be created in pcv2 with statuscode 200 
	And The target member key should be equal to input key value
	And Create card for source member should be successful with statuscode 200 
	And Leagel entity for source member should be successful with statuscode 200 
	And Create card and Legal entity relation for source member should be successful with statuscode 200 
	
Scenario: To verify that the source member perform transactions
	Given Member performs transaction in PCV2 as Source Member 
	When Source member call the earn transaction
	And Source member perform point redemption 
	And Source member perform redemption reversal
	And Source member perform point adjustment
	Then Point earn for the source member should be successful with status code 200
	And Point redemption should be successful for the source member with status code 200 
	And Redemption reversal for the source member is successful with status code 200
	And Point adjustment for the source member is successful with status code 200
	 
	
Scenario: To verify the soruce member balance after transactions 
	Given Source member wants to check the earn and redeem balance 
	When Source member call the earn and redeem balance 
	And Source member call the transaction and redeem history 
	And Target member call the earn and redeem balance 
	And Target member call the transaction and redeem history
	And Source member perofm generate a voucher token
	And Source member perform voucher issue 
	Then Earn and redeem balance of the source member should be disaplyed with status code<status_code> 
	And Earn and redeem balance of the target member should be disaplyed with status code<status_code> 
	And Source member transaction and redeem history should be displayed with status code 200 
	And Target member transaction and redeem history should be displayed with status code 200 
	And Voucher should be assign to source member successfully with status cod<status_code>
	Then Source member should be able to generate token successfully  with status code 200 
	
	#Chek member merge
Scenario: To verify that the member merge is working properly 
	Given Source member wants to merge with the target member 
	When Source member perform the member merge with the target member 
	Then Member merge should be successful with status code <statuscode> 
	
Scenario: To verify that the source member can do earn with source card after merging 
	Given Source member wants to ean points by doing transaction with source card 
	When Source member perform earn transaction with the source card 
	Then Earn transaction should not be successful for the source member with status code 404 
	
	#check balance and redeemable balance after merge
Scenario: To verify that the target member balance is equal to the sum of the balance of source and target member after merging 
	Given Target member wants to check balance 
	When Target member perform the check balance 
	Then The target member balance should be equal to the sum of balance of both source and target member 
	
Scenario: To verify that the target member redeemable balance will be sum of the redeemable balance of source and target member balance after merging 
	Given Target member wants to check redeemable balance 
	When Target member perform to check redeemable balance 
	Then The target member redeemable balance should be equal to the sum of redeemable balance of both source and target member 
	
	#Need to create these scenarios 
	#Scenrio:Source member can see transaction history after merge
	#Scanerio : Source member can see reward history after merge
	#Scanrio: source member can see voucher history after merge
	#scanrion : Source member could not seee his deposit statement	
	
Scenario: To verify that the source member can see transaction history after merging 
	Given Source member wants to see transaction history after merge 
	When Source member perform transaction history after merging 
	Then Source member should be able to see the transaction history successfully with status code 200 
	
Scenario: To verify that the source member can see reward history after merging 
	Given Source member wants to see reward history after merge 
	When Source member perform reward  history after merging 
	Then Source member should be able to see the reward history successfully with status code 200 
	
Scenario: To verify that the source member can not see deposit statement after merging 
	Given Source member wants to see deposit statement after merge 
	When Source member perform deposit statement after merging 
	Then Source member should not be able to see the deposit statement with status code 404 
	
	
	#transaction integration
Scenario: To verify that the taraget member contain the source member transaction after merging 
	Given Target member wants to check transaction history 
	When Target member perform the transaction history 
	Then Transaction history of the target member should include all the transaction detail of source member with status code 200 
	And Transaction history of the target member should contain the fields "transferredTo" and "transferReason" after merging 
	And Target member total transactions history count should be equal to the sum of the source and target member after merge 
	
Scenario: To verify that the reward history of the source member is transferred to target member after merge 
	Given Target member wants to check reward history 
	When Target member perform the reward history 
	Then Reward history of the target member should include all the transaction detail of source member with status code 200 
	And Reward history of the target member should contain the fields "transferredTo" and "transferReason" after merging 
	And Target member total redemption history count should be equal to the sum of the source and target member after merge 
	
	#ForVouchers
Scenario: To verify that the source vouchers are transaferred to target member after merge 
	Given Target member wants to see the vouchers 
	When Target member perform voucher histroy after merge 
	Then Target member should get all the vouchers history of the source member after merging 
	And Target member vouchers should have the same status as they have with the source member befor merging 
	
Scenario: To verify that the source member can see voucher history after merge 
	Given Source member wants to see his voucher history after merge 
	When Source member perform voucher histroy after merge 
	Then Source member should be able to see his vouchers history after merge with status code 200 
	
Scenario: To verify that the target member can convert the voucher issue by the source member
	Given Target member wants to convert the issued voucher of the source member
	When Target member perform the convert voucher
	Then Target member should be able to convert the voucher successfull with status code 200

Scenario: To verify that the token generated for a source voucher can be issued to target member after merge
	Given Target member want to issue a voucher after merge
	When Target member perform a issue token after merge
	Then Target member should be able to issue the voucher successfully with statuc code 200 		
	
#ForCards
Scenario: To verify the target member get all the source member cards after merge
	Given Target member wants to check cards and legal Entity infromation
	When Target member perform the cards history
	And Target member perform the Legal Entity history
	Then Target member should have all the source member cards in detail with status code 200
	Then Target member should have all the source member legal entity in detail with status code 200
	
Scenario: To verify that the altertnateIds merged correctly
    Given Target member wants to check alternateIds infromation
	When Target member perform the alternateIds infromation
	Then Target member should have all the source member alternateIds detail with status code 200
	
#ForEvents
#Check Events correctly merged to DEST
Scenario: To verify that the events merged to target member correctly
	Given Target member wants to check the event merge
	When Target member perform event history
	Then Target member should have all the events history with status code 200
	
Scenario: To verify that source member can see events history after merge
	Given Source member wants to check his event history
	When Source member perform event history
	Then Source member should be able to see all his event history after merge with status code 200
	
Scenario: Events count of source and target member is equal
	Given Target member wants to check his total event count
	When Target member perform event count in remark api
	Then Target member should have the same number of events count as of source member after merge with status code 200	
		
#Check remarks
Scenario: To verify that the member merge is successful
	Given Target member wants to check member merge
	When Target member perform check remarks
	Then Target member should be able to get successful remark for member merge with status code 200

#mislenous
Scenario: To verify that the source member's membership status is cancelled after merging
	Given Source member wants to see his membership status
	When Source member perform membership status 
	Then Source member should be able to see the membership status as cancelled with status code 200
	
Scenario: To verify that the source member can not earn point after merging
	Given Source member wants to earn points  
	When Source member performs earn points after merging
	Then Source member should not be able to earn point after merging and show status code 404


Scenario: To verify that the source member can not redeem points after merging
	Given Source member wants to redeem points 
	When Source member performs redeem points after merging
	Then Source member should not be able to redeem point after merging and show status code 404 

Scenario: To verify that the source member can not adjust points after merging 
	Given Source member wants to adjust points 
	When Source member performs adjust points after merging
    Then Source member should not be able to points adjustment after merging and show status code 404
    
Scenario: To verify that the source member can not reverse points after merging 
	Given Source member wants to reverse points 
	When Source member performs point reverse after merging
	Then Source member should not be able to do points reverse after merging and show status code 404   
	
#to check same member merge not work
#Scenario: Same members merge is not possible
#	Given Source member wants to merge with source member 
#	When Source member perform merge source member
#	Then Merging of same member should not be allowed and show status code 400
#	And Error message should be "Cannot perform merge, source and target must be different" 
	