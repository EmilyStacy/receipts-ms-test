@HealthCheck
Feature: the health can be retrieved
  Scenario: client makes call to GET /actuator/health
    When the client calls /actuator/health
Then the client receives response status code of 200
