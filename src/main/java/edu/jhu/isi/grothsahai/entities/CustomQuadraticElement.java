package edu.jhu.isi.grothsahai.entities;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;

import java.security.SecureRandom;

public class CustomQuadraticElement<E extends Element> extends QuadraticElement<Element> {
    private Pairing pairing = null;

    public CustomQuadraticElement(final QuadraticField field, final E x, final E y, final Pairing pairing) {
        super(field);
        this.x = x;
        this.y = y;
        this.pairing = pairing;
    }

    public QuarticElement pair(final CustomQuadraticElement element) {
        return new QuarticElement<Element>(new QuadraticField(new SecureRandom(), pairing.getGT()),
                pairing.pairing(this.getX(), element.getX()),
                pairing.pairing(this.getX(), element.getY()),
                pairing.pairing(this.getY(), element.getX()),
                pairing.pairing(this.getY(), element.getY()));
    }
}
