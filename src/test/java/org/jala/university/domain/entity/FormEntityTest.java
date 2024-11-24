package org.jala.university.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormEntityTest {

    @Test
    void testSetIncomeAndCalculateMaximumAmount() {

        FormEntity formEntity = new FormEntity();
        Double income = 1000.0;


        formEntity.setIncome(income);


        assertEquals(income, formEntity.getIncome());
        assertEquals(1800.0, formEntity.getMaximumAmount());
    }

    @Test
    void testConstructorWithIncomeAndProofOfIncome() {

        Double income = 2000.0;
        byte[] proofOfIncome = {1, 2, 3};


        FormEntity formEntity = new FormEntity(income, proofOfIncome);


        assertEquals(income, formEntity.getIncome());
        assertArrayEquals(proofOfIncome, formEntity.getProofOfIncome());
        assertEquals(3600.0, formEntity.getMaximumAmount());
    }

    @Test
    void testCalculateMaximumAmount() {

        FormEntity formEntity = new FormEntity();
        formEntity.setIncome(1500.0);


        formEntity.calculateMaximumAmount();

        assertEquals(2700.0, formEntity.getMaximumAmount());
    }

    @Test
    void testSetProofOfIncome() {

        FormEntity formEntity = new FormEntity();
        byte[] proofOfIncome = {10, 20, 30};


        formEntity.setProofOfIncome(proofOfIncome);

        assertArrayEquals(proofOfIncome, formEntity.getProofOfIncome());
    }

    @Test
    void testDefaultConstructor() {

        FormEntity formEntity = new FormEntity();


        assertNull(formEntity.getId());
        assertNull(formEntity.getIncome());
        assertNull(formEntity.getProofOfIncome());
        assertNull(formEntity.getMaximumAmount());
    }
}
