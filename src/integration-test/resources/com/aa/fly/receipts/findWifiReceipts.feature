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
