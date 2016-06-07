package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.api.impl.GeneratorImpl;
import edu.jhu.isi.grothsahai.api.impl.ProverImpl;
import edu.jhu.isi.grothsahai.api.impl.VerifierImpl;
import edu.jhu.isi.grothsahai.enums.ImplementationType;
import edu.jhu.isi.grothsahai.enums.Role;

public class NIZKFactory {
    public static Generator createGenerator(final ImplementationType type, final Role role) {
        return new GeneratorImpl(role);
    }

    public static Prover createProver(final ImplementationType type) {
        return new ProverImpl();
    }

    public static Verifier createVerifier(final ImplementationType type) {
        return new VerifierImpl();
    }
}
