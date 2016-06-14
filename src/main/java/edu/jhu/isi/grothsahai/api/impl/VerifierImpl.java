package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import it.unisa.dia.gas.jpbc.Element;

public class VerifierImpl implements Verifier {
    public Boolean verify(final CommonReferenceString crs, final Statement statement, final Proof proof) {
        final ProofImpl proofImpl = (ProofImpl) proof;
        final StatementImpl statementImpl = (StatementImpl) statement;
        final CommonReferenceStringImpl crsImpl = (CommonReferenceStringImpl) crs;

        final Element lhs = crsImpl.iota(1, statementImpl.getA()).pair(proofImpl.getD(),crsImpl.getPairing())
                .add(proofImpl.getC().pair(crsImpl.iota(2, statementImpl.getB()), crsImpl.getPairing()))
                .add(proofImpl.getC().pair(statementImpl.getGamma().multiply(proofImpl.getD()), crsImpl.getPairing()));
        final Element rhs = crsImpl.iotaT(ProblemType.PAIRING_PRODUCT, statementImpl.getT())
                .add(crsImpl.getU1().pair(proofImpl.getPi(), crsImpl.getPairing()))
                .add(proofImpl.getTheta().pair(crsImpl.getU2(), crsImpl.getPairing()));

        return lhs.isEqual(rhs);
    }
}
