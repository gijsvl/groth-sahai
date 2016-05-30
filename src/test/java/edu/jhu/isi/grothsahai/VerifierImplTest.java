package edu.jhu.isi.grothsahai;

import edu.jhu.isi.grothsahai.api.Verifier;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class VerifierImplTest extends BaseTest {
    @Test
    public void canCreateVerifier() {
        Verifier verifier = new VerifierImpl();
        notNull(verifier);
    }
}
