package edu.jhu.isi.grothsahai.entities.impl;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;

public class Vector {
    private Element[] elements;

    public Vector(final Element[] elements) {
        this.elements = elements;
    }

    public Element get(final int i) {
        return elements[i];
    }

    public Vector add(final Vector v) {
        Element[] resultElements = new Element[elements.length];
        for (int i = 0; i < elements.length; i++) {
            resultElements[i] = elements[i].add(v.get(i));
        }
        return new Vector(resultElements);
    }

    public int getLength() {
        return elements.length;
    }

    public Vector sub(final Vector v) {
        Element[] resultElements = new Element[elements.length];
        for (int i = 0; i < elements.length; i++) {
            resultElements[i] = elements[i].sub(v.get(i));
        }
        return new Vector(resultElements);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Vector vector = (Vector) o;
        return Arrays.equals(elements, vector.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }
}
