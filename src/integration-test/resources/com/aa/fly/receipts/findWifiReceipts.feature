@Wi-fi
Feature: Search wifi subscriptions by CC last four, lastName and purchase date range
	As a user
	I want to search my wifi subscriptions
	So that I can display them

    Scenario Outline: Happy path scenario
    
      Given I want to find my wifi subscription
      When I search with CC last four digits "<ccLastFour>", last name "<lastName>", start date "<startDate>", end date "<endDate>"
      Then I get a successful response
      Then I find number of subscription records "<noOfRecords>"
    
      Examples:
      | ccLastFour | lastName  | startDate  | endDate       | noOfRecords  |
      | 1234       | SMITH     | 2019-04-01 | 2019-05-01    | 1            |
      | 1234       | SMITH     | 2019-04-01 | 2019-06-01    | 2            |

  Scenario Outline: Search multiple wifi receipts

    Given I want to find multiple wifi receipts
    When I search with last name "<lastName>", lastFourOfCreditCard "<lastFourOfCreditCard>", smaller start date "<startDate>", end date "<endDate>"
    Then I get a response
    Then I get a successful multiple wifi receipts response
    Then I found two records

    Examples:
      | lastName | lastFourOfCreditCard | startDate                | endDate                  |
      | Smith    | 1234                 | 2019-02-01T17:21:46.932Z | 2019-06-01T17:21:46.932Z | 
 
 
  Scenario Outline: Search for wifi receipts returns no results

    Given I want to find wifi receipts for criteria
    When I search with invalid last name "<lastName>", lastFourOfCreditCard "<lastFourOfCreditCard>", smaller start date "<startDate>", end date "<endDate>"
    Then I submit my request
    Then I get a successful response with zero records

    Examples:
      | lastName | lastFourOfCreditCard | startDate                | endDate                  |
      | Asdf     | 8457                 | 2019-02-01T17:21:46.932Z | 2019-06-01T17:21:46.932Z | 
  