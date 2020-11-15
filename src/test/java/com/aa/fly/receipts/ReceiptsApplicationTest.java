package com.aa.fly.receipts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
public class ReceiptsApplicationTest
{
//    @Autowired
//    private ReceiptsMSException receiptsMSException;

    @Test
    public void ensureApplicationStarts()
    {
        ReceiptsApplication.main( new String[] {} );
//        Assert.assertNotNull( receiptsMSException );
    }

}
