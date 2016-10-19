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
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

public class QuarticElement<E extends Element> extends ImmutableQuadraticElement<Element> {
    private Element w;
    private Element z;

    public QuarticElement(final QuadraticField field, final E w, final E x, final E y, final E z) {
        super(new QuadraticElement<>(field));
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public QuarticElement(final QuadraticElement element) {
        super(element);
        this.w = ((QuadraticElement) element.getX()).getX();
        this.x = ((QuadraticElement) element.getX()).getY();
        this.y = ((QuadraticElement) element.getY()).getX();
        this.z = ((QuadraticElement) element.getY()).getY();
    }

    public QuarticElement(final QuarticElement element) {
        super(element);
        this.w = element.getW();
        this.x = element.getX();
        this.y = element.getY();
        this.z = element.getZ();
    }

    public QuarticElement(final QuadraticField field) {
        super(new QuadraticElement<>(field));
    }

    public Element getW() {
        return w;
    }

    public Element getZ() {
        return z;
    }

    public QuarticElement add(final Element e) {
        final QuarticElement result = this.duplicate();
        QuarticElement element = (QuarticElement) e;
        result.w = result.w.add(element.w);
        result.x = result.x.add(element.x);
        result.y = result.y.add(element.y);
        result.z = result.z.add(element.z);
        return result;
    }

    public QuarticElement duplicate() {
        return new QuarticElement(this);
    }

    public boolean isEqual(Element e) {
        if (e == this) {
            return true;
        } else if (!(e instanceof QuarticElement)) {
            return false;
        } else {
            QuarticElement element = (QuarticElement) e;
            return this.w.isEqual(element.w) && this.x.isEqual(element.x)
                    && this.y.isEqual(element.y) && this.z.isEqual(element.z);
        }
    }
}
