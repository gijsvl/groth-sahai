package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.entities.impl.CustomQuadraticElement;
import edu.jhu.isi.grothsahai.entities.impl.QuarticElement;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class CustomQuadraticElementTest extends BaseTest {
    @Test
    public void constructQuadraticElement() {
        final Pairing pairing = createPairing();
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final CustomQuadraticElement<Element> element = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, pairing);
        assertEquals(element1, element.getX());
        assertEquals(element2, element.getY());
    }

    @Test
    public void testPair() {
        final Pairing pairing = createPairing();
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Field g2 = pairing.getG2();
        final Element element3 = g2.newRandomElement();
        final Element element4 = g2.newRandomElement();
        final CustomQuadraticElement<Element> elementLeft = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g2),
                element3, element4, pairing);
        assertEquals(elementLeft.pair(elementRight), new QuarticElement<Element>(new QuadraticField<Field, QuadraticElement>(new SecureRandom(), pairing.getGT()),
                pairing.pairing(elementLeft.getX(), elementRight.getX()),
                pairing.pairing(elementLeft.getX(), elementRight.getY()),
                pairing.pairing(elementLeft.getY(), elementRight.getX()),
                pairing.pairing(elementLeft.getY(), elementRight.getY())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPair_illegalArgument() {
        final Pairing pairing = createPairing();
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final CustomQuadraticElement<Element> elementLeft = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, pairing);
        elementLeft.pair(elementRight);
    }
}
