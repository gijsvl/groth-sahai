package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.Matrix;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.Vector;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;

public class ProverImpl implements Prover {
    public Proof proof(final CommonReferenceString crs, final Statement statement, final Witness witness) {
        final StatementImpl statementImpl = (StatementImpl) statement;
        final WitnessImpl witnessImpl = (WitnessImpl) witness;
        final CommonReferenceStringImpl crsImpl = (CommonReferenceStringImpl) crs;

        final Matrix R = Matrix.random(crsImpl.getZr(), witnessImpl.getX().getLength(), 2);
        final Matrix S = Matrix.random(crsImpl.getZr(), witnessImpl.getY().getLength(), 2);
        final Matrix T = Matrix.random(crsImpl.getZr(), 2, 2);
        final Vector c = crsImpl.iota(1, witnessImpl.getX()).add(R.multiply(crsImpl.getU1()));
        final Vector d = crsImpl.iota(2, witnessImpl.getY()).add(S.multiply(crsImpl.getU2()));

        final Vector pi = R.getTranspose().multiply(crsImpl.iota(2, statementImpl.getB()))
                .add(R.getTranspose().multiply(statementImpl.getGamma()).multiply(crsImpl.iota(2, witnessImpl.getY())))
                .add(R.getTranspose().multiply(statementImpl.getGamma()).multiply(S).multiply(crsImpl.getU2()))
                .sub(T.getTranspose().multiply(crsImpl.getU2()));
        final Vector theta = S.getTranspose().multiply(crsImpl.iota(1, statementImpl.getA()))
                .add(S.getTranspose().multiply(statementImpl.getGamma().getTranspose())
                        .multiply(crsImpl.iota(1, witnessImpl.getX())))
                .add(T.multiply(crsImpl.getU1()));

        return new ProofImpl(c, d, pi, theta);
    }
}
