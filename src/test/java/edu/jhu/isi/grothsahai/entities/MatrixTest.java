package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.BaseTest;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MatrixTest extends BaseTest {
    private Pairing pairing;

    @Before
    public void setUp() throws Exception {
        final PairingParameters pairingParams = createPairingParams();
        pairing = PairingFactory.getPairing(pairingParams);
    }

    @Test
    public void testRandom() {
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                assertEquals(pairing.getG1(), random.get(i, j).getField());
    }

    @Test
    public void testRandom_noRows() {
        final int cols = 3;
        final int rows = 0;

        assertNull(Matrix.random(pairing.getG1(), rows, cols));
    }

    @Test
    public void testRandom_noCols() {
        final int cols = 0;
        final int rows = 2;

        assertNull(Matrix.random(pairing.getG1(), rows, cols));
    }

    @Test
    public void testGetNumberOfCols() {
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);

        assertEquals(cols, random.getNumberOfCols());
    }

    @Test
    public void testGetNumberOfRows() {
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);

        assertEquals(rows, random.getNumberOfRows());
    }

    @Test
    public void testTranspose() {
        final int cols = 3;
        final int rows = 2;
        final Matrix random = Matrix.random(pairing.getG1(), rows, cols);
        final Matrix transpose = random.getTranspose();

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                assertEquals(random.get(i, j), transpose.get(j, i));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_wrongDimensions() {
        final int cols = 3;
        final int rows = 2;
        final Matrix m1 = Matrix.random(pairing.getG1(), rows, cols);
        final Matrix m2 = Matrix.random(pairing.getG1(), rows, cols);

        m1.multiply(m2);
    }

    @Test
    public void testMultiply() {
        final int cols = 4;
        final int rows = 2;
        final Matrix m1 = Matrix.random(pairing.getG1(), rows, 3);
        final Matrix m2 = Matrix.random(pairing.getG1(), 3, cols);

        final Matrix multiply = m1.multiply(m2);
        assertEquals(rows, multiply.getNumberOfRows());
        assertEquals(cols, multiply.getNumberOfCols());
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                assertEquals(pairing.getG1(), multiply.get(i, j).getField());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyVector_wrongDimensions() {
        final int cols = 3;
        final int rows = 2;
        final Matrix m = Matrix.random(pairing.getZr(), rows, cols);
        final Vector v = generateVector(rows, pairing.getG1());

        m.multiply(v);
    }

    @Test
    public void testMultiplyVector() {
        final int cols = 4;
        final int rows = 2;
        final Matrix m = Matrix.random(pairing.getZr(), rows, cols);
        final Vector v = generateVector(cols, pairing.getG1());

        final Vector multiply = m.multiply(v);
        assertEquals(rows, multiply.getLength());
        for (int i = 0; i < rows; i++)
            assertEquals(pairing.getG1(), multiply.get(i).getField());
    }
}
