# ReceiptsMS (Self Service Receipts data access microservice)
- ## Background
  ##### This microservice is the key component as a JDBC data access layer to Self Service Receipts data source - Mosaic. And since Mosaic, at this point, resides inside AA enterprise infrastructure, at this writing AA does not provide an easy solution to source JDBC data outside of company network, therefore forcing this microservice to be deployed to AA private cloud - ePaaS.
  #### Specially noted that this microservice also serves Wi-Fi subscriptions receipt requests coming from legacy aa.com.  

- ## High level architecture
  ##### Following is the high level microservices including ReceiptsMS process flow and cloud representation from architecture stand point, 
  1) Firstly, ReceiptsBFF in BMX Cloud Foundry sends requests via eSOA to ReceiptsMS in ePaaS.
  
  2) ReceiptsMS constructs database query by applying request search criteria to where clause and invoking JDBC call with the query to Mosaic. Mosaic sends back the query results which then parsed by ReceiptsMS, and building response json sending back to ReceiptsBFF.

- ## Process flow diagram
   ![process flow](images/CloudMS.PNG)
  
- ## Application configuration files
   (Configured to always connect to Mosaic production for data retrieval)
   - **src/main/resources/application.yml**
   - **src/main/resources/application-prod.yml** 

- ## Running the application locally
  ### - VPN is needed
  ### - Plug actual values for prodFunctonalId and prodFunctonalIdPassword below into application.yml temporarily. Ask other developers for the values.
  - spring.datasource.username: prodFunctonalId
  - spring.datasource.password: prodFunctonalIdPassword
	    
  ### Navigate to src\main\java\com\aa\fly\receipts and start ReceiptsApplication class in appropriate mode (run/debug).
  #### (Observing the IDE console for any unexpected error(s))

- ## Submit requests to local running app
  - Once the app runs correctly locally it will be listening on localhost:8093 (unless server.port changed in application.yaml) 
  - Use any local tool capable of sending requests to a restful end point. Sample took like Postman or ReadyAPI should all work with below request,
  	#### - End point: localhost:8093
  	#### - Method: POST
  	#### - Header: X-Transaction-Id=0123456789
  	#### - Body (raw):
			{
			    "ticketNumber": "0012398358969",
			    "firstName": "ANTHONY",
			    "lastName": "CARINGI",
			    "departureDate": "02/10/2020"
			}  	
     
- ## Automated Testing - can run within IDE or command prompt. For command prompt,
  ### How to run the unit test suite
  
    - **mvn test**
    - Local test results can be checked by opening index.html with browser at 
      project foler/target/site/jacoco/index.html

  ### How to run integration test
    - **mvn verify -Pintegration-tests -Dcucumber.options='--tags @TicketAndFees' -Dbranch.application.url="http://localhost:8094"**
      
      (Be sure to start the app locally first)
      (-Dcucumber.options='--tags @TicketAndFees' allows only running features and/or scenarios with the tag, all scenarios will run when omitted the argument)
      
    - Local test results can be checked by opening index.html with browser at 
      project foler/target/cucumberReport/index.html

- ## SonarQube
  - Code quality, warnings and test coverage for each files can be found in SonarQube.
     - link:- https://sonarqube.aa.com/dashboard?id=tr.receipts-ms
     - Best practice is to also install SonarLint plugin for IDE so violations can be detected prior to committing the code.
     
- ## Coverity
  - Code quality and security. 
     - link:- https://coverity.aa.com/reports.htm#v10164/p10524
     - Best practice is to also configure IDE the ability to do Coverity local scan so violations can be detected prior to committing the code.

- ## Javadoc
  #### TBC
