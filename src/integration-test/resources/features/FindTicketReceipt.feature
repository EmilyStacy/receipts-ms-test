@TicketAndFees
Feature: Search with ticket number should return ticket receipt
  As a user
  I want to search with ticket number
  So that I can display the receipt

  Scenario Outline: Happy path scenario for receipt header

    Given I want to find my ticket receipt header for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with origin airport "<originAirport>", destinationAirport "<destinationAirport>" and pnr "<pnr>" and advantageNumber "<advantageNumber>" and loyaltyOwnerCode "<loyaltyOwnerCode>"

    Examples:
      | scenario                                        | ticketNumber   | lastName | firstName | departureDate  | originAirport | destinationAirport | pnr    | advantageNumber | loyaltyOwnerCode |
      | One way with over night connection              | 0012372570843  | SOUTHERN | FLAGSHIP  | 2019-12-09     | GRU           | LHR                | FIATTY | 270RFY8         | AA               |
      | Other airline frequent flier                    | 0012372570877  | KISSANE  | LOUISE    | 2019-12-09     | DFW           | OKC                | CUVNQZ | 10017965        | BA               |


  Scenario Outline: Ticket receipt details for different types of itineraries

    Given I want to find trip details for scenario "<scenario>"
    When I search for my trip with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with pnr as "<PNR>", departSegmentString as "<departSegmentString>", returnSegmentString as "<returnSegmentString>"

    Examples:
      | scenario                                          | ticketNumber  | lastName         | firstName   | departureDate | PNR    | departSegmentString | returnSegmentString |
      | Round trip with 2 stops each way                  | 0012372570893 | SMITH            | JOHN        | 2019-12-10    | FIDECV | MQ4063 leaving TYR(Tyler, TX) to DFW(Dallas/Fort Worth, TX) on 2019-12-10 at 06:45:00 and arriving on 2019-12-10 at 07:47:00 in class L0BWZNB1. AA2695 leaving DFW(Dallas/Fort Worth, TX) to DCA(Washington, DC) on 2019-12-10 at 09:00:00 and arriving on 2019-12-10 at 12:53:00 in class G0AJZNB3. AA2149 leaving DCA(Washington, DC) to BOS(Boston, MA) on 2019-12-10 at 14:30:00 and arriving on 2019-12-10 at 16:02:00 in class G0AJZNB3. | AA2159 leaving BOS(Boston, MA) to DCA(Washington, DC) on 2019-12-13 at 06:00:00 and arriving on 2019-12-13 at 07:47:00 in class G0AJZNB3. AA2101 leaving DCA(Washington, DC) to DFW(Dallas/Fort Worth, TX) on 2019-12-13 at 09:00:00 and arriving on 2019-12-13 at 11:45:00 in class G0AJZNB3. MQ3674 leaving DFW(Dallas/Fort Worth, TX) to TYR(Tyler, TX) on 2019-12-13 at 12:30:00 and arriving on 2019-12-13 at 13:20:00 in class L0BWZNB1.  |
      | Round trip with 2 stops open jaw                  | 0012372570913 | FRANKLIN         | MARK        | 2019-12-10    | WXVGFD | AA2265 leaving BOS(Boston, MA) to ORD(Chicago, IL) on 2019-12-10 at 06:00:00 and arriving on 2019-12-10 at 08:00:00 in class M0AVZNB1. AA1217 leaving ORD(Chicago, IL) to DFW(Dallas/Fort Worth, TX) on 2019-12-10 at 09:00:00 and arriving on 2019-12-10 at 11:37:00 in class M0AVZNB1. MQ3674 leaving DFW(Dallas/Fort Worth, TX) to TYR(Tyler, TX) on 2019-12-10 at 12:30:00 and arriving on 2019-12-10 at 13:20:00 in class M0AVZNB1. | MQ3983 leaving GGG(Gladewater/Kilgore/Longview, TX) to DFW(Dallas/Fort Worth, TX) on 2019-12-11 at 06:35:00 and arriving on 2019-12-11 at 07:35:00 in class M0AVZNB1. AA1734 leaving DFW(Dallas/Fort Worth, TX) to ORD(Chicago, IL) on 2019-12-11 at 08:35:00 and arriving on 2019-12-11 at 10:57:00 in class M0AVZNB1. AA2321 leaving ORD(Chicago, IL) to BOS(Boston, MA) on 2019-12-11 at 15:06:00 and arriving on 2019-12-11 at 18:21:00 in class M0AVZNB1.  |
      | Round trip with 3 stops with different fair basis | 0012372630037 | SANDERSON        | ADAM        | 2019-12-18    | EYJVSP | AA298 leaving HNL(Honolulu, HI) to LAX(Los Angeles, CA) on 2019-12-18 at 22:32:00 and arriving on 2019-12-19 at 06:09:00 in class Q0AKVNMH. AA2374 leaving LAX(Los Angeles, CA) to DFW(Dallas/Fort Worth, TX) on 2019-12-19 at 07:40:00 and arriving on 2019-12-19 at 12:44:00 in class M0AKZNN1. AA2223 leaving DFW(Dallas/Fort Worth, TX) to ORD(Chicago, IL) on 2019-12-19 at 14:45:00 and arriving on 2019-12-19 at 17:08:00 in class M0AKZNN1. MQ3692 leaving ORD(Chicago, IL) to CMI(Champaign, IL) on 2019-12-19 at 18:50:00 and arriving on 2019-12-19 at 19:58:00 in class L0AHZNN1. | MQ3577 leaving CMI(Champaign, IL) to ORD(Chicago, IL) on 2020-02-02 at 06:10:00 and arriving on 2020-02-02 at 07:36:00 in class SVFHZRN1. AA947 leaving ORD(Chicago, IL) to DFW(Dallas/Fort Worth, TX) on 2020-02-02 at 09:00:00 and arriving on 2020-02-02 at 11:40:00 in class Q8AZZNM3. AA1331 leaving DFW(Dallas/Fort Worth, TX) to LAX(Los Angeles, CA) on 2020-02-02 at 12:48:00 and arriving on 2020-02-02 at 14:20:00 in class Q8AZZNM3. AA2205 leaving LAX(Los Angeles, CA) to HNL(Honolulu, HI) on 2020-02-02 at 19:56:00 and arriving on 2020-02-02 at 23:55:00 in class NVAKWNMH. |
      | Round trip with 1 connection                      | 0012372570921 | SOUTHWESTERN     | FLAGSHIP    | 2019-12-09    | CUYFOV | AA2536 leaving MSY(New Orleans, LA) to DFW(Dallas/Fort Worth, TX) on 2019-12-09 at 17:52:00 and arriving on 2019-12-09 at 19:39:00 in class L0AHZRN1. AA1406 leaving DFW(Dallas/Fort Worth, TX) to SAT(San Antonio, TX) on 2019-12-09 at 20:44:00 and arriving on 2019-12-09 at 21:57:00 in class L0AHZRN1. | AA334 leaving SAT(San Antonio, TX) to DFW(Dallas/Fort Worth, TX) on 2019-12-10 at 18:28:00 and arriving on 2019-12-10 at 19:44:00 in class L0AHZRN1. AA1455 leaving DFW(Dallas/Fort Worth, TX) to MSY(New Orleans, LA) on 2019-12-10 at 21:45:00 and arriving on 2019-12-10 at 23:10:00 in class L0AHZRN1. |
      | Round trip with no connection                     | 0012372570922 | SOUTHWESTERN     | FLAGSHIP    | 2019-12-09    | CUYNWQ | AA2453 leaving DFW(Dallas/Fort Worth, TX) to LAX(Los Angeles, CA) on 2019-12-09 at 19:20:00 and arriving on 2019-12-09 at 20:41:00 in class L0AZZNN1. | AA386 leaving LAX(Los Angeles, CA) to DFW(Dallas/Fort Worth, TX) on 2019-12-10 at 06:00:00 and arriving on 2019-12-10 at 11:02:00 in class L0AZZNN1. |
      | One way with one connection                       | 0012372630039 | ONEWAYCONNECTION | HILTON      | 2019-12-18    | JQUNGA | YV5828 leaving BHM(Birmingham, AL) to DFW(Dallas/Fort Worth, TX) on 2019-12-18 at 14:36:00 and arriving on 2019-12-18 at 16:45:00 in class K0AIZRN1. AA1243 leaving DFW(Dallas/Fort Worth, TX) to SAN(San Diego, CA) on 2019-12-18 at 18:45:00 and arriving on 2019-12-18 at 20:02:00 in class K0AIZRN1. |  |
      | One way with no connection                        | 0012372570926 | ONEWAY           | JOSEPH      | 2019-12-09    | FIFVOQ | AA2720 leaving DFW(Dallas/Fort Worth, TX) to MIA(Miami, FL) on 2019-12-09 at 21:30:00 and arriving on 2019-12-10 at 01:09:00 in class K0AIZRN1.  |   |
      | International round trip with 1 connection        | 0012372630044 | SOUTHWESTERN     | FLAGSHIP    | 2019-12-18    | YCNMBS | AA1271 leaving IAH(Houston, TX) to DFW(Dallas/Fort Worth, TX) on 2019-12-18 at 20:25:00 and arriving on 2019-12-18 at 21:44:00 in class KHX0NSN1. AA909 leaving DFW(Dallas/Fort Worth, TX) to LIM(Lima, Peru) on 2019-12-18 at 22:35:00 and arriving on 2019-12-19 at 06:39:00 in class KHX0NSN1. | AA1558 leaving LIM(Lima, Peru) to MIA(Miami, FL) on 2020-02-02 at 08:05:00 and arriving on 2020-02-02 at 13:53:00 in class LLN0NSN1. AA2690 leaving MIA(Miami, FL) to IAH(Houston, TX) on 2020-02-02 at 20:29:00 and arriving on 2020-02-02 at 22:30:00 in class LLN0NSN1. |


  Scenario Outline: Verify Form of Payment and ticket fare details

    Given I want to retrieve payment details for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with fopIssueDate "<fopIssueDate>", fopTypeCode "<fopTypeCode>",  fopTypeDescription "<fopTypeDescription>",  fopAccountNumberLastFour "<fopAccountNumberLastFour>", fopAmount "<fopAmount>", and fopCurrencyCode "<fopCurrencyCode>"
    Then I get a successful response with baseFareAmount "<baseFareAmount>", baseFareCurrencyCode "<baseFareCurrencyCode>", totalFareAmount "<totalFareAmount>", taxFareAmount "<taxFareAmount>"

    Examples:
      | scenario                                        | ticketNumber   | lastName | firstName | departureDate  | fopIssueDate  | fopTypeCode  | fopTypeDescription             | fopAccountNumberLastFour | fopAmount | fopCurrencyCode | baseFareAmount | baseFareCurrencyCode | totalFareAmount | taxFareAmount |
      | CreditCard Visa                                 | 0012372186607  | TUCSON   | FLAGSHIP  | 2020-01-15     | 2019-10-30    | CCBA         | Visa ending in 0006            | 0006                     | 2252.95   | USD             | 1929.00        | USD                  | 2252.95         | 323.95        |
      | Fare paid in USD & originated from US           | 0012372303346  | martin   | adam      | 2019-11-08     | 2019-11-07    | CCBA         | Visa ending in 0006            | 0006                     | 849.30    | USD             | 776.74         | USD                  | 849.30          | 72.56         |
      | CreditCard Exchange                             | 0012372633993  | BROWN    | TAYLOR    | 2020-04-19     | 2019-12-18    | EF           | Exchange                       | 0006                     | 0.00      | USD             | 919.06         | USD                  | 1016.59         | 97.53         |
      | CreditCard Exchange Add Collect                 | 0012372627224  | KING     | DON       | 2020-04-19     | 2019-12-17    | CCBA         | Exchange - Visa ending in 0006 | 0006                     | 1056.01   | USD             | 989.77         | USD                  | 1092.60         | 102.83        |

  Scenario Outline: Verify Taxes

    Given I want to retrieve payment details for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with baseFareAmount "<baseFareAmount>", baseFareCurrencyCode "<baseFareCurrencyCode>", totalFareAmount "<totalFareAmount>", and taxesString "<taxesString>"

    Examples:
      | scenario                                        | ticketNumber   | lastName | firstName | departureDate  |  baseFareAmount | baseFareCurrencyCode | totalFareAmount | taxesString |
      | Taxes - base fare currency CAD, XF USD          | 0012372187652  | CANADA   | MONTREAL  | 2020-04-29     | 385.99          | CAD                  | 536.28          | 1,XG8, CANADIAN GST,19.90,CAD; 2,XG9,CANADIAN GST,1.50,CAD; 3,SQ,TORONTO AIR IMPROV FE,30.00,CAD;4,XQ4,QUEBEC SALES TAX,2.99,CAD;5,CA4,CANADIAN SECURITY FEE,12.10,CAD; 6,US2,INT'L DEPT/ARRIVAL TA,48.60,CAD; 7,AY,SECURITY SERVICE FEE,7.32,CAD; 8,YC,U.S. CUSTOMS,7.69,CAD; 9,XY2,U.S. IMMIGRATION,9.14,CAD; 10,XA,APHIS,5.17,CAD;11,XF,SYS GEN PFC,5.88,CAD;|

      
  Scenario Outline: Zero ancillaries with FOP amt = ticket total amt, FOP amt = passenger amt

    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response without ancillaries rowCount "<rowCount>", fopAmt "<fopAmt>", totalFareAmount "<totalFareAmount>", passengerTotalAmount "<passengerTotalAmount>"
    
    Examples:
      | scenario                                                                                               | ticketNumber   | lastName | firstName | departureDate  | rowCount | fopAmt  | totalFareAmount | passengerTotalAmount | 
      | Zero ancillaries with FOP amt = ticket total amt, FOP amt = passenger amt                              | 0012372187652  | CANADA   | MONTREAL  | 2020-04-29     | 1        | 536.28  | 536.28          | 536.28               |

      
  Scenario Outline: One ancillary bought same date as ticket (2 FOPs), pax amt = ticket FOP amt + Ancillary FOP amt

    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with one ancillary rowCount "<rowCount>", fopAmt "<fopAmt>", passengerTotalAmount "<passengerTotalAmount>", anclryFOPAmt1 "<anclryFOPAmt1>", anclryFOPAmt2 "<anclryFOPAmt2>", anclryFOPIssueDate "<anclryFOPIssueDate>", anclryFOPTypeCode "<anclryFOPTypeCode>", anclryFOPAccountNumberLast4 "<anclryFOPAccountNumberLast4>", anclryDocNbr "<anclryDocNbr>", anclryIssueDate "<anclryIssueDate>", anclryPriceCurrencyAmount "<anclryPriceCurrencyAmount>", anclrySalesCurrencyAmount "<anclrySalesCurrencyAmount>", anclryTaxCurrencyAmount "<anclryTaxCurrencyAmount>"

    Examples:
      | scenario                                                                                               | ticketNumber   | lastName | firstName | departureDate  | rowCount | fopAmt  | passengerTotalAmount | anclryFOPAmt1 |anclryFOPAmt2 | anclryFOPIssueDate| anclryFOPTypeCode| anclryFOPAccountNumberLast4| anclryDocNbr | anclryIssueDate | anclryPriceCurrencyAmount | anclrySalesCurrencyAmount | anclryTaxCurrencyAmount |
      | One ancillary bought same date as ticket (2 FOPs), pax amt = ticket FOP amt + Ancillary FOP amt        | 0012372186607  | tucson   | flagship  | 2020-01-15     | 2        | 2252.95 | 2527.08              | anclryFOPAmt1|anclryFOPAmt2 | anclryFOPIssueDate| anclryFOPTypeCode| anclryFOPAccountNumberLast4| 654190614    | 2019-10-31      | 255.00                    | 274.13                    | 19.13                   |
      
      
  Scenario Outline: Two ancillaries bought same date as ticket (3 FOPs), pax amt = ticket FOP amt + Ancillary FOP amts

    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with two ancillaries rowCount "<rowCount>", fopAmt "<fopAmt>", passengerTotalAmount "<passengerTotalAmount>", anclryFOPAmt1 "<anclryFOPAmt1>", anclryFOPAmt2 "<anclryFOPAmt2>", anclryFOPIssueDate "<anclryFOPIssueDate>", anclryFOPTypeCode "<anclryFOPTypeCode>", anclryFOPAccountNumberLast4 "<anclryFOPAccountNumberLast4>", anclryDocNbr "<anclryDocNbr>", anclryIssueDate "<anclryIssueDate>", anclryPriceCurrencyAmount "<anclryPriceCurrencyAmount>", anclrySalesCurrencyAmount "<anclrySalesCurrencyAmount>", anclryTaxCurrencyAmount "<anclryTaxCurrencyAmount>"    

    Examples:
      | scenario                                                                                               | ticketNumber   | lastName | firstName | departureDate  | rowCount | fopAmt  | passengerTotalAmount | anclryFOPAmt1 |anclryFOPAmt2 | anclryFOPIssueDate| anclryFOPTypeCode| anclryFOPAccountNumberLast4| anclryDocNbr | anclryIssueDate | anclryPriceCurrencyAmount | anclrySalesCurrencyAmount | anclryTaxCurrencyAmount |
      | Two ancillaries bought with ticket (3 FOPs), pax amt = ticket FOP amt + Ancillary FOP amts             | 0012372303346  | martin   | adam      | 2019-11-08     | 3        | 849.30  |  971.78              | 78.38         | 44.10        | 2019-11-07        | CCBA             | 0006                       | 654200213    | 2019-11-07      | 72.91                     | 78.38                     | 5.47                    |
      