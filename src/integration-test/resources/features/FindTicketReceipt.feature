@TicketAndFees
Feature: Search with ticket number should return ticket receipt
  As a user
  I want to search with ticket number
  So that I can display the receipt

  Scenario Outline: Happy path scenario for receipt header

    Given I want to find my ticket receipt header for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with origin airport "<originAirport>", destinationAirport "<destinationAirport>" and pnr "<pnr>"

    Examples:
      | scenario                                        | ticketNumber   | lastName | firstName | departureDate  | originAirport | destinationAirport | pnr    |
      | One way with over night connection              | 0012336961822  | SOUTHERN | FLAGSHIP  | 2019-05-20     | GRU           | LHR                | GZNJJP |
      | One way non-stop                                | 0012337074732  | BRAVO    | ALPHA     | 2019-06-26     | LGA           | DTW                | DNSBYR |


  Scenario Outline: One-way with no connection tickets

    Given I want to find my ticket receipt details for scenario "<scenario>"
    When I search with one-way with no connection ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with segDepartureDate "<segDepartureDate>", segAirportFrom "<segAirportFrom>", segAirportTo "<segAirportTo>", segTimeFrom "<segTimeFrom>", segTimeTo "<segTimeTo>", flightNum "<flightNum>", bookingClass "<bookingClass>", and fareBasis "<fareBasis>"

    Examples:
      | scenario                      | ticketNumber   | lastName       | firstName | departureDate | segDepartureDate | segAirportFrom | segAirportTo | segTimeFrom | segTimeTo | flightNum | bookingClass | fareBasis |
      | Ticket number: 0012337535465  | 0012337535465  | KRISHANMOORTHY | VENKAT    | 2020-03-19    | 2020-03-19       | SEA            | DFW          | 07:30:00    | 13:34:00  | 1667      | B            | NVAKZNB1  |
      | Ticket number: 0012337535413  | 0012337535413  | LINCOLN        | SARAH     | 2020-04-19    | 2020-04-19       | PHL            | MIA          | 08:15:00    | 11:15:00  | 1533      | B            | QVAJZNB3  |


  Scenario Outline: One-way with one connection tickets

    Given I want to find ticket receipt for scenario "<scenario>"
    When I search with one-way with one connection ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with segmentDepartureDate "<segDepartureDate>", firstsegAirportFrom "<firstsegAirportFrom>", firstsegAirportTo "<firstsegAirportTo>", firstsegTimeFrom "<firstsegTimeFrom>", firstsegTimeTo "<firstsegTimeTo>", firstflightNum "<firstflightNum>", secondsegAirportFrom "<secondsegAirportFrom>", secondsegAirportTo "<secondsegAirportTo>", secondsegTimeFrom "<secondsegTimeFrom>", secondsegTimeTo "<secondsegTimeTo>", secondflightNum "<secondflightNum>"

    Examples:
      | scenario                      | ticketNumber   | lastName    | firstName | departureDate | segDepartureDate | firstsegAirportFrom | firstsegAirportTo | firstsegTimeFrom | firstsegTimeTo | firstflightNum |secondsegAirportFrom | secondsegAirportTo | secondsegTimeFrom | secondsegTimeTo | secondflightNum |
      | Ticket number: 0012337535595  | 0012337535595  | LINCOLN     | ABRAHAM   | 2020-04-19    | 2020-04-19       | MIA                 | DFW               | 07:00:00         | 09:00:00       | 2575           | DFW                 | SEA                | 10:25:00          | 12:37:00        | 2755            |
      | Ticket number: 0012337499128  | 0012337499128  | SOUTHWESTERN| FLAGSHIP  | 2020-03-18    | 2020-03-18       | PHL                 | DFW               | 05:30:00         | 08:00:00       | 2340           | DFW                 | HNL                | 09:05:00          | 12:45:00        | 123            |
