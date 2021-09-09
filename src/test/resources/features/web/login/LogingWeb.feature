@issue:VAP-6
Feature: Login To Admin Portal

  Scenario Outline: User should be able to log in to PCv2 Dashboard successfully using valid credentials
    Given User should be on the login page of PCv2 admin portal
    When User enters correct username '<username>' and password '<password>' and clicks on login button
    Then User should login successfully and navigate to the dashboard page.
    Examples:
       |username|password|
      |amansharma|Summer20?|
 
Scenario Outline: User should be able to Logout successfully from PC v2 Admin
   Given User should be on the login page of PCv2 admin portal
    When User enters correct username '<username>' and password '<password>' and clicks on login button
    Then User should login successfully and navigate to the dashboard page.
    Then User should select profile dropdown and Click on logout button
    Then User should logout of Pcv2 admin and see login page.
    
    Examples:
      |username|password|
      |amansharma|Summer20?|
      
   Scenario Outline: User should not be able to log in to PCv2 Admin Using invalid credentials
    Given User should be on the login page of PCv2 admin portal
    When User enters incorrect username '<username>' and password '<password>' and clicks on login button
    And Message should be displayed : login failed
    
    Examples:
      |username|password|
      |test_automation_2|test_automation_2|
      
      
      
      @manual
@manual-result:passed
@manual-last-tested:sprint-15
@manual-test-evidence:assets/screenshot112.jpg
    Scenario: User should see error message "This field is required" if the username field is empty and clicks on Login button
    Given User should be on the login page of PCv2 admin portal
    When User does not put Username and clicks on login button
    Then User should see error message "This field is required".
      
     
      
      
      
      
      
      
  