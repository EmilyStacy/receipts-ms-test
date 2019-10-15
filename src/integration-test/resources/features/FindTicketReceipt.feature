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


  Scenario Outline: Ticket receipt details for different types of itineraries

    Given I want to find trip details for scenario "<scenario>"
    When I search for my trip with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with pnr as "<PNR>", departSegmentString as "<departSegmentString>", returnSegmentString as "<returnSegmentString>"

    Examples:
      | scenario                                          | ticketNumber  | lastName         | firstName   | departureDate | PNR    | departSegmentString | returnSegmentString |
      | Round trip with 2 stops each way                  | 0012371651094 | SMITH            | JOHN        | 2019-10-26    | EAXKUC | AA4063 leaving TYR(Tyler, TX) to DFW(Dallas/Fort Worth, TX) on 2019-10-26 at 06:45:00 and arriving 07:48:00 in class OWBVZNB5. AA2695 leaving DFW(Dallas/Fort Worth, TX) to DCA(Washington, DC) on 2019-10-26 at 08:59:00 and arriving 12:53:00 in class OWBVZNB5. AA2119 leaving DCA(Washington, DC) to BOS(Boston, MA) on 2019-10-26 at 13:30:00 and arriving 15:10:00 in class OWBVZNB5. | AA2265 leaving BOS(Boston, MA) to ORD(Chicago, IL) on 2019-11-05 at 06:00:00 and arriving 08:01:00 in class QVAIXSB5. AA1217 leaving ORD(Chicago, IL) to DFW(Dallas/Fort Worth, TX) on 2019-11-05 at 09:00:00 and arriving 11:37:00 in class QVAIXSB5. AA3674 leaving DFW(Dallas/Fort Worth, TX) to TYR(Tyler, TX) on 2019-11-05 at 12:36:00 and arriving 12:50:00 in class QVAIXSB5.  |
      | Round trip with 2 stops open jaw                  | 0012371651104 | FRANKLIN         | MARK        | 2019-11-05    | WGUBWQ | AA2265 leaving BOS(Boston, MA) to ORD(Chicago, IL) on 2019-11-05 at 06:00:00 and arriving 08:01:00 in class QVAIXSB5. AA1217 leaving ORD(Chicago, IL) to DFW(Dallas/Fort Worth, TX) on 2019-11-05 at 09:00:00 and arriving 11:37:00 in class QVAIXSB5. AA3674 leaving DFW(Dallas/Fort Worth, TX) to TYR(Tyler, TX) on 2019-11-05 at 12:36:00 and arriving 12:50:00 in class QVAIXSB5. | AA3983 leaving GGG(Gladewater/Kilgore/Longview, TX) to DFW(Dallas/Fort Worth, TX) on 2019-11-15 at 06:35:00 and arriving 07:35:00 in class OWBVZNB5. AA2695 leaving DFW(Dallas/Fort Worth, TX) to DCA(Washington, DC) on 2019-11-15 at 09:00:00 and arriving 12:53:00 in class OWBVZNB5. AA2119 leaving DCA(Washington, DC) to BOS(Boston, MA) on 2019-11-15 at 13:30:00 and arriving 15:00:00 in class OWBVZNB5.  |
      | Round trip with 3 stops with different fair basis | 0012371651123 | SANDERSON        | ADAM        | 2019-12-15    | ZDRTNN | AA298 leaving HNL(Honolulu, HI) to LAX(Los Angeles, CA) on 2019-12-15 at 22:30:00 and arriving 05:55:00 in class OVAKWSBH. AA2437 leaving LAX(Los Angeles, CA) to DFW(Dallas/Fort Worth, TX) on 2019-12-16 at 07:00:00 and arriving 12:03:00 in class Q7AKZNB3. AA2839 leaving DFW(Dallas/Fort Worth, TX) to ORD(Chicago, IL) on 2019-12-16 at 13:27:00 and arriving 15:51:00 in class Q7AKZNB3. AA3858 leaving ORD(Chicago, IL) to CMI(Champaign, IL) on 2019-12-16 at 17:10:00 and arriving 17:41:00 in class NVBHZNB3. | AA3577 leaving CMI(Champaign, IL) to ORD(Chicago, IL) on 2020-02-12 at 06:33:00 and arriving 07:36:00 in class NVBHZNB3. AA2428 leaving ORD(Chicago, IL) to DFW(Dallas/Fort Worth, TX) on 2020-02-12 at 08:25:00 and arriving 10:48:00 in class Q7AKZNB3. AA2811 leaving DFW(Dallas/Fort Worth, TX) to LAX(Los Angeles, CA) on 2020-02-12 at 11:45:00 and arriving 13:05:00 in class Q7AKZNB3. AA143 leaving LAX(Los Angeles, CA) to HNL(Honolulu, HI) on 2020-02-12 at 14:00:00 and arriving 18:20:00 in class OVAKZSBH. |
      | Round trip with 1 connection                      | 0012371549003 | SOUTHWESTERN     | FLAGSHIP    | 2019-10-04    | BLOWIM | AA9 leaving MSY(New Orleans, LA) to DFW(Dallas/Fort Worth, TX) on 2019-10-04 at 06:00:00 and arriving 07:41:00 in class S7AHZNN3. AA1358 leaving DFW(Dallas/Fort Worth, TX) to SAT(San Antonio, TX) on 2019-10-04 at 08:43:00 and arriving 09:52:00 in class S7AHZNN3. | AA2266 leaving SAT(San Antonio, TX) to DFW(Dallas/Fort Worth, TX) on 2019-10-11 at 05:00:00 and arriving 06:09:00 in class NUAHZNO3. AA138 leaving DFW(Dallas/Fort Worth, TX) to MSY(New Orleans, LA) on 2019-10-11 at 07:20:00 and arriving 08:50:00 in class NUAHZNO3. |
      | Round trip with no connection                     | 0012371503068 | SOUTHWESTERN     | FLAGSHIP    | 2019-09-25    | RMIYHK | AA2112 leaving DFW(Dallas/Fort Worth, TX) to LAX(Los Angeles, CA) on 2019-09-25 at 06:00:00 and arriving 07:15:00 in class T12M5UU. | AA386 leaving LAX(Los Angeles, CA) to DFW(Dallas/Fort Worth, TX) on 2019-10-02 at 06:00:00 and arriving 11:11:00 in class T12M5UU. |
      | One way with one connection                       | 0012371661436 | ONEWAYCONNECTION | HILTON      | 2020-04-19    | ARMHEN | AA5823 leaving BHM(Birmingham, AL) to DFW(Dallas/Fort Worth, TX) on 2020-04-19 at 06:02:00 and arriving 08:00:00 in class K7AZZNN1. AA1064 leaving DFW(Dallas/Fort Worth, TX) to SAN(San Diego, CA) on 2020-04-19 at 08:50:00 and arriving 09:45:00 in class K7AZZNN1. |  |
      | One way with no connection                        | 0012371661425 | ONEWAY           | JOSEPH      | 2020-04-19    | EKAYXI | AA2386 leaving DFW(Dallas/Fort Worth, TX) to MIA(Miami, FL) on 2020-04-19 at 07:15:00 and arriving 11:13:00 in class K0AIZRN1.  |   |
