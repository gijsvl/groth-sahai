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
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VectorTest extends BaseTest {
    private Pairing pairing;

    @Before
    public void setUp() throws Exception {
        final PairingParameters pairingParams = createPairingParams();
        pairing = PairingFactory.getPairing(pairingParams);
    }

    @Test
    public void testGetNullVector() {
        final Vector vector = Vector.getQuadraticZeroVector(
                new QuadraticField<>(new SecureRandom(), pairing.getG1()),
                pairing, 3);

        assertEquals(3, vector.getLength());
        for (int i = 0; i < vector.getLength(); i++) {
            assertTrue(vector.get(i).isZero());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_illegalDimensions() {
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG1());

        vector.add(vector2);
    }

    @Test
    public void testAdd() {
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(3, pairing.getG1());

        final Vector add = vector.add(vector2);
        for (int i = 0; i < add.getLength(); i++) {
            assertEquals(pairing.getG1(), add.get(i).getField());
            assertEquals(vector.get(i).add(vector2.get(i)), add.get(i));
        }
    }

    @Test
    public void testGetLength() {
        final Vector vector = generateVector(3, pairing.getG1());

        assertEquals(3, vector.getLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSub_illegalDimensions() {
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG1());

        vector.sub(vector2);
    }

    @Test
    public void testSub() {
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(3, pairing.getG1());

        final Vector sub = vector.sub(vector2);
        for (int i = 0; i < sub.getLength(); i++) {
            assertEquals(pairing.getG1(), sub.get(i).getField());
            assertEquals(vector.get(i).sub(vector2.get(i)), sub.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPair_illegalDimensions() {
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG2());

        vector.pair(vector2, pairing);
    }

    @Test
    public void testPair() {
        final Vector vector = generateVector(2, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG2());

        final Element pair = vector.pair(vector2, pairing);
        assertEquals(pairing.getGT(), pair.getField());
        assertEquals(pairing.pairing(vector.get(0), vector2.get(0))
                .add(pairing.pairing(vector.get(1), vector2.get(1))), pair);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPairInB_illegalDimensions() {
        final Vector vector = generateVector(3, new QuadraticField(new SecureRandom(), pairing.getG1()));
        final Vector vector2 = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG2()));

        vector.pairInB(vector2, pairing);
    }

    @Test
    public void testPairInB() {
        final Vector vector = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG1()));
        final Vector vector2 = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG2()));

        final Element pair = vector.pairInB(vector2, pairing);
        assertEquals(pairing.getGT(), ((QuadraticField) pair.getField()).getTargetField());
        assertEquals(new CustomQuadraticElement((QuadraticElement) vector.get(0), pairing)
                .pair(new CustomQuadraticElement((QuadraticElement) vector2.get(0), pairing))
                .add(new CustomQuadraticElement((QuadraticElement) vector.get(1), pairing)
                        .pair(new CustomQuadraticElement((QuadraticElement) vector2.get(1), pairing))), pair);
    }

    @Test
    public void testConcatVectors() throws Exception {
        final Vector vector = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG1()));
        final Vector vector2 = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG1()));

        final Vector newVector = new Vector(vector, vector2);
        assertTrue(Arrays.asList(newVector.getElements()).containsAll(Arrays.asList(vector.getElements())));
        assertTrue(Arrays.asList(newVector.getElements()).containsAll(Arrays.asList(vector2.getElements())));
    }

    @Test
    public void testGetZeroVector() throws Exception {
        final int length = 3;
        final Vector zeroVector = Vector.getZeroVector(length, pairing.getG1());

        assertEquals(length, zeroVector.getLength());
        for (final Element element : zeroVector.getElements()) {
            assertTrue(element.isZero());
        }
    }
}
