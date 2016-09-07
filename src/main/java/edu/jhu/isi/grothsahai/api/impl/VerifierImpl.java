package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.SingleProof;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import it.unisa.dia.gas.jpbc.Element;

public class VerifierImpl implements Verifier {
    public Boolean verify(final CommonReferenceString crs, final Statement statement, final Proof proof) {
        final ProofImpl proofImpl = (ProofImpl) proof;
        final StatementImpl statementImpl = (StatementImpl) statement;
        final CommonReferenceStringImpl crsImpl = (CommonReferenceStringImpl) crs;

        for (final SingleProof singleProof : proofImpl.getProofs()) {
            if (!verifyOneEquation(proofImpl, statementImpl, crsImpl, singleProof)) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyOneEquation(final ProofImpl proofImpl, final StatementImpl statementImpl, final CommonReferenceStringImpl crsImpl, final SingleProof singleProof) {
        final Element lhs = crsImpl.iota(1, statementImpl.getA()).pairInB(proofImpl.getD(), crsImpl.getPairing())
                .add(proofImpl.getC().pairInB(crsImpl.iota(2, statementImpl.getB()), crsImpl.getPairing()))
                .add(proofImpl.getC().pairInB(statementImpl.getGamma().multiply(proofImpl.getD()), crsImpl.getPairing()));
        final Element rhs = crsImpl.iotaT(ProblemType.PAIRING_PRODUCT, statementImpl.getT())
                .add(crsImpl.getU1().pairInB(singleProof.getPi(), crsImpl.getPairing()))
                .add(singleProof.getTheta().pairInB(crsImpl.getU2(), crsImpl.getPairing()));
        return lhs.isEqual(rhs);
    }
}
