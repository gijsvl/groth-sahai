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
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

import java.security.SecureRandom;

public class CustomQuadraticElement<E extends Element> extends ImmutableQuadraticElement<Element> {
    private Pairing pairing = null;

    public CustomQuadraticElement(final QuadraticField field, final E x, final E y, final Pairing pairing) {
        super(new QuadraticElement<>(field));
        this.x = x;
        this.y = y;
        this.pairing = pairing;
    }

    public CustomQuadraticElement(final CustomQuadraticElement<E> eCustomQuadraticElement) {
        super(eCustomQuadraticElement);
    }

    public CustomQuadraticElement(final QuadraticElement quadraticElement, final Pairing pairing) {
        super(quadraticElement);
        this.pairing = pairing;
    }

    public QuarticElement pair(final CustomQuadraticElement element) {
        return new QuarticElement<>(new QuadraticField(new SecureRandom(), pairing.getGT()),
                pairing.pairing(this.getX(), element.getX()),
                pairing.pairing(this.getX(), element.getY()),
                pairing.pairing(this.getY(), element.getX()),
                pairing.pairing(this.getY(), element.getY()));
    }

    public CustomQuadraticElement mulZn(Element e) {
        return new CustomQuadraticElement(super.duplicate().mulZn(e), pairing);
    }

    public Element getImmutable() {
        return new CustomQuadraticElement<>(this);
    }

    public QuadraticElement duplicate() {
        return new QuadraticElement<E>(this);
    }
}
