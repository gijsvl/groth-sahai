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

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.Stream;

public class Vector {
    private Element[] elements;

    public Vector(final Element[] elements) {
        this.elements = elements;
    }

    public Vector(final Vector... vectors) {
        elements = Stream.of(vectors).map(Vector::getElements).flatMap(Stream::of).toArray(Element[]::new);
    }

    public Vector() {
    }

    public static Vector getQuadraticZeroVector(final Field field, final Pairing pairing, final int size) {
        final Element[] elements = new Element[size];
        for (int i = 0; i < size; i++) {
            elements[i] = new CustomQuadraticElement((QuadraticElement) field.newZeroElement(), pairing);
        }
        return new Vector(elements);
    }

    public static Vector getZeroVector(final int length, final Field field) {
        final Element zeroElement = field.newZeroElement().getImmutable();
        final Element[] elements = new Element[length];
        Arrays.fill(elements, zeroElement);
        return new Vector(elements);
    }

    Element[] getElements() {
        return elements;
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
