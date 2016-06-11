package edu.jhu.isi.grothsahai.entities.impl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.util.Arrays;

public class Matrix {
    private Element[][] elements;

    public Matrix(final Element[][] elements) {
        this.elements = elements;
    }

    public static Matrix random(final Field field, final int m, final int n) {
        Element[][] elements = new Element[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                elements[i][j] = field.newRandomElement();
        return new Matrix(elements);
    }

    public void set(final int rowIndex, final int colIndex, final Element element) {
        elements[rowIndex][colIndex] = element;
    }

    public Element get(final int rowIndex, final int colIndex) {
        return elements[rowIndex][colIndex];
    }

    public int getNumberOfCols() {
        return elements[0].length;
    }

    public int getNumberOfRows() {
        return elements.length;
    }

    public Matrix getTranspose() {
        int m = elements.length;
        int n = elements[0].length;
        Element[][] elementsT = elements;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                elementsT[j][i] = elements[i][j];
            }
        }
        return new Matrix(elements);
    }

    public Matrix multiply(final Matrix B) {
        final int mA = elements.length;
        final int nA = elements[0].length;
        final int mB = B.getNumberOfRows();
        final int nB = B.getNumberOfCols();
        if (nA != mB) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        final Element[][] C = new Element[mA][nB];
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                for (int k = 0; k < nA; k++) {
                    C[i][j] = C[i][j].add(elements[i][k].mul(B.get(k, j)));
                }
            }
        }
        return new Matrix(C);
    }

    public Vector multiply(final Vector x) {
        final int m = elements.length;
        final int n = elements[0].length;
        if (x.getLength() != n) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        final Element[] y = new Element[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                y[i] = y[i].add(elements[i][j].mul(x.get(j)));
            }
        }
        return new Vector(y);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Matrix matrix = (Matrix) o;
        return Arrays.equals(elements, matrix.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }
}
