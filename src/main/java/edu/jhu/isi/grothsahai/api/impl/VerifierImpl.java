package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import org.apache.commons.lang3.NotImplementedException;

public class VerifierImpl implements Verifier {
    public Boolean verify(final CommonReferenceString crs, final Statement statement, final Proof proof) {
        throw new NotImplementedException("Not implemented yet");
    }
}
