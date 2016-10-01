package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.PairingParameters;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class NIZKFactoryTest extends BaseTest {
    @Test
    public void createFactore() throws Exception {
        notNull(new NIZKFactory());
    }

    @Test
    public void createGenerator_prover() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(Role.PROVER);

        notNull(generator);
    }

    @Test
    public void createGenerator_verifier() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(Role.VERIFIER);

        notNull(generator);
    }

    @Test
    public void createProver() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(Role.VERIFIER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final Prover prover = NIZKFactory.createProver(crs);

        notNull(prover);
    }

    @Test
    public void createVerifier() throws Exception {
        final Generator generator = NIZKFactory.createGenerator(Role.VERIFIER);
        PairingParameters pairingParams = generator.generatePairingParams();
        final CommonReferenceString crs = generator.generateCRS(pairingParams);
        final Verifier verifier = NIZKFactory.createVerifier(crs);

        notNull(verifier);
    }

}