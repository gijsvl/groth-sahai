package edu.jhu.isi.grothsahai.entities.impl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

public class Matrix {
    private Element[][] elements;

    public Matrix(final Element[][] elements) {
        this.elements = elements;
    }

    public static Matrix random(final Field field, final int m, final int n) {
        Element[][] elements = new Element[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                elements[i][j] = field.newRandomElement().getImmutable();
        return new Matrix(elements);
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
        Element[][] elementsT = new Element[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                elementsT[j][i] = elements[i][j].getImmutable();
            }
        }
        return new Matrix(elementsT);
    }

    public Matrix multiply(final Matrix B) {
        final int mA = elements.length;
        final int nA = elements[0].length;
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
        final int m = elements.length;
        final int n = elements[0].length;
        if (v.getLength() != n) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }
        final Element[] y = new Element[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (y[i] == null) {
                    y[i] = v.get(0).getField().newZeroElement();
                }
                y[i] = v.get(j).mulZn(elements[i][j]).add(y[i]);
            }
        }
        return new Vector(y);
    }
}
