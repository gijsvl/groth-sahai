package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.QuarticElement;
import edu.jhu.isi.grothsahai.entities.impl.SingleProof;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public class VerifierImpl implements Verifier {

    private CommonReferenceStringImpl crsImpl;

    public VerifierImpl(final CommonReferenceString crs) {
        this.crsImpl = (CommonReferenceStringImpl) crs;
    }

    public Boolean verify(final List<Statement> statements, final Proof proof) {
        final ProofImpl proofImpl = (ProofImpl) proof;

        if (proofImpl.getProofs().size() != statements.size()) {
            return false;
        }

        for (int i = 0; i < proofImpl.getProofs().size(); i++) {
            if (!verifyOneEquation(proofImpl, (StatementImpl) statements.get(i), proofImpl.getProofs().get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyOneEquation(final ProofImpl proofImpl, final StatementImpl statementImpl, final SingleProof singleProof) {
        Element lhs;
        if (statementImpl.getA().getLength() != 0) {
            lhs = crsImpl.iota(1, statementImpl.getA()).pairInB(proofImpl.getD(), crsImpl.getPairing());
        } else {
            lhs = new QuarticElement(crsImpl.getBT(), crsImpl.getGT().newZeroElement(), crsImpl.getGT().newZeroElement(), crsImpl.getGT().newZeroElement(), crsImpl.getGT().newZeroElement());
        }
        if (statementImpl.getB().getLength() != 0) {
            lhs = lhs.add(proofImpl.getC().pairInB(crsImpl.iota(2, statementImpl.getB()), crsImpl.getPairing()));
        }
        if (statementImpl.getGamma() != null) {
            lhs = lhs.add(proofImpl.getC().pairInB(statementImpl.getGamma().multiply(proofImpl.getD()), crsImpl.getPairing()));
        }

        final Element rhs = crsImpl.iotaT(ProblemType.PAIRING_PRODUCT, statementImpl.getT())
                .add(crsImpl.getU1().pairInB(singleProof.getPi(), crsImpl.getPairing()))
                .add(singleProof.getTheta().pairInB(crsImpl.getU2(), crsImpl.getPairing()));
        return lhs.isEqual(rhs);
    }
}
