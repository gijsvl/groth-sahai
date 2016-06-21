package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
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
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element3, element4, pairing);
        final CustomQuadraticElement<Element> quadraticElement = new CustomQuadraticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                elementLeft, elementRight, pairing);
        final QuarticElement<Element> element = new QuarticElement<Element>(quadraticElement);
        assertEquals(element1, element.getW());
        assertEquals(element2, element.getX());
        assertEquals(element3, element.getY());
        assertEquals(element4, element.getZ());
    }

    @Test
    public void constructQuarticElement_fromQuartic() {
        final Pairing pairing = createPairing();
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        final QuarticElement<Element> element = new QuarticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, element3, element4);
        final QuarticElement qElement2 = new QuarticElement(element);
        assertEquals(element1, qElement2.getW());
        assertEquals(element2, qElement2.getX());
        assertEquals(element3, qElement2.getY());
        assertEquals(element4, qElement2.getZ());
    }

    @Test
    public void testAdd() throws Exception {
        final Pairing pairing = createPairing();
        final QuarticElement quarticElement = createQuarticElement(pairing);
        final QuarticElement quarticElement2 = createQuarticElement(pairing);

        final QuarticElement add = quarticElement.add(quarticElement2);
        assertEquals(quarticElement.getW().add(quarticElement2.getW()), add.getW());
        assertEquals(quarticElement.getX().add(quarticElement2.getX()), add.getX());
        assertEquals(quarticElement.getY().add(quarticElement2.getY()), add.getY());
        assertEquals(quarticElement.getZ().add(quarticElement2.getZ()), add.getZ());
    }

    @Test
    public void testDuplicate() {
        final Pairing pairing = createPairing();
        final QuarticElement quarticElement = createQuarticElement(pairing);
        final QuarticElement duplicate = quarticElement.duplicate();
        assertEquals(quarticElement.getW(), duplicate.getW());
        assertEquals(quarticElement.getX(), duplicate.getX());
        assertEquals(quarticElement.getY(), duplicate.getY());
        assertEquals(quarticElement.getZ(), duplicate.getZ());
    }

    @Test
    public void testIsEqual() {
        final Pairing pairing = createPairing();
        final QuarticElement quarticElement = createQuarticElement(pairing);
        final QuarticElement quarticElement2 = createQuarticElement(pairing);
        final QuarticElement duplicate = quarticElement.duplicate();
        assertNotEquals(quarticElement, quarticElement2);
        assertNotEquals(quarticElement, new QuadraticElement(
                new QuadraticField(new SecureRandom(), pairing.getG1())));
        assertEquals(quarticElement, quarticElement);
        assertEquals(quarticElement, duplicate);
    }

    private QuarticElement createQuarticElement(final Pairing pairing) {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        return new QuarticElement<Element>(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g1),
                element1, element2, element3, element4);
    }
}
