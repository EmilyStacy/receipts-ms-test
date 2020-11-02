@TicketAndFees
Feature: Search with ticket number should return ticket receipt
  As a user
  I want to search with ticket number
  So that I can display the receipt

  @Header
  Scenario Outline: Happy path scenario for receipt header
    Given I want to find my ticket receipt header for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with origin airport "<originAirport>", destinationAirport "<destinationAirport>" and pnr "<pnr>" and advantageNumber "<advantageNumber>" and loyaltyOwnerCode "<loyaltyOwnerCode>"

    Examples:
      | scenario                           | ticketNumber  | lastName | firstName | departureDate | originAirport | destinationAirport | pnr    | advantageNumber | loyaltyOwnerCode |
      | Other airline frequent flier       | 0012384122707 | TRAISMAN | LEO       | 12/20/2019    | MCI           | ORD                | VSUFLN | 61140446        | AS               |
      | One way with over night connection | 0012119066455 | RICHINS  | SHANNON   | 03/12/2020    | SGU           | VIE                | YYMFUN |                 | BA               |

  @OlderThan18Month
  Scenario: Search ticket with issued date older than 18 months from today
    Given I want to confirm Mosaic only supports tickets with issued date not older than 18 months from today
    When I search ticket number with below criteria
      | ticketNumber  | 0012329621998 |
      | firstName     | THOMAS        |
      | lastName      | HEADSTREAM    |
      | departureDate | 03/08/2019    |
    Then I get a 204 http status code response indicating no content

  Scenario Outline: Ticket receipt details for different types of itineraries
    Given I want to find trip details for scenario "<scenario>"
    When I search for my trip with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with pnr as "<PNR>", departSegmentString as "<departSegmentString>", returnSegmentString as "<returnSegmentString>"

    Examples:
      | scenario                                          | ticketNumber  | lastName    | firstName | departureDate | PNR    | departSegmentString                                                                                                                                                                                                                                                                                                                                                  | returnSegmentString                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
      | Round trip with 2 stops each way                  | 0012329621998 | HEADSTREAM  | THOMAS    | 03/08/2019    | TXKYHH | AA3898 leaving ABI(Abilene, TX) to DFW(Dallas/Fort Worth, TX) on 2019-03-07 at 17:01:00 and arriving on 2019-03-07 at 17:57:00 in class Y27A5RC and the flight status is USED.                                                                                                                                                                                       | AA2386 leaving DFW(Dallas/Fort Worth, TX) to MIA(Miami, FL) on 2019-03-08 at 07:18:00 and arriving on 2019-03-08 at 11:05:00 in class Y27A5RC and the flight status is USED. AA2219 leaving MIA(Miami, FL) to SXM(St Maarten, Sint Maarten) on 2019-03-08 at 12:25:00 and arriving on 2019-03-08 at 16:17:00 in class Y27A5RC and the flight status is USED. AA2219 leaving SXM(St Maarten, Sint Maarten) to MIA(Miami, FL) on 2019-03-17 at 16:17:00 and arriving on 2019-03-17 at 19:49:00 in class Y65ARE and the flight status is USED. AA1219 leaving MIA(Miami, FL) to DFW(Dallas/Fort Worth, TX) on 2019-03-17 at 21:35:00 and arriving on 2019-03-17 at 23:54:00 in class Y65ARE and the flight status is USED. AA3463 leaving DFW(Dallas/Fort Worth, TX) to ABI(Abilene, TX) on 2019-03-18 at 09:10:00 and arriving on 2019-03-18 at 10:11:00 in class Y65ARE and the flight status is USED. |
      | Round trip with no connection open jaw            | 0012341496264 | WEBER       | PATRICK   | 06/04/2019    | RLFZUS | AA2368 leaving DFW(Dallas/Fort Worth, TX) to BWI(Baltimore/Washington, MD) on 2019-06-04 at 10:35:00 and arriving on 2019-06-04 at 14:38:00 in class GVAIUSM1 and the flight status is USED.                                                                                                                                                                         | AA2562 leaving PHL(Philadelphia, PA) to DFW(Dallas/Fort Worth, TX) on 2019-06-08 at 11:25:00 and arriving on 2019-06-08 at 13:52:00 in class KWBUPNIF and the flight status is USED.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
      | Round trip with 3 stops with different fare basis | 0012398358969 | CARINGI     | ANTHONY   | 02/10/2020    | MIADNA | AA392 leaving PHL(Philadelphia, PA) to DCA(Washington, DC) on 2020-02-10 at 06:32:00 and arriving on 2020-02-10 at 07:38:00 in class GVFHZNM3 and the flight status is USED. YX4636 leaving DCA(Washington, DC) to MSP(Minneapolis/St Paul, MN) on 2020-02-10 at 10:06:00 and arriving on 2020-02-10 at 12:06:00 in class GVFHZNM3 and the flight status is USED.    | MQ4163 leaving MSP(Minneapolis/St Paul, MN) to ORD(Chicago, IL) on 2020-02-11 at 15:49:00 and arriving on 2020-02-11 at 17:31:00 in class GVAIZNM5 and the flight status is USED. AA2715 leaving ORD(Chicago, IL) to LAX(Los Angeles, CA) on 2020-02-11 at 20:29:00 and arriving on 2020-02-11 at 23:07:00 in class GVAIZNM5 and the flight status is USED. AA658 leaving LAX(Los Angeles, CA) to PHL(Philadelphia, PA) on 2020-02-17 at 11:57:00 and arriving on 2020-02-17 at 19:59:00 in class LUAKZNN1 and the flight status is USED.                                                                                                                                                                                                                                                                                                                                                             |
      | Round trip with 1 connection                      | 0012120198098 | CHESKIEWICZ | ROBYN     | 03/08/2020    | TXFAFG | AA2882 leaving PIT(Pittsburgh, PA) to DFW(Dallas/Fort Worth, TX) on 2020-03-08 at 05:30:00 and arriving on 2020-03-08 at 07:47:00 in class L0AIZRN1 and the flight status is USED. AA1064 leaving DFW(Dallas/Fort Worth, TX) to SAN(San Diego, CA) on 2020-03-08 at 08:30:00 and arriving on 2020-03-08 at 09:39:00 in class L0AIZRN1 and the flight status is USED. | AA1245 leaving SAN(San Diego, CA) to CLT(Charlotte, NC) on 2020-03-13 at 22:29:00 and arriving on 2020-03-14 at 05:57:00 in class L0AIZRN1 and the flight status is USED. OH5596 leaving CLT(Charlotte, NC) to PIT(Pittsburgh, PA) on 2020-03-14 at 08:05:00 and arriving on 2020-03-14 at 09:41:00 in class L0AIZRN1 and the flight status is USED.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
      | Round trip with no connection                     | 0012382521768 | NATZIC      | CELINA    | 12/22/2019    | GJWVRI | AA2202 leaving LAX(Los Angeles, CA) to BNA(Nashville, TN) on 2019-12-22 at 13:23:00 and arriving on 2019-12-22 at 19:21:00 in class GVAIZNB3 and the flight status is USED.                                                                                                                                                                                          | AA2183 leaving BNA(Nashville, TN) to LAX(Los Angeles, CA) on 2019-12-26 at 20:16:00 and arriving on 2019-12-26 at 23:02:00 in class GVAIZNB3 and the flight status is USED.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | One way with one connection                       | 0012120308259 | HAYES       | SOPHIA    | 03/07/2020    | XUITDR | OH5288 leaving STL(St Louis, MO) to CLT(Charlotte, NC) on 2020-03-07 at 18:24:00 and arriving on 2020-03-07 at 21:20:00 in class SVAHZNO1 and the flight status is USED. AA2799 leaving CLT(Charlotte, NC) to BWI(Baltimore/Washington, MD) on 2020-03-07 at 22:30:00 and arriving on 2020-03-07 at 23:50:00 in class SVAHZNO1 and the flight status is USED.        |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
      | One way with no connection                        | 0012399149997 | LEAHU       | BIANCA    | 01/24/2020    | SWGZIM | AA844 leaving ORD(Chicago, IL) to PHL(Philadelphia, PA) on 2020-01-24 at 18:55:00 and arriving on 2020-01-24 at 21:58:00 in class Y20AUC and the flight status is USED.                                                                                                                                                                                              |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
      | International round trip with 1 connection        | 0012120344750 | HAEEMS      | NATHANIEL | 05/15/2020    | KAFAFY | AA2943 leaving YYZ(Toronto, Canada) to CLT(Charlotte, NC) on 2020-05-15 at 15:59:00 and arriving on 2020-05-15 at 18:12:00 in class Q0ACZNN3 and the flight status is EXCH. AA2494 leaving CLT(Charlotte, NC) to MIA(Miami, FL) on 2020-05-15 at 19:55:00 and arriving on 2020-05-15 at 21:54:00 in class Q0ACZNN3 and the flight status is EXCH.                    | AA1246 leaving MIA(Miami, FL) to YYZ(Toronto, Canada) on 2020-05-18 at 20:33:00 and arriving on 2020-05-18 at 23:45:00 in class Q0ACZNN3 and the flight status is EXCH.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |

  Scenario Outline: Verify Form of Payment and ticket fare details
    Given I want to retrieve payment details for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with fopIssueDate "<fopIssueDate>", fopTypeCode "<fopTypeCode>",  fopTypeDescription "<fopTypeDescription>",  fopAccountNumberLastFour "<fopAccountNumberLastFour>", fopAmount "<fopAmount>", and fopCurrencyCode "<fopCurrencyCode>"
    Then I get a successful response with baseFareAmount "<baseFareAmount>", baseFareCurrencyCode "<baseFareCurrencyCode>", totalFareAmount "<totalFareAmount>", taxFareAmount "<taxFareAmount>", showPassangerTotal "<showPassangerTotal>"

    Examples:
      | scenario                                             | ticketNumber  | lastName         | firstName  | departureDate | fopIssueDate | fopTypeCode | fopTypeDescription                   | fopAccountNumberLastFour | fopAmount | fopCurrencyCode | baseFareAmount | baseFareCurrencyCode | totalFareAmount | taxFareAmount | showPassangerTotal |
      | CreditCard Visa                                      | 0012123252684 | GONZALEZ ALFONSO | BARBARO SR | 04/16/2020    | 2020-03-30   | CCBA        | Visa ending in 7420                  | 7420                     | 274.55    | USD             | 183.00         | USD                  | 274.55          | 91.55         | true               |
      | Fare paid in USD & originated from US                | 0012123151703 | MCGINN           | JOSEPH     | 08/06/2020    | 2020-03-28   | CCIK        | Mastercard ending in 3682            | 3682                     | 11.20     | USD             | 0.00           | USD                  | 11.20           | 11.20         | true               |
      | CreditCard Exchange                                  | 0012123313110 | CORBETT          | DONAI      | 04/03/2020    | 2020-03-30   | EF          | Exchange                             | 3747                     | 0.00      | USD             | 79.00          | USD                  | 93.60           | 14.60         | true               |
      | Another CreditCard Exchange Add Collect multiple fop | 0012376658996 | FARRELL          | MORGAN     | 04/17/2020    | 2019-09-07   | CCIK        | Exchange - Mastercard ending in 4605 | 4605                     | 656.13    | USD             | 126.00         | USD                  | 656.13          | 530.13        | true               |
      | CreditCard Exchange Add Collect                      | 0012122778151 | ALMODOVAR        | ANGELA     | 12/16/2020    | 2020-03-23   | CCBA        | Exchange - Visa ending in 3210       | 3210                     | 105.10    | USD             | 530.23         | USD                  | 616.40          | 86.17         | true               |
      | Ancillaries with different currency code             | 0012117846087 | MORALESSANCHEZ   | RAFAEL     | 05/01/2020    | 2020-02-25   | CCBA        | Visa ending in 6206                  | 6206                     | 551.36    | EUR             | 353.00         | EUR                  | 551.36          | 198.36        | false              |

  Scenario Outline: Verify Taxes
    Given I want to retrieve payment details for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with baseFareAmount "<baseFareAmount>", baseFareCurrencyCode "<baseFareCurrencyCode>", totalFareAmount "<totalFareAmount>", and taxesString "<taxesString>"

    Examples:
      | scenario                               | ticketNumber  | lastName         | firstName    | departureDate | baseFareAmount | baseFareCurrencyCode | totalFareAmount | taxesString                                                                                                                                                                                                                                                                                                                                                      |
      | Taxes - base fare currency HSD, XF HKD | 0012332330812 | PAN              | CHENGHSIN MR | 03/07/2019    | 6650.00        | HKD                  | 8740.00         | 1,YR, CARRIER SURCHARGE,,1168.00,HKD; 2,HK,AIR PAX DEPARTURE TAX,,120.00,HKD; 3,G3,AIRPORT CONSTRUCTION,,160.00,HKD;4,I5,AIRPORT PAX SEC CHRG,,50.00,HKD;5,AY,SECURITY SERVICE FEE,,88.00,HKD; 6,ZQ,AIRPORT FACILITY CHG,,204.00,HKD; 7,YC,U.S. CUSTOMS,,92.00,HKD; 8,XY,U.S. IMMIGRATION,,110.00,HKD; 9,XA,APHIS,,62.00,HKD; 10,XF,SYS GEN PFC,DFW,36.00,HKD    |
      | Taxes - base fare currency USD, NO XF  | 0012377560479 | FERNANDEZ        | ENRIQUE      | 10/09/2019    | 656.00         | USD                  | 826.83          | 1,XC, GUATAMALA INTL DEP TA,,30.00,USD; 2,XB,VALUE ADDED TAX,,78.70,USD; 3,QQ,GUATEMALA AIRPORT SEC,,2.60,USD;4,AY,SECURITY SERVICE FEE,,5.60,USD;5,US2,INT'L DEPT/ARRIVAL TA,,37.20,USD;6,YC,U.S. CUSTOMS,,5.77,USD; 7,XY,U.S. IMMIGRATION,,7.00,USD; 8,XA,APHIS,,3.96,USD;                                                                                     |
      | Taxes - base fare currency USD, XF USD | 0012371807025 | FRANCIS          | MARIA        | 08/25/2019    | 436.00         | USD                  | 536.03          | 1,AY, SECURITY SERVICE FEE,,5.60,USD; 2,CO,AIRPORT TAX,,36.00,USD; 3,US2,INT'L DEPT/ARRIVAL TA,,37.20,USD;4,YC,U.S. CUSTOMS,,5.77,USD;5,XY,U.S. IMMIGRATION,,7.00,USD; 6,XA,APHIS,,3.96,USD;7,XF,SYS GEN PFC (MIA),MIA,4.50,USD;                                                                                                                                 |
      | Taxes - base fare currency CAD, XF CAD | 0012141584117 | RAHMANI LASHGARI | KIAN         | 10/20/2020    | 303.99         | CAD                  | 401.59          | 1,CA4, CANADIAN SECURITY FEE,,12.10,CAD; 2,XG8,CANADIAN GST,,15.80,CAD; 3,RC2,HARMONIZED TAX,,3.25,CAD; 4,SQ,TORONTO AIR IMPROV FE,,25.00,CAD; 5,AY,SECURITY SERVICE FEE,,14.90,CAD; 6,YC,U.S. CUSTOMS,,7.97,CAD; 7,XY2,U.S. IMMIGRATION,,9.32,CAD; 8,XA,APHIS,,5.27,CAD; 9,XF,SYS GEN PFC,CLT,3.99,CAD;                                                         |
      | Taxes - base fare currency EUR, USD    | 0012376578402 | SANJOSE          | MARIA        | 05/15/2020    | 1325.00        | USD                  | 1750.93         | 1,YRI, CARRIER SURCHARGE,,333.60,USD; 2,JD3,DEPARTURE CHARGE,,23.70,USD; 3,QV3,SPAIN SECURITY CHG IN,,3.90,USD; 4,OG,AVIATION SFTY & SEC F,,0.70,USD; 5,US2,INT'L DEPT/ARRIVAL TA,,37.20,USD; 6,AY,SECURITY SERVICE FEE,,5.60,USD; 7,YC,U.S. CUSTOMS,,5.77,USD; 8,XY2,U.S. IMMIGRATION,,7.00,USD; 9,XA,APHIS,CLT,3.96,USD; 10,XF,SYS GEN PFC (JFK),JFK,4.50,USD; |

  Scenario Outline: Verify Zps
    Given I want to retrieve ZP taxes details for scenario "<scenario>"
    When I search the passenger info with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with the correct taxamount "<taxesamount>" and zpamount "<zpstring>"

    Examples:
      | scenario                          | ticketNumber  | lastName  | firstName | departureDate | taxesamount | zpstring                                                                                                                            |
      | Taxes-one ZP                      | 0012111159293 | BAMMIDI   | RAVINDRA  | 02/02/2020    | 18.03       | [Tax{taxCodeSequenceId='4', taxCode='ZP', taxDescription='U.S. SEGMENT TAX', cityCode='', taxAmount='4.30', taxCurrencyCode='USD'}] |
      | Taxes- another ticket with one ZP | 0012110664024 | RUTT      | JANE      | 03/14/2020    | 24.05       | [Tax{taxCodeSequenceId='5', taxCode='ZP', taxDescription='U.S. SEGMENT TAX', cityCode='', taxAmount='4.30', taxCurrencyCode='USD'}] |
      | Taxes- no ZP                      | 0012397883850 | VEMIREDDI | VISALI    | 02/21/2020    | 87.05       | []                                                                                                                                  |
      | Taxes- another ticket with no ZP  | 0012384477850 | YAMAUCHI  | HOLLY     | 03/07/2020    | 159.15      | []                                                                                                                                  |

  Scenario Outline: Verify statusMessage when bulk ticket is found
    Given I want to retrieve a bulk ticket receipt for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a response with bulk ticket found message "<statusMessage>"

    Examples:
      | scenario                                                  | ticketNumber  | lastName | firstName    | departureDate | statusMessage |
      | Ticket having trip details but not cost details in mosaic | 0012111527988 | COLLINS  | TERESA       | 02/20/2020    | BulkTicket    |
      | Another bulk ticket                                       | 0012111006637 | SEYMOUR  | JENNIFER LEE | 04/30/2020    | BulkTicket    |

  @0ancillaries-fops
  Scenario Outline: Zero ancillaries with FOP amt = ticket total amt, FOP amt = passenger amt
    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response without ancillaries rowCount "<rowCount>", fopAmt "<fopAmt>", totalFareAmount "<totalFareAmount>", passengerTotalAmount "<passengerTotalAmount>"

    Examples:
      | scenario                                                                  | ticketNumber  | lastName | firstName | departureDate | rowCount | fopAmt | totalFareAmount | passengerTotalAmount |
      | Zero ancillaries with FOP amt = ticket total amt, FOP amt = passenger amt | 0012384122707 | TRAISMAN | LEO       | 12/20/2019    | 1        | 214.30 | 214.30          | 214.30               |

  @1ancillaries-2fops
  Scenario Outline: One ancillary bought same date as ticket (2 FOPs), pax amt = ticket FOP amt + Ancillary FOP amt
    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with one ancillary rowCount "<rowCount>", fopAmt "<fopAmt>", passengerTotalAmount "<passengerTotalAmount>", anclryFOPAmt1 "<anclryFOPAmt1>", anclryFOPAmt2 "<anclryFOPAmt2>", anclryFOPIssueDate "<anclryFOPIssueDate>", anclryFOPTypeCode "<anclryFOPTypeCode>", anclryFOPAccountNumberLast4 "<anclryFOPAccountNumberLast4>", anclryDocNbr "<anclryDocNbr>", anclryIssueDate "<anclryIssueDate>", anclryPriceCurrencyAmount "<anclryPriceCurrencyAmount>", anclrySalesCurrencyAmount "<anclrySalesCurrencyAmount>", anclryTaxCurrencyAmount "<anclryTaxCurrencyAmount>"

    Examples:
      | scenario                                                                                        | ticketNumber  | lastName  | firstName | departureDate | rowCount | passengerTotalAmount | anclryFOPAmt1 | anclryFOPIssueDate | anclryFOPTypeCode | anclryFOPAccountNumberLast4 | anclryDocNbr | anclryIssueDate | anclryPriceCurrencyAmount | anclrySalesCurrencyAmount | anclryTaxCurrencyAmount |
      | One ancillary bought same date as ticket (2 FOPs), pax amt = ticket FOP amt + Ancillary FOP amt | 0012380586501 | ARMSTRONG | DANA      | 06/26/2020    | 2        | 123.16               | 117.56        | 2019-09-28         | Mastercard        | 1274                        | 657212986    | 2019-09-28      | 117.56                    | 117.56                    | 0.00                    |

  @1ancillaries-2fops
  Scenario Outline: One ancillaries bought with ticket (2 FOPs), pax amt = ticket FOP amt + Ancillary FOP amts
    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with ancillaries and fop details fopIssueDate "<fopIssueDate>", fopTypeCode "<fopTypeCode>", fopTypeDescription "<fopTypeDescription>", fopAccountNumberLastFour "<fopAccountNumberLastFour>", fopAmount "<fopAmount>", fopCurrencyCode "<fopCurrencyCode>" and fopIndex "<fopIndex>"

    Examples:
      | scenario                | ticketNumber  | lastName  | firstName | departureDate | fopIssueDate | fopTypeCode | fopTypeDescription        | fopAccountNumberLastFour | fopAmount | fopCurrencyCode | fopIndex |
      | 1ancillaries-firstfops  | 0012380586501 | ARMSTRONG | DANA      | 06/26/2020    | 2019-09-28   | CCIK        | Mastercard ending in 1274 | 1274                     | 5.60      | USD             | 0        |
      | 1ancillaries-secondfops | 0012380586501 | ARMSTRONG | DANA      | 06/26/2020    | 2019-09-28   | CCIK        | Mastercard ending in 1274 | 1274                     | 117.56    | USD             | 1        |

  @2ancillaries-3fops
  Scenario Outline: Two ancillaries bought with ticket (3 FOPs), pax amt = ticket FOP amt + Ancillary FOP amts
    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with two ancillaries rowCount "<rowCount>",passengerTotalAmount "<passengerTotalAmount>",anclryDocNbrOne "<anclryDocNbrOne>",anclryIssueDateone "<anclryIssueDateone>",anclryProdCodeOne "<anclryProdCodeOne>",anclryProdNameOne "<anclryProdNameOne>",anclryPriceCurrencyAmountOne "<anclryPriceCurrencyAmountOne>",anclryPriceCurrencyCodeOne "<anclryPriceCurrencyCodeOne>",anclrySalesCurrencyAmountOne "<anclrySalesCurrencyAmountOne>",anclrySalesCurrencyCodeOne "<anclrySalesCurrencyCodeOne>",anclryTaxCurrencyAmountOne "<anclryTaxCurrencyAmountOne>",anclryDocNbrTwo "<anclryDocNbrTwo>",anclryIssueDateTwo "<anclryIssueDateTwo>",anclryProdCodeTwo "<anclryProdCodeTwo>",anclryProdNameTwo "<anclryProdNameTwo>",anclryPriceCurrencyAmountTwo "<anclryPriceCurrencyAmountTwo>",anclryPriceCurrencyCodeTwo "<anclryPriceCurrencyCodeTwo>",anclrySalesCurrencyAmountTwo "<anclrySalesCurrencyAmountTwo>",anclrySalesCurrencyCodeTwo "<anclrySalesCurrencyCodeTwo>",anclryTaxCurrencyAmountTwo "<anclryTaxCurrencyAmountTwo>"

    Examples:
      | scenario     | ticketNumber  | lastName | firstName | departureDate | rowCount | passengerTotalAmount | anclryDocNbrOne | anclryIssueDateone | anclryProdCodeOne | anclryProdNameOne     | anclryPriceCurrencyAmountOne | anclryPriceCurrencyCodeOne | anclrySalesCurrencyAmountOne | anclrySalesCurrencyCodeOne | anclryTaxCurrencyAmountOne | anclryDocNbrTwo | anclryIssueDateTwo | anclryProdCodeTwo | anclryProdNameTwo            | anclryPriceCurrencyAmountTwo | anclryPriceCurrencyCodeTwo | anclrySalesCurrencyAmountTwo | anclrySalesCurrencyCodeTwo | anclryTaxCurrencyAmountTwo |
      | 2ancillaries | 0012132820896 | ADAMS    | PAUL      | 08/13/2020    | 3        | 640.51               | 620518471       | 2020-08-13         | 099               | MSR-OTHER NON TAXABLE | 100.00                       | USD                        | 100.00                       | USD                        | 0.00                       | 619785952       | 2020-07-16         | 090               | MAIN CABIN EXTRA (LAX - PHL) | 44.31                        | USD                        | 44.31                        | USD                        | 0.00                       |

  @2ancillaries-3fops
  Scenario Outline: Two ancillaries bought with ticket (3 FOPs), pax amt = ticket FOP amt + Ancillary FOP amts
    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with ancillaries and fop details fopIssueDate "<fopIssueDate>", fopTypeCode "<fopTypeCode>", fopTypeDescription "<fopTypeDescription>", fopAccountNumberLastFour "<fopAccountNumberLastFour>", fopAmount "<fopAmount>", fopCurrencyCode "<fopCurrencyCode>" and fopIndex "<fopIndex>"

    Examples:
      | scenario                | ticketNumber  | lastName | firstName | departureDate | fopIssueDate | fopTypeCode | fopTypeDescription  | fopAccountNumberLastFour | fopAmount | fopCurrencyCode | fopIndex |
      | 2ancillaries-firstfops  | 0012132820896 | ADAMS    | PAUL      | 08/13/2020    | 2020-07-16   | CCBA        | Visa ending in 0270 | 0270                     | 496.20    | USD             | 0        |
      | 2ancillaries-secondfops | 0012132820896 | ADAMS    | PAUL      | 08/13/2020    | 2020-08-13   | CCBA        | Visa ending in 4890 | 4890                     | 100.00    | USD             | 1        |
      | 2ancillaries-thirdfops  | 0012132820896 | ADAMS    | PAUL      | 08/13/2020    | 2020-07-16   | CCBA        | Visa ending in 0270 | 0270                     | 44.31     | USD             | 2        |

  Scenario: Search ticket with 3 ancillaries bought on different dates, pax amt = ticket FOP amt + Ancillary FOP amts
    When I search ticket number with below criteria
      | ticketNumber  | 0012120199665 |
      | firstName     | KIMBERLY      |
      | lastName      | MCDONALD      |
      | departureDate | 03/08/2020    |
    Then I get a successful response with the following ancillaries
      | rowCount             | 4                            |
      | passengerTotalAmount | 971.44                       |
      | anclryFOP1IssueDate  | 2020-03-07                   |
      | anclryFOPName1       | MAIN CABIN EXTRA (BPT - DFW) |
      | anclryFOPName2       | PAID LFB UPGRADE (BPT - DFW) |
      | anclryFOPName3       | PREFERRED SEATS (BPT - DFW)  |
      | ticketFOP            | 837.79                       |
      | anclryFOPAmt1        | 34.94                        |
      | anclryFOPAmt2        | 64.47                        |
      | anclryFOPAmt3        | 34.24                        |

  Scenario Outline: Invalid airline code
    Given I want to retrieve payment details - ancillaries for scenario "<scenario>"
    When I search with ticket number "<ticketNumber>", last name "<lastName>", first name "<firstName>", departure date "<departureDate>"
    Then I get a successful response with invalid airline code rowCount "<rowCount>"

    Examples:
      | scenario             | ticketNumber  | lastName | firstName | departureDate | rowCount |
      | Invalid airline code | 0022132820896 | ADAMS    | PAUL      | 08/13/2020    | 3        |
