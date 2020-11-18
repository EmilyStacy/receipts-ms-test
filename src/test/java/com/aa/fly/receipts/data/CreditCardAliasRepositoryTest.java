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

    @Test
    public void testLoadCreditCardAliases() throws NoSuchMethodException {
    Mockito.when(jdbcTemplate.queryForRowSet(anyString()))
                .thenReturn(resultSet);
    Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
    Mockito.when(resultSet.getString("CREDIT_CARD_TYPE_ALIAS_CD")).thenReturn("AA ");
    Mockito.when(this.resultSet.getString("CREDIT_CARD_TYPE_ALIAS_DESC")).thenReturn("AA PERSONAL CARD");
    creditCardAliasRepository.loadCreditCardAliases();
        Assert.assertEquals("AA Personal Card",creditCardAliasRepository.getCreditCardAliasMap().get("CCAA"));
    }

    @Test public void testLoadCreditCardAliasesCreateMap() {
        Mockito.when(jdbcTemplate.queryForRowSet(anyString()))
                .thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString("CREDIT_CARD_TYPE_ALIAS_CD")).thenReturn("AA PERSONAL CARD");
        creditCardAliasRepository.loadCreditCardAliases();
        Assert.assertEquals(1, creditCardAliasRepository.getCreditCardAliasMap().size());

    }

}



