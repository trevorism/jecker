Feature: Context Root of this API
  In order to use the Jecker API, it must be available

  Scenario: ContextRoot https
    Given the jecker application is alive
    When I navigate to https://jecker.testing.trevorism.com
    Then the API returns a link to the help page

  Scenario: Ping https
    Given the jecker application is alive
    When I navigate to /ping on https://jecker.testing.trevorism.com
    Then pong is returned, to indicate the service is alive

