package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.enums.ImplementationType;
import edu.jhu.isi.grothsahai.enums.Role;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class NIZKFactoryTest extends BaseTest {
    @Test
    public void createFactore() throws Exception {
        notNull(new NIZKFactory());
    }

    @Test
    public void createGenerator_prover() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(ImplementationType.GROTH_SAHAI, Role.PROVER);

        notNull(generator);
    }

    @Test
    public void createGenerator_verifier() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(ImplementationType.GROTH_SAHAI, Role.VERIFIER);

        notNull(generator);
    }

    @Test
    public void createProver() throws Exception {
        final Prover prover = NIZKFactory.createProver(ImplementationType.GROTH_SAHAI);

        notNull(prover);
    }

    @Test
    public void createVerifier() throws Exception {
        final Verifier verifier = NIZKFactory.createVerifier(ImplementationType.GROTH_SAHAI);

        notNull(verifier);
    }

}