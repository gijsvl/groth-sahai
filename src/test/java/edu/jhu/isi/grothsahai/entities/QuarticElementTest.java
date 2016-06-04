package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.BaseTest;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class QuarticElementTest extends BaseTest {
    @Test
    public void constructQuarticElement() {
        final Pairing pairing = createPairing();
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        final QuarticElement<Element> element = new QuarticElement<Element>(
                new QuadraticField(new SecureRandom(), g1),
                element1, element2, element3, element4);
        assertEquals(element1, element.getW());
        assertEquals(element2, element.getX());
        assertEquals(element3, element.getY());
        assertEquals(element4, element.getZ());
    }

    @Test
    public void constructQuarticElementFromQuadraticElement() {
        final Pairing pairing = createPairing();
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        final CustomQuadraticElement<Element> elementLeft = new CustomQuadraticElement<Element>(
                new QuadraticField(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<Element>(
                new QuadraticField(new SecureRandom(), g1),
                element3, element4, pairing);
        final CustomQuadraticElement<Element> quadraticElement = new CustomQuadraticElement<Element>(
                new QuadraticField(new SecureRandom(), g1),
                elementLeft, elementRight, pairing);
        final QuarticElement<Element> element = new QuarticElement<Element>(quadraticElement);
        assertEquals(element1, element.getW());
        assertEquals(element2, element.getX());
        assertEquals(element3, element.getY());
        assertEquals(element4, element.getZ());
    }
}
