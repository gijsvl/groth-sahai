package edu.jhu.isi.grothsahai.entities.impl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

import java.security.SecureRandom;
import java.util.Arrays;

public class Vector {
    private Element[] elements;

    public Vector(final Element[] elements) {
        this.elements = elements;
    }

    public Vector() {
    }

    public static Vector getQuadraticNullVector(final Field field, final Pairing pairing, final int size) {
        final Element[] elements = new Element[size];
        for (int i = 0; i < size; i++) {
            elements[i] = new CustomQuadraticElement((QuadraticElement) field.newZeroElement(), pairing);
        }
        return new Vector(elements);
    }

    public Element get(final int i) {
        return elements[i];
    }

    public Vector add(final Vector v) {
        if (elements.length != v.getLength()) {
            throw new IllegalArgumentException("Illegal vector dimensions.");
        }
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
        if (elements.length != v.getLength()) {
            throw new IllegalArgumentException("Illegal vector dimensions.");
        }
        Element[] resultElements = new Element[elements.length];
        for (int i = 0; i < elements.length; i++) {
            resultElements[i] = elements[i].sub(v.get(i)).getImmutable();
        }
        return new Vector(resultElements);
    }

    public Element pair(final Vector v, final Pairing pairing) {
        if (getLength() != 0 && v.getLength() != 0 && getLength() != v.getLength()) {
            throw new IllegalArgumentException("Illegal vector dimensions.");
        }
        Element result = pairing.getGT().newZeroElement();
        if (v.getLength() != 0) {
            for (int i = 0; i < getLength(); i++) {
                result = result.add(pairing.pairing(get(i), v.get(i)));
            }
        }
        return result;
    }

    public Element pairInB(final Vector v, final Pairing pairing) {
        if (getLength() != v.getLength()) {
            throw new IllegalArgumentException("Illegal vector dimensions.");
        }
        Element result = new QuarticElement(new QuadraticField(new SecureRandom(), pairing.getGT()),
                pairing.getGT().newZeroElement(), pairing.getGT().newZeroElement(),
                pairing.getGT().newZeroElement(), pairing.getGT().newZeroElement());
        for (int i = 0; i < getLength(); i++) {
            if (v.get(i).getClass().equals(ImmutableQuadraticElement.class)) {
                v.set(i, new CustomQuadraticElement((QuadraticElement) v.get(i), pairing));
            }
            if (get(i).getClass().equals(ImmutableQuadraticElement.class)) {
                set(i, new CustomQuadraticElement((QuadraticElement) get(i), pairing));
            }
            result = result.add(((CustomQuadraticElement) get(i)).pair((CustomQuadraticElement) v.get(i)));
        }
        return result.getImmutable();
    }

    public void set(final int i, final Element element) {
        elements[i] = element;
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
