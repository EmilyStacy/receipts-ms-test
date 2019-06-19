Feature: Search wifi subscriptions by CC last four, lastName
	As a user
	I want to search my wifi subscriptions
	So that I can print them

Scenario Outline: Happy path scenario

  Given I want to find my wifi subscription
  When I search with CC last four digits "<ccLastFour>", last name "<lastName>", start date "<startDate>", end date "<endDate>"
  Then I find number of subscription records "<noOfRecords>"

  Examples:
  | ccLastFour | lastName  | startDate  | endDate       | noOfRecords  |
  | 1234       | SMITH     | 2016-12-01 | 2017-01-02    | 3            |
  | 1234       | SMITH     | 2018-01-20 | 2018-01-22    | 1            |
  | 1234       | SMITH     | 2016-12-31 | 2019-01-18    | 6            |
