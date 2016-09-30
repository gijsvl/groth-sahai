package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.util.Assert.notNull;

public class GeneratorImplTest extends BaseTest {
    @Test
    public void canCreateGenerator() {
        final Generator generator = new GeneratorImpl(Role.PROVER);
        notNull(generator);
    }

    @Test
    public void testGeneratePairing() {
        final Generator generator = new GeneratorImpl(Role.PROVER);

        final Pairing pairing = generator.generatePairing();
        notNull(pairing);
    }

    @Test
    public void testGenerateCRS() {
        final Generator generator = new GeneratorImpl(Role.PROVER);

        final CommonReferenceString crs = generator.generateCRS(generator.generatePairing());
        notNull(crs);
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateStatementAndWitness_asVerifier() {
        final GeneratorImpl generator = new GeneratorImpl(Role.VERIFIER);

        final Pairing pairing = generator.generatePairing();
        generator.generateStatementAndWitness(pairing);
    }

    @Test
    public void testGenerateStatementAndWitness_asProver() {
        final GeneratorImpl generator = new GeneratorImpl(Role.PROVER);

        final Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = (CommonReferenceString) generator.generateCRS(pairing);
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing);

        final Statement statement = (Statement) statementWitnessPair.getStatement().get(0);
        final Vector a = statement.getA();
        final Vector b = statement.getB();
        final Matrix gamma = statement.getGamma();
        final Element t = statement.getT();
        final Vector x = ((Witness) statementWitnessPair.getWitness()).getX();
        final Vector y = ((Witness) statementWitnessPair.getWitness()).getY();

        notNull(statementWitnessPair.getStatement());
        notNull(a);
        notNull(b);
        notNull(gamma);
        notNull(t);
        notNull(statementWitnessPair.getWitness());
        notNull(x);
        notNull(y);

        assertEquals(a.pair(y, crs.getPairing())
                .add(x.pair(b, crs.getPairing()))
                .add(x.pair(gamma.multiply(y), crs.getPairing())).getImmutable(), t);
        assertEquals(crs.getPairing().getGT(), t.getField());
    }
}
