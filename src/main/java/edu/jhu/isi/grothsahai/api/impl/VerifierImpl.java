package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.QuarticElement;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import edu.jhu.isi.grothsahai.json.Serializer;
import it.unisa.dia.gas.jpbc.Element;

import java.util.List;

public class VerifierImpl implements Verifier {

    private CommonReferenceString crs;

    public VerifierImpl(final String crs) {
        this.crs = Serializer.deserializeCRS(crs);
    }

    public VerifierImpl(final CommonReferenceString crs) {
        this.crs = crs;
    }

    public Boolean verify(final String statement, final String proof) {
        final List<Statement> statementObj = Serializer.deserializeStatement(statement, crs);
        final Proof proofObj = Serializer.deserializeProof(proof, crs);
        return verify(statementObj, proofObj);
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
            lhs = crs.iota(1, statement.getA()).pairInB(proof.getD(), crs.getPairing());
        } else {
            lhs = new QuarticElement(crs.getBT(), crs.getGT().newZeroElement(), crs.getGT().newZeroElement(), crs.getGT().newZeroElement(), crs.getGT().newZeroElement());
        }
        if (statement.getB().getLength() != 0) {
            lhs = lhs.add(proof.getC().pairInB(crs.iota(2, statement.getB()), crs.getPairing()));
        }
        if (statement.getGamma() != null) {
            lhs = lhs.add(proof.getC().pairInB(statement.getGamma().multiply(proof.getD()), crs.getPairing()));
        }

        final Element rhs = crs.iotaT(ProblemType.PAIRING_PRODUCT, statement.getT())
                .add(crs.getU1().pairInB(singleProof.getPi(), crs.getPairing()))
                .add(singleProof.getTheta().pairInB(crs.getU2(), crs.getPairing()));
        return lhs.isEqual(rhs);
    }
}
