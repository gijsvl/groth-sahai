package edu.jhu.isi.grothsahai.entities.impl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

public class QuarticElement<E extends Element> extends ImmutableQuadraticElement<Element> {
    private Element w;
    private Element z;

    public QuarticElement(final QuadraticField field, final E w, final E x, final E y, final E z) {
        super(new QuadraticElement<Element>(field));
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

    public Element getW() {
        return w;
    }

    public Element getZ() {
        return z;
    }

    public QuarticElement add(final Element e) {
        final QuarticElement result = this.duplicate();
        QuarticElement element = (QuarticElement) e;
        result.w.add(element.w);
        result.x.add(element.x);
        result.y.add(element.y);
        result.z.add(element.z);
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
