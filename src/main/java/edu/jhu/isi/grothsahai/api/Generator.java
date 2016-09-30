package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.impl.StatementAndWitness;
import it.unisa.dia.gas.jpbc.Pairing;

public interface Generator {
    Pairing generatePairing();

    CommonReferenceString generateCRS(final Pairing pairing);

    StatementAndWitness generateStatementAndWitness(final Pairing pairing);

    StatementAndWitness generateStatementAndWitness(final Pairing pairing, final int aLength, final int bLength, final int nrOfStatements);
}
