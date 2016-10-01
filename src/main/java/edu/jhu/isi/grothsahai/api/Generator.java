package edu.jhu.isi.grothsahai.api;

import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;

public interface Generator {
    PairingParameters generatePairingParams();

    CommonReferenceString generateCRS(final PairingParameters pairing);

    StatementAndWitness generateStatementAndWitness(final Pairing pairing);

    StatementAndWitness generateStatementAndWitness(final Pairing pairing, final int aLength, final int bLength, final int nrOfStatements);
}
