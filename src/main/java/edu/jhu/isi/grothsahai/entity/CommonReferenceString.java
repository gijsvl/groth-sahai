package edu.jhu.isi.grothsahai.entity;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrElement;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeDCurveGenerator;

import java.security.SecureRandom;

public class CommonReferenceString {
    private final ZrField zr;
    private final QuadraticField[] b;
    private final Field[] g;
    private final QuadraticElement[][] u = new QuadraticElement[2][2];

    public CommonReferenceString(final QuadraticElement u11, final QuadraticElement u12, final QuadraticElement u21, final QuadraticElement u22, final Field zr, final Field[] g, final QuadraticField[] b) {
        this.u[0][0] = u11;
        this.u[0][1] = u12;
        this.u[1][0] = u21;
        this.u[1][1] = u22;
        this.zr = (ZrField) zr;
        this.g = g;
        this.b = b;
    }

    public static CommonReferenceString generate() {
        final int discriminant = 9563;
        final PairingParametersGenerator parametersGenerator = new PBCTypeDCurveGenerator(discriminant);
        final PairingParameters params = parametersGenerator.generate();
        final Pairing pairing = PairingFactory.getPairing(params);

        final Field zr = pairing.getZr();
        final Field[] g = new Field[2];
        g[0] = pairing.getG1();
        g[1] = pairing.getG2();
        final Element p1 = newNonZeroNonOneElement(g[0]);
        final Element a1 = newNonZeroNonOneElement(zr);
        final Element t1 = newNonZeroNonOneElement(zr);
        final Element q1 = p1.mulZn(a1).getImmutable();
        final Element u1 = p1.mulZn(t1).getImmutable();
        final Element v1 = q1.mulZn(t1).getImmutable();
        final Element p2 = newNonZeroNonOneElement(g[1]);
        final Element a2 = newNonZeroNonOneElement(zr);
        final Element t2 = newNonZeroNonOneElement(zr);
        final Element q2 = p2.mulZn(a2).getImmutable();
        final Element u2 = p2.mulZn(t2).getImmutable();
        final Element v2 = q2.mulZn(t2).getImmutable();

        final QuadraticField[] b = new QuadraticField[2];
        b[0] = new QuadraticField(new SecureRandom(), g[0]);
        b[1] = new QuadraticField(new SecureRandom(), g[1]);
        final CustomQuadraticElement<Element> bigU11 = new CustomQuadraticElement<Element>(b[0], p1, q1);
        final CustomQuadraticElement<Element> bigU12 = new CustomQuadraticElement<Element>(b[0], u1, v1);
        final CustomQuadraticElement<Element> bigU21 = new CustomQuadraticElement<Element>(b[1], p2, q2);
        final CustomQuadraticElement<Element> bigU22 = new CustomQuadraticElement<Element>(b[1], u2, v2);

        return new CommonReferenceString(bigU11, bigU12, bigU21, bigU22, zr, g, b);
    }

    public QuadraticElement iota(final int index, final Element x) {
        if (index != 0 && index != 1) {
            throw new IllegalArgumentException("Index must be 0 or 1.");
        }
        if (g[index].equals(zr)) {
            return u[index][1].sub(new CustomQuadraticElement<Element>(b[index], g[index].newZeroElement(), u[index][0].getX())).mulZn(x);
        }
        return new CustomQuadraticElement<Element>(b[index], g[index].newZeroElement(), x);
    }

//    public QuadraticElement commit(final int index, final ZrElement x, final ZrElement randomness) {
//        return iota(index, x).add(u[index][0].mulZn(randomness));
//    }

    public QuadraticElement commit(final int index, final Element x, final ZrElement randomness1, final ZrElement randomness2) {
        return iota(index, x).add(u[index][0].mulZn(randomness1)).add(u[index][1].mulZn(randomness2));
    }

    QuadraticElement getU11() {
        return u[0][0];
    }

    QuadraticElement getU12() {
        return u[0][1];
    }

    QuadraticElement getU21() {
        return u[1][0];
    }

    QuadraticElement getU22() {
        return u[1][1];
    }

    ZrField getZr() {
        return zr;
    }

    QuadraticField getB1() {
        return b[0];
    }

    QuadraticField getB2() {
        return b[1];
    }

    Field getG1() {
        return g[0];
    }

    Field getG2() {
        return g[1];
    }

    private static Element newNonZeroNonOneElement(Field field) {
        Element returnElement;
        do {
            returnElement = field.newRandomElement();
        } while (returnElement.isZero() || returnElement.isOne());
        return returnElement.getImmutable();
    }
}