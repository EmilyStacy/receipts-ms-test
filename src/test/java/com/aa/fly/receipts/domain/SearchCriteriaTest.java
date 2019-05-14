package com.aa.fly.receipts.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 629874 on 5/13/2019.
 */
public class SearchCriteriaTest
{

   private SearchCriteria criteria = new SearchCriteria();

   @Before
   public void setUp() throws Exception
   {
      criteria = new SearchCriteria();
   }

   @Test
   public void getTicketNumber() throws Exception
   {
      criteria.setTicketNumber("0012362111828");
      assertEquals("0012362111828", criteria.getTicketNumber());
   }

   @Test
   public void setTicketNumber() throws Exception
   {
      criteria.setTicketNumber("0012362111828");
      assertEquals("0012362111828", criteria.getTicketNumber());
   }

   @Test
   public void getLastName() throws Exception
   {
      criteria.setLastName("LastName");
      assertEquals("LastName", criteria.getLastName());
   }

   @Test
   public void setLastName() throws Exception
   {
      criteria.setLastName("LastName");
      assertEquals("LastName", criteria.getLastName());
   }

   @Test
   public void getFirstName() throws Exception
   {
      criteria.setFirstName("FirstName");
      assertEquals("FirstName", criteria.getFirstName());
   }

   @Test
   public void setFirstName() throws Exception
   {
      criteria.setFirstName("FirstName");
      assertEquals("FirstName", criteria.getFirstName());

   }

   @Test
   public void testToString() throws Exception
   {
      criteria.setFirstName("FirstName");
      criteria.setLastName("LastName");
      assertTrue(criteria.toString().contains("FirstName"));
      assertTrue(criteria.toString().contains("LastName"));
   }

}
