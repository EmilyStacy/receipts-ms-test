@TicketAndFees
Feature: Search with ticket number should return ticket summary
	As a user
	I want to search with ticket Number
	So that I can display the summary

    Scenario Outline: Happy path scenario
    
      Given I want to find my ticket summary for scenario <scenario>
      When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
      Then I get a successful response with origin airport "<originAirport>", destinationAirport "<destinationAirport>" and pnr "<pnr>"
    
      Examples:
      | scenario                                        | ticketNumber   | lastName | firstName | departureDate  | originAirport | destinationAirport | pnr    |
      | One way with connection                         | 0012335038507  | TEST     | SIMON     | 2019-05-01     | MCO           | MIA                | MRYMPT |
      | One way with connection with partial firstname  | 0012335038507  | TEST     | SIMO      | 2019-05-01     | MCO           | MIA                | MRYMPT |
      | One way with over night connection              | 0012336961822  | SOUTHERN | FLAGSHIP  | 2019-05-20     | GRU           | LHR                | GZNJJP |
      | One way non-stop                                | 0012337074732  | BRAVO    | ALPHA     | 2019-06-26     | LGA           | DTW                | DNSBYR |

