@TicketAndFees
Feature: Search with ticket number should return ticket receipt
  As a user
  I want to search with ticket number
  So that I can display the receipt

  Scenario Outline: Happy path scenario for receipt header

    Given I want to find my ticket receipt header for scenario <scenario>
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with origin airport "<originAirport>", destinationAirport "<destinationAirport>" and pnr "<pnr>"

    Examples:
      | scenario                                        | ticketNumber   | lastName | firstName | departureDate  | originAirport | destinationAirport | pnr    |
      | One way with connection                         | 0012335038507  | TEST     | SIMON     | 2019-05-01     | MCO           | MIA                | MRYMPT |
      | One way with connection with partial firstname  | 0012335038507  | TEST     | SIMO      | 2019-05-01     | MCO           | MIA                | MRYMPT |
      | One way with over night connection              | 0012336961822  | SOUTHERN | FLAGSHIP  | 2019-05-20     | GRU           | LHR                | GZNJJP |
      | One way non-stop                                | 0012337074732  | BRAVO    | ALPHA     | 2019-06-26     | LGA           | DTW                | DNSBYR |


  Scenario Outline: One-way with no connection tickets

    Given I want to find my ticket receipt details for scenario <scenario>
    When I search with one-way with no connection ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with segDepartureDate "<segDepartureDate>", segAirportFrom "<segAirportFrom>", segAirportTo "<segAirportTo>", segTimeFrom "<segTimeFrom>", segTimeTo "<segTimeTo>", flightNum "<flightNum>", bookingClass "<bookingClass>", and fareBasis "<fareBasis>"

    Examples:
      | scenario                      | ticketNumber   | lastName       | firstName | departureDate | segDepartureDate | segAirportFrom | segAirportTo | segTimeFrom | segTimeTo | flightNum | bookingClass | fareBasis |
      | Ticket number: 0012337535465  | 0012337535465  | KRISHANMOORTHY | VENKAT    | 2020-03-19    | 2020-03-19       | SEA            | DFW          | 07:30:00    | 13:34:00  | 1667      | B            | NVAKZNB1  |
      | Ticket number: 0012337535413  | 0012337535413  | LINCOLN        | SARAH     | 2020-04-19    | 2020-04-19       | PHL            | MIA          | 08:15:00    | 11:15:00  | 1533      | B            | QVAJZNB3  |

