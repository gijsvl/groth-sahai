package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeDCurveGenerator;
import org.apache.commons.lang3.NotImplementedException;

import java.security.SecureRandom;

public class CommonReferenceStringImpl implements CommonReferenceString {
    private final ZrField zr;
    private final QuadraticField[] b;
    private final Field[] g;
    private final CustomQuadraticElement[] w = new CustomQuadraticElement[2];
    private final QuadraticElement[][] u = new QuadraticElement[2][2];
    private final Pairing pairing;

    public CommonReferenceStringImpl(final QuadraticElement u11, final QuadraticElement u12,
                                     final QuadraticElement u21, final QuadraticElement u22,
                                     final Field zr, final Field[] g, final QuadraticField[] b,
                                     final Pairing pairing) {
        this.u[0][0] = u11;
        this.u[0][1] = u12;
        this.u[1][0] = u21;
        this.u[1][1] = u22;
        this.zr = (ZrField) zr;
        this.g = g;
        this.b = b;
        this.pairing = pairing;
        w[0] = new CustomQuadraticElement<Element>(b[1], g[1].newZeroElement().getImmutable(), u[0][1].getX(), pairing);
        w[1] = new CustomQuadraticElement<Element>(b[2], g[2].newZeroElement().getImmutable(), u[1][1].getX(), pairing);
    }

    public static CommonReferenceStringImpl generate() {
        // TODO: make pairing flexible (and include as parameter)
        final int discriminant = 9563;
        final PairingParametersGenerator parametersGenerator = new PBCTypeDCurveGenerator(discriminant);
        final PairingParameters params = parametersGenerator.generate();
        final Pairing pairing = PairingFactory.getPairing(params);

        final Field zr = pairing.getZr();
        final Field[] g = new Field[3];
        g[0] = pairing.getGT();
        g[1] = pairing.getG1();
        g[2] = pairing.getG2();
        final Element p1 = newNonZeroNonOneElement(g[1]);
        final Element a1 = newNonZeroNonOneElement(zr);
        final Element t1 = newNonZeroNonOneElement(zr);
        final Element q1 = p1.mulZn(a1).getImmutable();
        final Element u1 = p1.mulZn(t1).getImmutable();
        final Element v1 = q1.mulZn(t1).getImmutable();
        final Element p2 = newNonZeroNonOneElement(g[2]);
        final Element a2 = newNonZeroNonOneElement(zr);
        final Element t2 = newNonZeroNonOneElement(zr);
        final Element q2 = p2.mulZn(a2).getImmutable();
        final Element u2 = p2.mulZn(t2).getImmutable();
        final Element v2 = q2.mulZn(t2).getImmutable();

        final QuadraticField[] b = new QuadraticField[3];
        b[1] = new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g[1]);
        b[2] = new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g[2]);
        final QuadraticField partBt = new QuadraticField<Field, QuadraticElement>(new SecureRandom(), g[0]);
        b[0] = new QuadraticField<QuadraticField, QuadraticElement>(new SecureRandom(), partBt);
        final CustomQuadraticElement<Element> bigU11 = new CustomQuadraticElement<Element>(b[1], p1, q1, pairing);
        final CustomQuadraticElement<Element> bigU12 = new CustomQuadraticElement<Element>(b[1], u1, v1, pairing);
        final CustomQuadraticElement<Element> bigU21 = new CustomQuadraticElement<Element>(b[2], p2, q2, pairing);
        final CustomQuadraticElement<Element> bigU22 = new CustomQuadraticElement<Element>(b[2], u2, v2, pairing);

        return new CommonReferenceStringImpl(bigU11, bigU12, bigU21, bigU22, zr, g, b, pairing);
    }

    public Vector iota(final int index, final Vector xs) {
        final QuadraticElement[] result = new QuadraticElement[xs.getLength()];
        for (int i = 0; i < xs.getLength(); i++) {
            result[i] = iota(index, xs.get(i));
        }

        return new Vector(result);
    }

    QuadraticElement iota(final int index, final Element x) {
        if (index != 1 && index != 2) {
            throw new IllegalArgumentException("Index must be 1 or 2.");
        }
//        final int indexMin1 = index - 1;
//        if (g[index].equals(zr)) {
//            //TODO: test this branch
//            return u[indexMin1][1].sub(w[indexMin1]).mulZn(x);
//        }
        return new CustomQuadraticElement<Element>(b[index], g[index].newZeroElement(), x, pairing);
    }

    public QuarticElement iotaT(final ProblemType type, final Element x) {
        // TODO: child classes for the different types
        switch (type) {
            case PAIRING_PRODUCT:
                return new QuarticElement<Element>((new QuadraticField(new SecureRandom(), g[0])), g[0].newOneElement(),
                        g[0].newOneElement(), g[0].newOneElement(), x);
//            case MULTI_SCALAR_G1:
//                return new QuarticElement<Element>((new QuadraticField(new SecureRandom(), g[0])), g[0].newOneElement(),
//                        g[0].newOneElement(), ((CustomQuadraticElement) x).pair((CustomQuadraticElement) w[1].getX()),
//                        ((CustomQuadraticElement) x).pair((CustomQuadraticElement) w[1].getY()));
//            case MULTI_SCALAR_G2:
//                return new QuarticElement<Element>((new QuadraticField(new SecureRandom(), g[0])), g[0].newOneElement(),
//                        ((CustomQuadraticElement) w[0].getX()).pair((CustomQuadraticElement) x), g[0].newOneElement(),
//                        ((CustomQuadraticElement) w[0].getY()).pair((CustomQuadraticElement) x));
//            case QUADRATIC:
//                return new QuarticElement(w[0].pair(w[1]).powZn(x));
        }
        throw new NotImplementedException("ProblemType not covered: " + type);
    }

    QuadraticElement getU11() {
        return u[0][0];
    }

    QuadraticElement getU12() {
        return u[0][1];
    }

    public Vector getU1() {
        return new Vector(u[0]);
    }

    QuadraticElement getU21() {
        return u[1][0];
    }

    QuadraticElement getU22() {
        return u[1][1];
    }

    public Vector getU2() {
        return new Vector(u[1]);
    }

    public ZrField getZr() {
        return zr;
    }

    QuadraticField getB1() {
        return b[1];
    }

    QuadraticField getB2() {
        return b[2];
    }

    QuadraticField getBT() {
        return b[0];
    }

    public Field getG1() {
        return g[1];
    }

    public Field getG2() {
        return g[2];
    }

    Field getGT() {
        return g[0];
    }

    public Pairing getPairing() {
        return pairing;
    }

    private static Element newNonZeroNonOneElement(Field field) {
        Element returnElement;
        do {
            returnElement = field.newRandomElement();
        } while (returnElement.isZero() || returnElement.isOne());
        return returnElement.getImmutable();
    }
}
