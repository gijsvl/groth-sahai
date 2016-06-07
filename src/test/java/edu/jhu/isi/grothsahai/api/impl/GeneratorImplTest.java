package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.enums.Role;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class GeneratorImplTest extends BaseTest {
    @Test
    public void canCreateGenerator() {
        Generator generator = new GeneratorImpl(Role.PROVER);
        notNull(generator);
    }

    @Test(expected = NotImplementedException.class)
    public void testGenerateCRS() {
        Generator generator = new GeneratorImpl(Role.PROVER);
        generator.generateCRS();
    }

    @Test(expected = NotImplementedException.class)
    public void testGenerateStatementAndWitness() {
        Generator generator = new GeneratorImpl(Role.PROVER);
        generator.generateStatementAndWitness();
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateStatementAndWitness_asVerifier() {
        Generator generator = new GeneratorImpl(Role.VERIFIER);
        generator.generateStatementAndWitness();
    }
}
