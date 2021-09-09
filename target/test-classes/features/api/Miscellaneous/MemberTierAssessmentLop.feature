#@NewVersion
@current
Feature: Verify Tier Assessment
Background: Enroll a member
	Given member is enroll for Tier Assessment 
	When user perform enrollment for tier assessment 
	Then member is created for tier assessment successfull with statuscode 200
#Scenario: Enroll a member
#	Given member is enroll for Tier Assessment 
#	When user perform enrollment for tier assessment 
#	Then member is created for tier assessment successfull with statuscode 200

Scenario: direct tier from transaction
	Given member post transaction to update member tier
	When user perform earn transaction for member tier
	And user perform get member tier information
	Then earn transaction of member for tier is successfull with status code 200
	And member can see his tier detail succesfully with status code 200
	
Scenario: tier downgrade
	Given member post redemption to downgrade member tier
	When user perform earn transaction for member tier downgrade
	And user perform redemption for tier downgrade
	And user run the tier assessment job 
	And user perform get member tier information for downgrade
	Then earn transaction of member for tier downgrade is successfull with status code 200
	And redemption of member for tier is successfull with status code 200
	And tier assessment job run sucessfully with status code 200
	And member can see his tier downgrade detail succesfully with status code 200				
		
Scenario: give points to member
	Given member post transaction for tier assessment
	When user perform earn transaction for tier assessment of member
	And user perform get member details
	And user run the tier assessment job 
	And user again perform get member details
	Then earn transaction for tier assessment of member is successfull with status code 200
	And member details should be displayed successfull with statuscode 200 
	And tier assessment job run sucessfully with status code 200
	And the member can see his tier detail succesfully with status code 200 
		
Scenario: tier upgrade
	Given member post transaction to upgrade member tier
	When user perform transaction for tier upgrade
	And user again perform transaction for tier upgrade
	And user run the tier assessment job 
	And user perform get member tier information for upgrade
	Then transaction of member for tier is successfull with status code 200
	And next transaction of member for tier is successfull with status code 200
	And tier assessment job run sucessfully with status code 200
	And member can see his tier upgrade detail succesfully with status code 200

Scenario: tier upgrade to final tier
	Given member post transaction for member upgrade for final tier
	When user perform transaction for member upgrade for final tier
	And user run the tier assessment job 
	And user perform get member tier information of final tier
	Then transaction of member for final tier is successfull with status code 200
	And tier assessment job run sucessfully with status code 200
	And member can see his final tier status succesfully with status code 200	
	
	
	
	
	

	