package edu.jhu.isi.grothsahai;

import edu.jhu.isi.grothsahai.api.Prover;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class ProverImplTest extends BaseTest {
    @Test
    public void canCreateProver() {
        Prover prover = new ProverImpl();
        notNull(prover);
    }
}
