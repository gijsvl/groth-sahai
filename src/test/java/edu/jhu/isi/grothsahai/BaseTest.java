package edu.jhu.isi.grothsahai;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeDCurveGenerator;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
public abstract class BaseTest {
    protected Pairing createPairing() {
        final int discriminant = 9563;
        final PairingParametersGenerator parametersGenerator = new PBCTypeDCurveGenerator(discriminant);
        final PairingParameters params = parametersGenerator.generate();
        return PairingFactory.getPairing(params);
    }
}
