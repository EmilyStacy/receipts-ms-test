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

  Scenario Outline: Round trip or one way with more than one connection

    Given I want to find round trip or one way with more than one connection ticket receipt for scenario "<scenario>"
    When I search for round trip or one way with multiple connections ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with pnr as "<PNR>", departSegmentString as "<departSegmentString>", returnSegmentString as "<returnSegmentString>"

    Examples:
      | scenario                                          | ticketNumber  | lastName     | firstName   | departureDate | PNR    | departSegmentString | returnSegmentString |
      | Round trip with 2 stops each way                  | 0012371651094 | SMITH        | JOHN        | 2019-10-26    | EAXKUC | AA4063 leaving TYR to DFW on 2019-10-26 at 06:45:00 and arriving 07:48:00 in class OWBVZNB5. AA2695 leaving DFW to DCA on 2019-10-26 at 08:59:00 and arriving 12:53:00 in class OWBVZNB5. AA2119 leaving DCA to BOS on 2019-10-26 at 13:30:00 and arriving 15:10:00 in class OWBVZNB5. | AA2265 leaving BOS to ORD on 2019-11-05 at 06:00:00 and arriving 08:01:00 in class QVAIXSB5. AA1217 leaving ORD to DFW on 2019-11-05 at 09:00:00 and arriving 11:37:00 in class QVAIXSB5. AA3674 leaving DFW to TYR on 2019-11-05 at 12:36:00 and arriving 12:50:00 in class QVAIXSB5.  |
      | Round trip with 2 stops open jaw                  | 0012371651104 | FRANKLIN     | MARK        | 2019-11-05    | WGUBWQ | AA2265 leaving BOS to ORD on 2019-11-05 at 06:00:00 and arriving 08:01:00 in class QVAIXSB5. AA1217 leaving ORD to DFW on 2019-11-05 at 09:00:00 and arriving 11:37:00 in class QVAIXSB5. AA3674 leaving DFW to TYR on 2019-11-05 at 12:36:00 and arriving 12:50:00 in class QVAIXSB5. | AA3983 leaving GGG to DFW on 2019-11-15 at 06:35:00 and arriving 07:35:00 in class OWBVZNB5. AA2695 leaving DFW to DCA on 2019-11-15 at 09:00:00 and arriving 12:53:00 in class OWBVZNB5. AA2119 leaving DCA to BOS on 2019-11-15 at 13:30:00 and arriving 15:00:00 in class OWBVZNB5.  |
      | Round trip with 3 stops with different fair basis | 0012371651123 | SANDERSON    | ADAM        | 2019-12-15    | ZDRTNN | AA298 leaving HNL to LAX on 2019-12-15 at 22:30:00 and arriving 05:55:00 in class OVAKWSBH. AA2437 leaving LAX to DFW on 2019-12-16 at 07:00:00 and arriving 12:03:00 in class Q7AKZNB3. AA2839 leaving DFW to ORD on 2019-12-16 at 13:27:00 and arriving 15:51:00 in class Q7AKZNB3. AA3858 leaving ORD to CMI on 2019-12-16 at 17:10:00 and arriving 17:41:00 in class NVBHZNB3. | AA3577 leaving CMI to ORD on 2020-02-12 at 06:33:00 and arriving 07:36:00 in class NVBHZNB3. AA2428 leaving ORD to DFW on 2020-02-12 at 08:25:00 and arriving 10:48:00 in class Q7AKZNB3. AA2811 leaving DFW to LAX on 2020-02-12 at 11:45:00 and arriving 13:05:00 in class Q7AKZNB3. AA143 leaving LAX to HNL on 2020-02-12 at 14:00:00 and arriving 18:20:00 in class OVAKZSBH. |
