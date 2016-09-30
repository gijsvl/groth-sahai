package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.entities.CustomQuadraticElement;
import edu.jhu.isi.grothsahai.entities.Vector;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VectorTest extends BaseTest {
    @Test
    public void testGetNullVector() {
        final Pairing pairing = createPairing();
        final Vector vector = Vector.getQuadraticNullVector(
                new QuadraticField<Field, QuadraticElement>(new SecureRandom(), pairing.getG1()),
                pairing, 3);

        assertEquals(3, vector.getLength());
        for (int i = 0; i < vector.getLength(); i++) {
            assertTrue(vector.get(i).isZero());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd_illegalDimensions() {
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG1());

        vector.add(vector2);
    }

    @Test
    public void testAdd() {
        final Pairing pairing = createPairing();
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
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(3, pairing.getG1());

        assertEquals(3, vector.getLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSub_illegalDimensions() {
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG1());

        vector.sub(vector2);
    }

    @Test
    public void testSub() {
        final Pairing pairing = createPairing();
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
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(3, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG2());

        vector.pair(vector2, pairing);
    }

    @Test
    public void testPair() {
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(2, pairing.getG1());
        final Vector vector2 = generateVector(2, pairing.getG2());

        final Element pair = vector.pair(vector2, pairing);
        assertEquals(pairing.getGT(), pair.getField());
        assertEquals(pairing.pairing(vector.get(0), vector2.get(0))
                .add(pairing.pairing(vector.get(1), vector2.get(1))), pair);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPairInB_illegalDimensions() {
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(3, new QuadraticField(new SecureRandom(), pairing.getG1()));
        final Vector vector2 = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG2()));

        vector.pairInB(vector2, pairing);
    }

    @Test
    public void testPairInB() {
        final Pairing pairing = createPairing();
        final Vector vector = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG1()));
        final Vector vector2 = generateVector(2, new QuadraticField(new SecureRandom(), pairing.getG2()));

        final Element pair = vector.pairInB(vector2, pairing);
        assertEquals(pairing.getGT(), ((QuadraticField) pair.getField()).getTargetField());
        assertEquals(new CustomQuadraticElement((QuadraticElement) vector.get(0), pairing)
                .pair(new CustomQuadraticElement((QuadraticElement) vector2.get(0), pairing))
                .add(new CustomQuadraticElement((QuadraticElement) vector.get(1), pairing)
                        .pair(new CustomQuadraticElement((QuadraticElement) vector2.get(1), pairing))), pair);
    }
}
