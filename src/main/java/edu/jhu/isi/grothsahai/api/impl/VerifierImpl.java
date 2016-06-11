package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.Vector;

public class VerifierImpl implements Verifier {
    public Boolean verify(final CommonReferenceString crs, final Statement statement, final Proof proof) {
        final ProofImpl proofImpl = (ProofImpl) proof;
        final StatementImpl statementImpl = (StatementImpl) statement;
        final CommonReferenceStringImpl crsImpl = (CommonReferenceStringImpl) crs;

        final Vector lhs;
        final Vector rhs;

        return false;
    }
}
