package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class VerifierImplTest extends BaseTest {
    @Test
    public void canCreateVerifier() {
        final Verifier verifier = new VerifierImpl();
        notNull(verifier);
    }

    @Test(expected = NotImplementedException.class)
    public void testVerify() {
        final Verifier verifier = new VerifierImpl();
        final CommonReferenceString crs = null;
        final Statement statement = null;
        final Proof proof = null;
        verifier.verify(crs, statement, proof);
    }
}
