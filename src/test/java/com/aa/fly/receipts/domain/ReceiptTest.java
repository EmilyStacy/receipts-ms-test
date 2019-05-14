package com.aa.fly.receipts.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 629874 on 5/13/2019.
 */
public class ReceiptTest
{
   private Receipt receipt;

   @Before
   public void setUp() throws Exception
   {
      receipt = new Receipt();
   }

   @Test
   public void getFirstName() throws Exception
   {
      receipt.setFirstName("FirstName");
      assertEquals("FirstName", receipt.getFirstName());
   }

   @Test
   public void setFirstName() throws Exception
   {
      receipt.setFirstName("FirstName");
      assertEquals("FirstName", receipt.getFirstName());
   }

   @Test
   public void getLastName() throws Exception
   {
      receipt.setLastName("LastName");
      assertEquals("LastName", receipt.getLastName());
   }

   @Test
   public void setLastName() throws Exception
   {
      receipt.setLastName("LastName");
      assertEquals("LastName", receipt.getLastName());
   }

   @Test
   public void getReceiptTotal() throws Exception
   {
      receipt.setReceiptTotal("250.20");
      assertEquals("250.20", receipt.getReceiptTotal());
   }

   @Test
   public void setReceiptTotal() throws Exception
   {
      receipt.setReceiptTotal("250.20");
      assertEquals("250.20", receipt.getReceiptTotal());
   }

   @Test
   public void testToString() throws Exception
   {
      receipt.setFirstName("FirstName");
      receipt.setLastName("LastName");
      assertTrue(receipt.toString().contains("FirstName"));
      assertTrue(receipt.toString().contains("LastName"));
   }

}
