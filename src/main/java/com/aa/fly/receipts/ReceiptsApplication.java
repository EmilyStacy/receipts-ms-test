package com.aa.fly.receipts;

/**
 * Created by 629874 on 4/25/2019.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ReceiptsApplication implements CommandLineRunner

{



   @Autowired
   JdbcTemplate jdbcTemplate;

   private static Logger logger = LoggerFactory.getLogger( ReceiptsApplication.class );

   public static void main( String[] args )
   {

      SpringApplication.run( ReceiptsApplication.class,
            args );
   }

   /* (non-Javadoc)
    * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
    */
   @Override
   public void run( String... args )
         throws Exception
   {
      logger.info( "$$$$$$$$$$$$$$$$$$$$$$ select 1 $$$$$$$$$$$$$$$$:  {} ",
            jdbcTemplate.queryForObject( "select 1",
                  String.class ) );

   }


}
