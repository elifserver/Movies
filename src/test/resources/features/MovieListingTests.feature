Feature: Movie listing tests

  Background: Create a payload with APIKEY
    Given "movie" Payload is created with APIKEY

  Scenario: Get latest movie
    When User calls "GetLatestMovieAPI" with "GET" request
    Then The API call response status should be 200
    And Movie should be the expected one

  Scenario: Get now playing
    When User calls "GetNowPlayingAPI" with "GET" request
    Then The API call response status should be 200
    And Movie count should be the expected

  Scenario: Get popular
    When User calls "GetNowPopularAPI" with "GET" request
    Then The API call response status should be 200
    And Popular List should have "Mortal Kombat"