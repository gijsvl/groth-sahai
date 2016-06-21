package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.Matrix;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.Vector;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class GeneratorImpl implements Generator {
    private Role role;

    public GeneratorImpl(final Role role) {
        this.role = role;
    }

    public CommonReferenceString generateCRS() {
        return CommonReferenceStringImpl.generate();
    }

    public Pair<Statement, Witness> generateStatementAndWitness(final CommonReferenceString crs) {
        if (role == Role.VERIFIER) {
            throw new IllegalStateException("The verifier should not create a statement witness pair, " +
                    "but should receive the statement from the prover at generation time.");
        }
        final CommonReferenceStringImpl crsImpl = (CommonReferenceStringImpl) crs;
        Element[] aElements = new Element[5];
        Element[] xElements = new Element[5];
        Element[] bElements = new Element[5];
        Element[] yElements = new Element[5];
        for (int i = 0; i < 5; i++) {
            aElements[i] = crsImpl.getG1().newRandomElement().getImmutable();
            xElements[i] = crsImpl.getG1().newRandomElement().getImmutable();
            bElements[i] = crsImpl.getG2().newRandomElement().getImmutable();
            yElements[i] = crsImpl.getG2().newRandomElement().getImmutable();
        }
        final Matrix gamma = Matrix.random(crsImpl.getZr(), 5, 5);

        final Vector a = new Vector(aElements);
        final Vector b = new Vector(bElements);
        final Vector x = new Vector(xElements);
        final Vector y = new Vector(yElements);

        final Element t = a.pair(y, crsImpl.getPairing())
                .add(x.pair(b, crsImpl.getPairing()))
                .add(x.pair(gamma.multiply(y), crsImpl.getPairing()));
        final StatementImpl statement = new StatementImpl(a, b, gamma, t);
        final WitnessImpl witness = new WitnessImpl(x, y);

        return new ImmutablePair<Statement, Witness>(statement, witness);
    }
}
