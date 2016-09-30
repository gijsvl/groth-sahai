package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.api.impl.GeneratorImpl;
import edu.jhu.isi.grothsahai.api.impl.ProverImpl;
import edu.jhu.isi.grothsahai.api.impl.VerifierImpl;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.enums.Role;

public class NIZKFactory {
    public static Generator createGenerator(final Role role) {
        return new GeneratorImpl(role);
    }

    public static Prover createProver(final CommonReferenceString crs) {
        return new ProverImpl(crs);
    }

    public static Verifier createVerifier(final CommonReferenceString crs) {
        return new VerifierImpl(crs);
    }
}
