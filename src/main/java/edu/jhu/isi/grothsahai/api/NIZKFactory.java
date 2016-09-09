package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.api.impl.GeneratorImpl;
import edu.jhu.isi.grothsahai.api.impl.ProverImpl;
import edu.jhu.isi.grothsahai.api.impl.VerifierImpl;
import edu.jhu.isi.grothsahai.enums.Role;

public class NIZKFactory {
    public static Generator createGenerator(final Role role) {
        return new GeneratorImpl(role);
    }

    public static Prover createProver() {
        return new ProverImpl();
    }

    public static Verifier createVerifier() {
        return new VerifierImpl();
    }
}
