package edu.jhu.isi.grothsahai.entity;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

public class CustomQuadraticElement<E extends Element> extends QuadraticElement {
    public CustomQuadraticElement(final QuadraticField field) {
        super(field);
    }

    public CustomQuadraticElement(final QuadraticElement element) {
        super(element);
    }

    public CustomQuadraticElement(final QuadraticField field, final E x, final E y) {
        super(field);
        this.x = x;
        this.y = y;
    }
}
