package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import org.junit.Test;

import java.util.Arrays;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class VerifierImplTest extends BaseTest {
    @Test
    public void canCreateVerifier() {
        final CommonReferenceString crs = null;
        final Verifier verifier = new VerifierImpl(crs);
        notNull(verifier);
    }

    @Test(expected = NullPointerException.class)
    public void testVerify() {
        final CommonReferenceString crs = null;
        final Verifier verifier = new VerifierImpl(crs);
        final Statement statement = null;
        final Proof proof = null;
        isTrue(!verifier.verify(Arrays.asList(statement), proof));
    }
}
