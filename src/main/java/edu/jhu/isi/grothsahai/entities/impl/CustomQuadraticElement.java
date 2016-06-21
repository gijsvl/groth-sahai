package edu.jhu.isi.grothsahai.entities.impl;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.ImmutableQuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

import java.security.SecureRandom;

public class CustomQuadraticElement<E extends Element> extends ImmutableQuadraticElement<Element> {
    private Pairing pairing = null;

    public CustomQuadraticElement(final QuadraticField field, final E x, final E y, final Pairing pairing) {
        super(new QuadraticElement<Element>(field));
        this.x = x;
        this.y = y;
        this.pairing = pairing;
    }

    public CustomQuadraticElement(final CustomQuadraticElement<E> eCustomQuadraticElement) {
        super(eCustomQuadraticElement);
    }

    public CustomQuadraticElement(final QuadraticElement eQuadraticElement, final Pairing pairing) {
        super(eQuadraticElement);
        this.pairing = pairing;
    }

    public QuarticElement pair(final CustomQuadraticElement element) {
        return new QuarticElement<Element>(new QuadraticField(new SecureRandom(), pairing.getGT()),
                pairing.pairing(this.getX(), element.getX()),
                pairing.pairing(this.getX(), element.getY()),
                pairing.pairing(this.getY(), element.getX()),
                pairing.pairing(this.getY(), element.getY()));
    }

    public CustomQuadraticElement mulZn(Element e) {
        return new CustomQuadraticElement(super.duplicate().mulZn(e), pairing);
    }

    public Element getImmutable() {
        return new CustomQuadraticElement<E>(this);
    }

    public QuadraticElement duplicate() {
        return new QuadraticElement<E>(this);
    }
}