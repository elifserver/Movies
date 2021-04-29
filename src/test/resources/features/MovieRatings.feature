Feature: Rate the movies

  Background: Prepare the payload
    Given SessionID is added the payload

  Scenario: Rate a movie
    Given Movie with "ID"  "460465" is selected
    When User rates movie "9.50"
    Then The API call response status should be 201

  Scenario Outline: Rate a movie with invalid value
    Given Movie with "ID"  "460465" is selected
    When User rates movie "<rate>"
    Then User should get "<errorMessage>"
    Examples:
      | rate | errorMessage                                               |
      | 0.99 | Value invalid: Values must be a multiple of 0.50.          |
      | 11   | Value too high: Value must be less than, or equal to 10.0. |
      | -1   | Value too low: Value must be greater than 0.0.             |

  Scenario: Delete raring
    Given Movie with "ID"  "460465" is selected
    And User deletes movie rating
    Then The API call response status should be 200
