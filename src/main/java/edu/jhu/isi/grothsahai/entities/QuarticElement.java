package edu.jhu.isi.grothsahai.entities;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

public class QuarticElement<E extends Element> extends QuadraticElement<Element> {
    private final Element w;
    private final Element z;

    public QuarticElement(final QuadraticField field, final E w, final E x, final E y, final E z) {
        super(field);
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

    public Element getW() {
        return w;
    }

    public Element getZ() {
        return z;
    }
}
