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

public class CustomQuadraticElementTest extends BaseTest {
    private Pairing pairing;

    @Before
    public void setUp() throws Exception {
        final PairingParameters pairingParams = createPairingParams();
        pairing = PairingFactory.getPairing(pairingParams);
    }

    @Test
    public void constructQuadraticElement() {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final CustomQuadraticElement<Element> element = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);
        assertEquals(element1, element.getX());
        assertEquals(element2, element.getY());
    }

    @Test
    public void testPair() {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final Field g2 = pairing.getG2();
        final Element element3 = g2.newRandomElement();
        final Element element4 = g2.newRandomElement();
        final CustomQuadraticElement<Element> elementLeft = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g2),
                element3, element4, pairing);
        assertEquals(elementLeft.pair(elementRight), new QuarticElement<>(new QuadraticField<>(new SecureRandom(), pairing.getGT()),
                pairing.pairing(elementLeft.getX(), elementRight.getX()),
                pairing.pairing(elementLeft.getX(), elementRight.getY()),
                pairing.pairing(elementLeft.getY(), elementRight.getX()),
                pairing.pairing(elementLeft.getY(), elementRight.getY())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPair_illegalArgument() {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final CustomQuadraticElement<Element> elementLeft = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);
        final CustomQuadraticElement<Element> elementRight = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);
        elementLeft.pair(elementRight);
    }

    @Test
    public void testMulZn() throws Exception {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final CustomQuadraticElement<Element> element = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);

        final Element zrElement = pairing.getZr().newRandomElement();
        final CustomQuadraticElement mulZn = element.mulZn(zrElement);

        assertEquals(element1.mulZn(zrElement), mulZn.getX());
        assertEquals(element2.mulZn(zrElement), mulZn.getY());
    }

    @Test
    public void testDuplicate() throws Exception {
        final Field g1 = pairing.getG1();
        final Element element1 = g1.newRandomElement();
        final Element element2 = g1.newRandomElement();
        final CustomQuadraticElement<Element> element = new CustomQuadraticElement<>(
                new QuadraticField<>(new SecureRandom(), g1),
                element1, element2, pairing);

        final QuadraticElement duplicate = element.duplicate();

        assertEquals(element1, duplicate.getX());
        assertEquals(element2, duplicate.getY());
    }
}
