package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import org.apache.commons.lang3.NotImplementedException;

public class ProverImpl implements Prover {
    public Proof proof(final CommonReferenceString crs, final Statement statement, final Witness witness) {
        throw new NotImplementedException("Not implemented yet");
    }
}
