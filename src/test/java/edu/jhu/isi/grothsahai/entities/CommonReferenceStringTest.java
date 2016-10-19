/*
 * Copyright (c) 2016 Gijs Van Laer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.jhu.isi.grothsahai.entities;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.enums.ProblemType;
import edu.jhu.isi.grothsahai.exceptions.NotImplementedException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class CommonReferenceStringTest extends BaseTest {
    @Test
    public void canGenerateRandomCRS() {
        final CommonReferenceString CRS =  CommonReferenceString.generate(createPairingParams());
        isTrue(!CRS.getU11().isZero());
        isTrue(!CRS.getU11().isOne());
        isTrue(!CRS.getU12().isZero());
        isTrue(!CRS.getU12().isOne());
        isTrue(!CRS.getU21().isZero());
        isTrue(!CRS.getU21().isOne());
        isTrue(!CRS.getU22().isZero());
        isTrue(!CRS.getU22().isOne());
        notNull(CRS.getZr());
        notNull(CRS.getG1());
        notNull(CRS.getG2());
        notNull(CRS.getGT());
        notNull(CRS.getB1());
        notNull(CRS.getB2());
        notNull(CRS.getBT());
    }

    @Test
    public void canGenerateRandomCRS_allElementsInBi() {
        final CommonReferenceString CRS =  CommonReferenceString.generate(createPairingParams());
        isTrue(CRS.getU11().getField().equals(CRS.getB1()));
        isTrue(CRS.getU12().getField().equals(CRS.getB1()));
        isTrue(CRS.getU21().getField().equals(CRS.getB2()));
        isTrue(CRS.getU22().getField().equals(CRS.getB2()));
    }

    @Test
    public void canGenerateRandomCRS_allSubElementsInGi() {
        final CommonReferenceString CRS =  CommonReferenceString.generate(createPairingParams());
        isTrue(CRS.getU11().getX().getField().equals(CRS.getG1()));
        isTrue(CRS.getU11().getY().getField().equals(CRS.getG1()));
        isTrue(CRS.getU12().getX().getField().equals(CRS.getG1()));
        isTrue(CRS.getU12().getY().getField().equals(CRS.getG1()));
        isTrue(CRS.getU21().getX().getField().equals(CRS.getG2()));
        isTrue(CRS.getU21().getY().getField().equals(CRS.getG2()));
        isTrue(CRS.getU22().getX().getField().equals(CRS.getG2()));
        isTrue(CRS.getU22().getY().getField().equals(CRS.getG2()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIota_wrongIndex() {
        final CommonReferenceString CRS = CommonReferenceString.generate(createPairingParams());
        CRS.iota(3, CRS.getG1().newRandomElement());
    }

    @Test
    public void testIota_firstElementIsZero() {
        final CommonReferenceString CRS = CommonReferenceString.generate(createPairingParams());
        final QuadraticElement iota1 = CRS.iota(1, CRS.getG1().newRandomElement());
        final QuadraticElement iota2 = CRS.iota(2, CRS.getG2().newRandomElement());
        isTrue(iota1.getX().isZero());
        isTrue(!iota1.getY().isZero());
        isTrue(iota2.getX().isZero());
        isTrue(!iota2.getY().isZero());
    }

    @Test
    public void testIota_mapsToCorrectField() {
        final CommonReferenceString CRS = CommonReferenceString.generate(createPairingParams());
        isTrue(CRS.iota(1, CRS.getG1().newRandomElement()).getField().equals(CRS.getB1()));
        isTrue(CRS.iota(2, CRS.getG2().newRandomElement()).getField().equals(CRS.getB2()));
    }

    @Test
    public void testIotaT_PairingProduct() {
        final CommonReferenceString crs = CommonReferenceString.generate(createPairingParams());
        final Element x = crs.getG1().newRandomElement();
        final QuarticElement iotaT = crs.iotaT(ProblemType.PAIRING_PRODUCT, x);

        notNull(iotaT);
        isTrue(iotaT.getW().isOne());
        isTrue(iotaT.getX().isOne());
        isTrue(iotaT.getY().isOne());
        assertEquals(x, iotaT.getZ());
    }

    @Test(expected = NotImplementedException.class)
    public void testIotaT_MultiScalarG1() {
        final CommonReferenceString crs = CommonReferenceString.generate(createPairingParams());
        final Element x = crs.getG1().newRandomElement();

        crs.iotaT(ProblemType.MULTI_SCALAR_G1, x);
    }

    @Test(expected = NotImplementedException.class)
    public void testIotaT_MultiScalarG2() {
        final CommonReferenceString crs = CommonReferenceString.generate(createPairingParams());
        final Element x = crs.getG1().newRandomElement();

        crs.iotaT(ProblemType.MULTI_SCALAR_G2, x);
    }

    @Test(expected = NotImplementedException.class)
    public void testIotaT_Quadratic() {
        final CommonReferenceString crs = CommonReferenceString.generate(createPairingParams());
        final Element x = crs.getG1().newRandomElement();

        crs.iotaT(ProblemType.QUADRATIC, x);
    }
}
