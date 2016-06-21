package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.Matrix;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.Vector;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.lang3.tuple.Pair;
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
    public void testGenerateCRS() {
        final Generator generator = new GeneratorImpl(Role.PROVER);

        final CommonReferenceString crs = generator.generateCRS();
        notNull(crs);
    }

    @Test(expected = IllegalStateException.class)
    public void testGenerateStatementAndWitness_asVerifier() {
        final GeneratorImpl generator = new GeneratorImpl(Role.VERIFIER);

        final CommonReferenceString crs = generator.generateCRS();
        generator.generateStatementAndWitness(crs);
    }

    @Test
    public void testGenerateStatementAndWitness_asProver() {
        final GeneratorImpl generator = new GeneratorImpl(Role.PROVER);

        final CommonReferenceStringImpl crs = (CommonReferenceStringImpl) generator.generateCRS();
        final Pair<Statement, Witness> statementWitnessPair = generator.generateStatementAndWitness(crs);

        final Vector a = ((StatementImpl) statementWitnessPair.getLeft()).getA();
        final Vector b = ((StatementImpl) statementWitnessPair.getLeft()).getB();
        final Matrix gamma = ((StatementImpl) statementWitnessPair.getLeft()).getGamma();
        final Element t = ((StatementImpl) statementWitnessPair.getLeft()).getT();
        final Vector x = ((WitnessImpl) statementWitnessPair.getRight()).getX();
        final Vector y = ((WitnessImpl) statementWitnessPair.getRight()).getY();

        notNull(statementWitnessPair.getLeft());
        notNull(a);
        notNull(b);
        notNull(gamma);
        notNull(t);
        notNull(statementWitnessPair.getRight());
        notNull(x);
        notNull(y);

        assertEquals(a.pair(y, crs.getPairing())
                .add(x.pair(b, crs.getPairing()))
                .add(x.pair(gamma.multiply(y), crs.getPairing())).getImmutable(), t);
        assertEquals(crs.getPairing().getGT(), t.getField());
    }
}
