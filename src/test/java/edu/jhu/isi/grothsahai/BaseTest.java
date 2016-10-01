package edu.jhu.isi.grothsahai;

import edu.jhu.isi.grothsahai.api.impl.GeneratorImpl;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.PairingParameters;

public abstract class BaseTest {
    protected PairingParameters createPairingParams() {
        return new GeneratorImpl(Role.PROVER).generatePairingParams();
    }

    protected Vector generateVector(final int rows, final Field field) {
        final Element[] elements = new Element[rows];
        for (int i = 0; i < rows; i++) {
            elements[i] = field.newRandomElement().getImmutable();
        }
        return new Vector(elements);
    }
}
