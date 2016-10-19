/*
 * Copyright (c) 2016 Gijs Van Laer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.BaseTest;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class QuarticElementTest extends BaseTest {
    private Pairing pairing;

    @Before
    public void setUp() throws Exception {
        final PairingParameters pairingParams = createPairingParams();
        pairing = PairingFactory.getPairing(pairingParams);
    }

    @Test
    public void constructQuarticElement() {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        final QuarticElement<Element> element = new QuarticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, element3, element4);
        assertEquals(element1, element.getW());
        assertEquals(element2, element.getX());
        assertEquals(element3, element.getY());
        assertEquals(element4, element.getZ());
    }

    @Test
    public void constructQuarticElementFromQuadraticElement() {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        final CustomQuadraticElement<Element> elementLeft = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element3, element4, pairing);
        final CustomQuadraticElement<Element> quadraticElement = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                elementLeft, elementRight, pairing);
        final QuarticElement<Element> element = new QuarticElement<>(quadraticElement);
        assertEquals(element1, element.getW());
        assertEquals(element2, element.getX());
        assertEquals(element3, element.getY());
        assertEquals(element4, element.getZ());
    }

    @Test
    public void constructQuarticElement_fromQuartic() {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Element element3 = g1.newRandomElement();
        final Element element4 = g1.newRandomElement();
        final QuarticElement<Element> element = new QuarticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, element3, element4);
        final QuarticElement qElement2 = new QuarticElement(element);
        assertEquals(element1, qElement2.getW());
        assertEquals(element2, qElement2.getX());
        assertEquals(element3, qElement2.getY());
        assertEquals(element4, qElement2.getZ());
    }

    @Test
    public void testAdd() throws Exception {
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
        final QuarticElement quarticElement = createQuarticElement(pairing);
        final QuarticElement duplicate = quarticElement.duplicate();
        assertEquals(quarticElement.getW(), duplicate.getW());
        assertEquals(quarticElement.getX(), duplicate.getX());
        assertEquals(quarticElement.getY(), duplicate.getY());
        assertEquals(quarticElement.getZ(), duplicate.getZ());
    }

    @Test
    public void testIsEqual() {
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
        return new QuarticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, element3, element4);
    }
}
