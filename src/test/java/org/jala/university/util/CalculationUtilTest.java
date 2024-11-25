package org.jala.university.util;

import org.jala.university.utils.CalculationUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculationUtilTest {

    @Test
    public void testGetTotalPayable() {
        
        // Valores de teste
        Double amountBorrowed = 1000.0;
        int numberOfInstallments = 12;
        
        // Valor esperado calculado manualmente
        Double expectedTotalPayable = 1000.0 * Math.pow(1 + 0.02, 12);
        
        // Chama o método a ser testado
        Double actualTotalPayable = CalculationUtil.getTotalPayable(amountBorrowed, numberOfInstallments);
        
        // Verifica se o valor retornado é igual ao valor esperado
        assertEquals(expectedTotalPayable, actualTotalPayable, 0.01);

    }
}
