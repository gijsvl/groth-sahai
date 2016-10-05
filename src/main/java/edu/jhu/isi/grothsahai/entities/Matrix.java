package edu.jhu.isi.grothsahai.entities;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.util.Arrays;

public class Matrix {
    private Element[][] elements;

    public Matrix(final Element[][] elements) {
        this.elements = elements;
    }

    public static Matrix random(final Field field, final int m, final int n) {
        if (m == 0 || n == 0) {
            return null;
        }
        Element[][] elements = new Element[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                elements[i][j] = field.newRandomElement().getImmutable();
        return new Matrix(elements);
    }

    public static Matrix zero(final Field field, final int m, final int n) {
        if (m == 0 || n == 0) {
            return null;
        }
        Element[][] elements = new Element[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                elements[i][j] = field.newZeroElement().getImmutable();
        return new Matrix(elements);
    }

    public Element get(final int rowIndex, final int colIndex) {
        return elements[rowIndex][colIndex];
    }

    public void set(final int rowIndex, final int colIndex, final Element element) {
        elements[rowIndex][colIndex] = element;
    }

    public int getNumberOfCols() {
        return getNumberOfRows() != 0 ? elements[0].length : 0;
    }

    public int getNumberOfRows() {
        return elements.length;
    }

    public Matrix getTranspose() {
        int m = getNumberOfRows();
        int n = getNumberOfCols();
        Element[][] elementsT = new Element[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                elementsT[j][i] = elements[i][j].getImmutable();
            }
        }
        return new Matrix(elementsT);
    }

    public Matrix multiply(final Matrix B) {
        final int mA = getNumberOfRows();
        final int nA = getNumberOfCols();
        final int mB = B.getNumberOfRows();
        final int nB = B.getNumberOfCols();
        if (nA != mB) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }
        final Element[][] C = new Element[mA][nB];
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                for (int k = 0; k < nA; k++) {
                    if (C[i][j] == null) {
                        C[i][j] = elements[0][0].getField().newZeroElement().getImmutable();
                    }
                    C[i][j] = C[i][j].add(elements[i][k].mul(B.get(k, j)));
                }
            }
        }
        return new Matrix(C);
    }

    public Vector multiply(final Vector v) {
        final int m = getNumberOfRows();
        final int n = getNumberOfCols();
        if (v.getLength() != n) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }
        final Element[] y = new Element[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (y[i] == null) {
                    y[i] = v.get(0).getField().newZeroElement().getImmutable();
                }
                y[i] = y[i].add(v.get(j).mulZn(elements[i][j]));
            }
        }
        return new Vector(y);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Matrix matrix = (Matrix) o;
        return Arrays.deepEquals(elements, matrix.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(elements);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "elements=" + Arrays.deepToString(elements) +
                '}';
    }
}
