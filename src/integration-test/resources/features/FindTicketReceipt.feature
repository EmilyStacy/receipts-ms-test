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
      | scenario                      | ticketNumber   | lastName | firstName | departureDate | segDepartureDate | segAirportFrom | segAirportTo | segTimeFrom | segTimeTo | flightNum | bookingClass | fareBasis |
      | Ticket number: 0012371661425  | 0012371661425  | ONEWAY   | JOSEPH    | 2020-04-19    | 2020-04-19       | DFW            | MIA          | 07:15:00     | 11:13:00  | 2386      | K            | K0AIZRN1  |


  Scenario Outline: One-way with one connection tickets

    Given I want to find ticket receipt for scenario "<scenario>"
    When I search with one-way with one connection ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with segmentDepartureDate "<segDepartureDate>", firstsegAirportFrom "<firstsegAirportFrom>", firstsegAirportTo "<firstsegAirportTo>", firstsegTimeFrom "<firstsegTimeFrom>", firstsegTimeTo "<firstsegTimeTo>", firstflightNum "<firstflightNum>", secondsegAirportFrom "<secondsegAirportFrom>", secondsegAirportTo "<secondsegAirportTo>", secondsegTimeFrom "<secondsegTimeFrom>", secondsegTimeTo "<secondsegTimeTo>", secondflightNum "<secondflightNum>"

    Examples:
      | scenario                      | ticketNumber   | lastName         | firstName | departureDate | segDepartureDate | firstsegAirportFrom | firstsegAirportTo | firstsegTimeFrom | firstsegTimeTo | firstflightNum |secondsegAirportFrom | secondsegAirportTo | secondsegTimeFrom | secondsegTimeTo | secondflightNum |
      | Ticket number: 0012371661436  | 0012371661436  | ONEWAYCONNECTION | HILTON    | 2020-04-19    | 2020-04-19       | BHM                 | DFW               | 06:02:00         | 08:00:00       | 5823           | DFW                 | SAN                | 08:50:00          | 09:45:00        | 1064            |

  
  Scenario Outline: Round trip with no connection tickets

    Given I want to find round trip ticket receipt for scenario "<scenario>"
    When I search for round trip with no connection ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response for round trip ticket receipt with firstSegDepartureDate "<firstSegDepartureDate>", firstSegAirportFrom "<firstSegAirportFrom>", firstSegAirportTo "<firstSegAirportTo>", firstSegTimeFrom "<firstSegTimeFrom>", firstSegTimeTo "<firstSegTimeTo>", firstSegFlightNum "<firstSegFlightNum>", firstSegReturnTrip "<firstSegReturnTrip>", secondSegDepartureDate "<secondSegDepartureDate>", secondSegAirportFrom "<secondSegAirportFrom>", secondSegAirportTo "<secondSegAirportTo>", secondSegTimeFrom "<secondSegTimeFrom>", secondSegTimeTo "<secondSegTimeTo>", secondSegFlightNum "<secondSegFlightNum>", secondSegReturnTrip "<secondSegReturnTrip>"

    Examples:
      | scenario                      | ticketNumber   | lastName     | firstName | departureDate | firstSegDepartureDate | firstSegAirportFrom | firstSegAirportTo | firstSegTimeFrom | firstSegTimeTo | firstSegFlightNum | firstSegReturnTrip | secondSegDepartureDate | secondSegAirportFrom | secondSegAirportTo | secondSegTimeFrom | secondSegTimeTo | secondSegFlightNum | secondSegReturnTrip  |
      | Ticket number: 0012371503068  | 0012371503068  | SOUTHWESTERN | FLAGSHIP  | 2019-09-25    | 2019-09-25            | DFW                 | LAX               | 06:00:00         | 07:15:00       | 2112              | false              | 2019-10-02             | LAX                  | DFW                | 06:00:00          | 11:11:00        | 386                | true                 |


  Scenario Outline: Round trip with one connection tickets

    Given I want to find round trip with one connection ticket receipt for scenario "<scenario>"
    When I search for round trip with one connection ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response for round trip with one connection ticket receipt with departSeg1DepartureDate "<departSeg1DepartureDate>", departSeg1AirportFrom "<departSeg1AirportFrom>", departSeg1AirportTo "<departSeg1AirportTo>", departSeg1TimeFrom "<departSeg1TimeFrom>", departSeg1TimeTo "<departSeg1TimeTo>", departSeg1FlightNum "<departSeg1FlightNum>", departSeg1_isReturnTrip "<departSeg1_isReturnTrip>", departSeg2DepartureDate "<departSeg2DepartureDate>", departSeg2AirportFrom "<departSeg2AirportFrom>", departSeg2AirportTo "<departSeg2AirportTo>", departSeg2TimeFrom "<departSeg2TimeFrom>", departSeg2TimeTo "<departSeg2TimeTo>", departSeg2FlightNum "<departSeg2FlightNum>", departSeg2ReturnTrip "<departSeg2ReturnTrip>", returnSeg1DepartureDate "<returnSeg1DepartureDate>", returnSeg1AirportFrom "<returnSeg1AirportFrom>", returnSeg1AirportTo "<returnSeg1AirportTo>", returnSeg1TimeFrom "<returnSeg1TimeFrom>", returnSeg1TimeTo "<returnSeg1TimeTo>", returnSeg1FlightNum "<returnSeg1FlightNum>", returnSeg1ReturnTrip "<returnSeg1ReturnTrip>", returnSeg2DepartureDate "<returnSeg2DepartureDate>", returnSeg2AirportFrom "<returnSeg2AirportFrom>", returnSeg2AirportTo "<returnSeg2AirportTo>", returnSeg2TimeFrom "<returnSeg2TimeFrom>", returnSeg2TimeTo "<returnSeg2TimeTo>", returnSeg2FlightNum "<returnSeg2FlightNum>"


    Examples:
      | scenario                     | ticketNumber  | lastName     | firstName   | departureDate | departSeg1DepartureDate | departSeg1AirportFrom | departSeg1AirportTo | departSeg1TimeFrom | departSeg1TimeTo | departSeg1FlightNum | departSeg1_isReturnTrip | departSeg2DepartureDate | departSeg2AirportFrom | departSeg2AirportTo | departSeg2TimeFrom | departSeg2TimeTo | departSeg2FlightNum | departSeg2ReturnTrip | returnSeg1DepartureDate | returnSeg1AirportFrom | returnSeg1AirportTo | returnSeg1TimeFrom | returnSeg1TimeTo | returnSeg1FlightNum | returnSeg1ReturnTrip | returnSeg2DepartureDate | returnSeg2AirportFrom | returnSeg2AirportTo | returnSeg2TimeFrom | returnSeg2TimeTo | returnSeg2FlightNum |
      | Ticket number: 0012371549003 | 0012371549003 | SOUTHWESTERN | FLAGSHIP    | 2019-10-04    | 2019-10-04              | MSY                   | DFW                 | 06:00:00           | 07:41:00         | 9                   | false                   | 2019-10-04              | DFW                   | SAT                 | 08:43:00           | 09:52:00         | 1358                | false                | 2019-10-11              | SAT                   | DFW                 | 05:00:00           | 06:09:00         | 2266                | true                 | 2019-10-11              | DFW                   | MSY                 | 07:20:00           | 08:50:00         | 138                 |
