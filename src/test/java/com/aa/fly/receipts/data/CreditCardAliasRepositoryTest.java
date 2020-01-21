package com.aa.fly.receipts.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import com.aa.fly.receipts.data.CreditCardAliasRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class CreditCardAliasRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlRowSet resultSet;

    @InjectMocks
    private CreditCardAliasRepository creditCardAliasRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");



@Test
    public void toTitleCaseReturnNullWhenInputIsNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
    Method method = creditCardAliasRepository.getClass().getDeclaredMethod("toTitleCase", String.class);
    method.setAccessible(true);
    String input = null;
    String returnValue =  (String) method.invoke(creditCardAliasRepository, input);
    assertNull(returnValue);

}

@Test
    public void toTitleCaseReturnLowerCaseWhenTokenLengthBiggerThan3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
    Method method = creditCardAliasRepository.getClass().getDeclaredMethod("toTitleCase", String.class);
    method.setAccessible(true);
    String input = "AA PERSONAL CARD";
    String returnValue =  (String) method.invoke(creditCardAliasRepository, input);
    assertEquals("AA Personal Card", returnValue);

}

    @Test
    public void toTitleCaseReturnUpperCaseWhenTokenLengthShorterThan3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        Method method = creditCardAliasRepository.getClass().getDeclaredMethod("toTitleCase", String.class);
        method.setAccessible(true);
        String input = "AA AEP CARD";
        String returnValue =  (String) method.invoke(creditCardAliasRepository, input);
        assertEquals("AA AEP Card", returnValue);

    }

// test loadCreditCardAliases

//    @Test
//    public void testLoadCreditCardAliases() throws NoSuchMethodException {
//    //arrange
//    //SqlRowSet resultSet = Mockito.mock(SqlRowSet.class);
//    Mockito.when(jdbcTemplate.queryForRowSet(anyString()))
//                .thenReturn(resultSet);
//    Mockito.when(this.resultSet.next()).thenReturn(true).thenReturn(false);
//    Mockito.when(this.resultSet.getString("CREDIT_CARD_TYPE_ALIAS_CD")).thenReturn("AA ");
//        Mockito.when(this.resultSet.getString("CREDIT_CARD_TYPE_ALIAS_DESC")).thenReturn("AA PERSONAL CARD");
//    //act
//    creditCardAliasRepository.loadCreditCardAliases();
//    //assert
//    Assert.assertEquals("CC AAPERSONALCARD",this.resultSet.getString(" CREDIT_CARD_TYPE_ALIAS_CD "));
//    }
//
//    @Test public void testLoadCreditCardAliasesCreateMap() throws NoSuchMethodException, ParseException {
//       //arrange
//       // SqlRowSet resultSet = Mockito.mock(SqlRowSet.class);
//        Mockito.when(jdbcTemplate.queryForRowSet(anyString()))
//                .thenReturn(this.resultSet);
//        Mockito.when(this.resultSet.next()).thenReturn(true).thenReturn(false);
//        Mockito.when(this.resultSet.getString("CREDIT_CARD_TYPE_ALIAS_CD")).thenReturn("AA PERSONAL CARD");
//        ReflectionTestUtils.setField(creditCardAliasRepository, "creditCardAliasMap", getcreditCardAliasMap());
//        //act
//        creditCardAliasRepository.loadCreditCardAliases();
//        //assert
//        //Assert.assertTrue(EqualsBuilder.reflectionEquals(1, getcreditCardAliasMap().size()));
//        Assert.assertEquals(1, getcreditCardAliasMap().size());
//
//    }
//
//    private Map<String, String> getcreditCardAliasMap() throws ParseException {
//    Map<String, String> creditCardAliasMap = new HashMap<>();
//    return creditCardAliasMap;
//    }

}



