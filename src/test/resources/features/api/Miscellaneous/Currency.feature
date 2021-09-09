@current
Feature: currency conversation
	
Scenario: check member currency conversion
	Given user check for currency conversation
	When user perform import currency
	And user perform the convert currency
	Then currency is imported is successfully with status code 200
	And currency conversion is successfully with status code 200
