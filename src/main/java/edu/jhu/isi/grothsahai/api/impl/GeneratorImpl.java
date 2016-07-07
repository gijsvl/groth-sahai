package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.Matrix;
import edu.jhu.isi.grothsahai.entities.impl.Pair;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.Vector;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeDCurveGenerator;

public class GeneratorImpl implements Generator {
    private Role role;

    public GeneratorImpl(final Role role) {
        this.role = role;
    }

    public Pairing generatePairing() {
        // TODO: make pairing flexible
        final int discriminant = 9563;
        final PairingParametersGenerator parametersGenerator = new PBCTypeDCurveGenerator(discriminant);
        final PairingParameters params = parametersGenerator.generate();
        return PairingFactory.getPairing(params);
    }

    public CommonReferenceString generateCRS(final Pairing pairing) {
        return CommonReferenceStringImpl.generate(pairing);
    }

    public Pair<Statement, Witness> generateStatementAndWitness(final Pairing pairing) {
        if (role == Role.VERIFIER) {
            throw new IllegalStateException("The verifier should not create a statement witness pair, " +
                    "but should receive the statement from the prover at generation time.");
        }
        final int bLength = (int) Math.round(Math.random() * 10);
        final int aLength = (int) Math.round(Math.random() * 10);
        Element[] aElements = new Element[aLength];
        Element[] yElements = new Element[aLength];
        for (int i = 0; i < aLength; i++) {
            aElements[i] = pairing.getG1().newRandomElement().getImmutable();
            yElements[i] = pairing.getG2().newRandomElement().getImmutable();
        }
        Element[] bElements = new Element[bLength];
        Element[] xElements = new Element[bLength];
        for (int i = 0; i < bLength; i++) {
            bElements[i] = pairing.getG2().newRandomElement().getImmutable();
            xElements[i] = pairing.getG1().newRandomElement().getImmutable();
        }
        final Matrix gamma = Matrix.random(pairing.getZr(), bLength, aLength);

        final Vector a = new Vector(aElements);
        final Vector b = new Vector(bElements);
        final Vector x = new Vector(xElements);
        final Vector y = new Vector(yElements);

        final Element t = a.pair(y, pairing)
                .add(x.pair(b, pairing))
                .add(x.pair(gamma.multiply(y), pairing));
        final StatementImpl statement = new StatementImpl(a, b, gamma, t);
        final WitnessImpl witness = new WitnessImpl(x, y);

        return new Pair<Statement, Witness>(statement, witness);
    }
}
