@issue:VAP-6
Feature: Member information in Admin Portal

  Scenario Outline: User should be able to log in to PCv2 admin portal and Check member information
    Given User should be on the login page of PCv2 admin portal
    When User enters correct username '<username>' and password '<password>' and clicks on login button
    Then User should login successfully and navigate to the member info page.
    Examples:
      |username|password|
      |test_automation_1|Test1234#|