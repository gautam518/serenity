Feature: Search member in admin portal 

  Scenario Outline: User should be able to log in to PCv2 admin portal and search member
    Given User should be on the login page of PCv2 admin portal
    When User enters correct username '<username>' and password '<password>' and clicks on login button
    Then User should login successfully and should able to search member
    Examples:
      |username|password|
      |amansharma|Summer20?|