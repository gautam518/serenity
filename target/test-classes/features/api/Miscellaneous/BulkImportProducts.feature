@current
Feature: Bulk import product csv file 

Scenario: bulk import product in csv file
	Given user check for bulk import of product csv file
	When user perform member enrollmnet for product csv file import
	And user again perform member enrollmnet for product csv file import
	And user perform the bulk import of product csv file
	Then member is enroll successfully for product csv with status code 200
	And new member is enroll successfully for product csv with status code 200
	And bulk import of product csv file is successful with status code 200