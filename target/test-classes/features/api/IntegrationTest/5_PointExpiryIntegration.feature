Feature: Verify Point Expiry Integration

Scenario Outline: redemable balance in Points expiry
	Given member point expiry for a member
	When user perform enrollment of a of member on date<date> for point expiry
	And user perform earn transaction of member on date<txdate> for point expiry 
	And user perform manual point adjustment of points<point>for point expiry
	And user perform account statement for member balance
	And user run the point expiry job
	And again user perform earn transaction of member on date<txdate> for point expiry
	And user run the point unlock job 
	Then member is created successful with status code 200
	And the member earn transaction for point expiry is successful with status code 200
	And the member point adjustment for point expiry  is successful with status code 200
	And member blance is displayed successfully with status code 200 
	And the point expiry job run sucessfully with status code 200
	And member second earn transaction for point expiry is successful with status code 200
	And the point unlock job run sucessfully with status code 200
	Examples: 
		| date|txdate|point|
		| "2021-03-20T00:00:00.000Z"|"2021-05-20T00:00:00.000Z"|"-112"|	