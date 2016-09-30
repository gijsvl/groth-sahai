package edu.jhu.isi.grothsahai;

import edu.jhu.isi.grothsahai.entities.Vector;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeDCurveGenerator;

public abstract class BaseTest {
    protected Pairing createPairing() {
        final int discriminant = 9563;
        final PairingParametersGenerator parametersGenerator = new PBCTypeDCurveGenerator(discriminant);
        final PairingParameters params = parametersGenerator.generate();
        return PairingFactory.getPairing(params);
    }

    protected Vector generateVector(final int rows, final Field field) {
        final Element[] elements = new Element[rows];
        for (int i = 0; i < rows; i++) {
            elements[i] = field.newRandomElement().getImmutable();
        }
        return new Vector(elements);
    }
}
