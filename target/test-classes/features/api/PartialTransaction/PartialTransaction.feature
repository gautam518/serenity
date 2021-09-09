@PartialTest
Feature: In order to check partial transaction 
	As a customer 
I want to do partial transaction

Background: Enroll member into system
 Given Enroll a member for partial transaction
 When Member performs enrollment for partial transaction
 Then Member should be enroll successfully with status code 200

Scenario: Post a transaction for one existing item
	Given Member call the earn transaction for one valid product item 
	When Member perfom earn transaction for one valid product item with assumeMissingProductItemsQualified as true
	Then Transaction with assumeMissingProductItemsQualified as true should be successfull with status code 200

Scenario: Post a transaction for one invalid product item
	Given Member call the earn transaction for one invalid product item with assumeMissingProductItemsQualified as true
	When Member perfom earn transaction for one invalid product item with assumeMissingProductItemsQualified as true
	Then Transaction with assumeMissingProductItemsQualified as true should not be successfull with status code 500

Scenario: Post a transaction for multiple valid product item
	Given Member call the earn transaction for multiple valid product item with assumeMissingProductItemsQualified as true
	When Member perfom earn transaction for multiple valid product item with assumeMissingProductItemsQualified as true
	Then Transaction with assumeMissingProductItemsQualified as true should be successfull for multiple products with status code 200

Scenario: Post a transaction for mixture of existing and non-existing product item
	Given Member call the earn transaction for mixture of existing and non-existing product item with assumeMissingProductItemsQualified as true
	When Member perfom earn transaction for mixture of existing and non-existing product item with assumeMissingProductItemsQualified as true
	Then Transaction with assumeMissingProductItemsQualified as true should be successfull for mixture product with status code 200		
	
Scenario: Post a transaction for one existing item for assumeMissingProductItemsQualified as false
	Given Member call the earn transaction for one valid product item 
	When Member perfom earn transaction for one valid product item with assumeMissingProductItemsQualified as false
	Then Transaction with assumeMissingProductItemsQualified as false for one existing product should be successfull with status code 200

Scenario: Post a transaction for one invalid product item for assumeMissingProductItemsQualified as false
	Given Member call the earn transaction for one invalid product item 
	When Member perfom earn transaction for one invalid product item with assumeMissingProductItemsQualified as false
	Then Transaction with assumeMissingProductItemsQualified as false for single invalid product should not be successfull with status code 500

Scenario: Post a transaction for multiple valid product item for assumeMissingProductItemsQualified as false
	Given Member call the earn transaction for multiple valid product item 
	When Member perfom earn transaction for multiple valid product item with assumeMissingProductItemsQualified as false
	Then Transaction with assumeMissingProductItemsQualified as false for multiple valid product should not be successfull with status code 200

Scenario: Post a transaction for mixture of existing and non-existing product item for assumeMissingProductItemsQualified as false
	Given Member call the earn transaction for mixture of existing and non-existing product item 
	When Member perfom earn transaction for mixture of existing and non-existing product item with assumeMissingProductItemsQualified as false
	Then Transaction with assumeMissingProductItemsQualified as false for mixture of existing and non-existing product should not be successfull with status code 200			

Scenario: Post a transaction for one existing item for IgnoreMissingProductItems as true
	Given Member call the earn transaction for one valid product item 
	When Member perfom earn transaction for one valid product item with IgnoreMissingProductItems as true
	Then Transaction with IgnoreMissingProductItems as true for one existing product should be successfull with status code 200

Scenario: Post a transaction for one invalid product item for IgnoreMissingProductItems as true
	Given Member call the earn transaction for one invalid product item 
	When Member perfom earn transaction for one invalid product item with IgnoreMissingProductItems as true
	Then Transaction with IgnoreMissingProductItems as true for single invalid product should not be successfull with status code 500

Scenario: Post a transaction for multiple valid product item for IgnoreMissingProductItems as true
	Given Member call the earn transaction for multiple valid product item 
	When Member perfom earn transaction for multiple valid product item with IgnoreMissingProductItems as true
	Then Transaction with IgnoreMissingProductItems as true for multiple valid product should not be successfull with status code 200

Scenario: Post a transaction for mixture of existing and non-existing product item for IgnoreMissingProductItems as true
	Given Member call the earn transaction for mixture of existing and non-existing product item 
	When Member perfom earn transaction for mixture of existing and non-existing product item with IgnoreMissingProductItems as true
	Then Transaction with IgnoreMissingProductItems as true for mixture of existing and non-existing product should not be successfull with status code 200
	
Scenario: Post a transaction for one existing item for IgnoreMissingProductItems as false
	Given Member call the earn transaction for one valid product item 
	When Member perfom earn transaction for one valid product item with IgnoreMissingProductItems as false
	Then Transaction with IgnoreMissingProductItems as false for one existing product should be successfull with status code 200

Scenario: Post a transaction for one invalid product item for IgnoreMissingProductItems as false
	Given Member call the earn transaction for one invalid product item 
	When Member perfom earn transaction for one invalid product item with IgnoreMissingProductItems as false
	Then Transaction with IgnoreMissingProductItems as false for single invalid product should not be successfull with status code 500

Scenario: Post a transaction for multiple valid product item for IgnoreMissingProductItems as false
	Given Member call the earn transaction for multiple valid product item 
	When Member perfom earn transaction for multiple valid product item with IgnoreMissingProductItems as false
	Then Transaction with IgnoreMissingProductItems as false for multiple valid product should not be successfull with status code 200

Scenario: Post a transaction for mixture of existing and non-existing product item for IgnoreMissingProductItems as false
	Given Member call the earn transaction for mixture of existing and non-existing product item 
	When Member perfom earn transaction for mixture of existing and non-existing product item with IgnoreMissingProductItems as false
	Then Transaction with IgnoreMissingProductItems as false for mixture of existing and non-existing product should not be successfull with status code 200
	
Scenario: Transaction for one existing item for TxEvaluator
	Given Member call the earn transaction for one valid product item 
	When Member perfom earn transaction for one valid product item for TxEvaluator
	Then Transaction should be successfull with status code 200

Scenario: Transaction for one invalid product item for TxEvaluator
	Given Member call the earn transaction for one invalid product item
	When Member perfom earn transaction for one invalid product item for TxEvaluator
	Then Transaction with TxEvaluator should not be successfull with status code 500

Scenario: Transaction for multiple valid product item for TxEvaluator
	Given Member call the earn transaction for multiple valid product item
	When Member perfom earn transaction for multiple valid product item for TxEvaluator
	Then Transaction with TxEvaluator should be successfull for multiple products with status code 200

Scenario: Transaction for mixture of existing and non-existing product item  for TxEvaluator
	Given Member call the earn transaction for mixture of existing and non-existing product item 
	When Member perfom earn transaction for mixture of existing and non-existing product item
	Then Transaction with TxEvaluator should be successfull for mixture product with status code 404
	
#new cases
Scenario: Transaction for Standard TxEvaluator without passing product key
	Given Member call the earn transaction 
	When Member perfom earn transaction without passing product key
	Then Transaction without passing product key should be successfull with status code 200		
		
		
	