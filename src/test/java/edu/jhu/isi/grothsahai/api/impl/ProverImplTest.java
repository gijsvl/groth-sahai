package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class ProverImplTest extends BaseTest {
    @Test
    public void canCreateProver() {
        Prover prover = new ProverImpl();
        notNull(prover);
    }

    @Test(expected = NotImplementedException.class)
    public void testProof() {
        final Prover prover = new ProverImpl();
        final CommonReferenceString crs = null;
        final Statement statement = null;
        final Witness witness = null;
        prover.proof(crs, statement, witness);
    }
}
