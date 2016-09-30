package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.QuarticElement;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public class VerifierImpl implements Verifier {

    private CommonReferenceString crsImpl;

    public VerifierImpl(final CommonReferenceString crs) {
        this.crsImpl = crs;
    }

    public Boolean verify(final List<Statement> statements, final Proof proof) {
        if (proof.getProofs().size() != statements.size()) {
            return false;
        }

        for (int i = 0; i < proof.getProofs().size(); i++) {
            if (!verifyOneEquation(proof, statements.get(i), proof.getProofs().get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean verifyOneEquation(final Proof proof, final Statement statement, final SingleProof singleProof) {
        Element lhs;
        if (statement.getA().getLength() != 0) {
            lhs = crsImpl.iota(1, statement.getA()).pairInB(proof.getD(), crsImpl.getPairing());
        } else {
            lhs = new QuarticElement(crsImpl.getBT(), crsImpl.getGT().newZeroElement(), crsImpl.getGT().newZeroElement(), crsImpl.getGT().newZeroElement(), crsImpl.getGT().newZeroElement());
        }
        if (statement.getB().getLength() != 0) {
            lhs = lhs.add(proof.getC().pairInB(crsImpl.iota(2, statement.getB()), crsImpl.getPairing()));
        }
        if (statement.getGamma() != null) {
            lhs = lhs.add(proof.getC().pairInB(statement.getGamma().multiply(proof.getD()), crsImpl.getPairing()));
        }

        final Element rhs = crsImpl.iotaT(ProblemType.PAIRING_PRODUCT, statement.getT())
                .add(crsImpl.getU1().pairInB(singleProof.getPi(), crsImpl.getPairing()))
                .add(singleProof.getTheta().pairInB(crsImpl.getU2(), crsImpl.getPairing()));
        return lhs.isEqual(rhs);
    }
}
