package edu.jhu.isi.grothsahai.entities.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import org.junit.Test;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class CommonReferenceStringImplTest extends BaseTest {
    @Test
    public void canGenerateRandomCRS() {
        final CommonReferenceStringImpl CRS =  CommonReferenceStringImpl.generate();
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
        final CommonReferenceStringImpl CRS =  CommonReferenceStringImpl.generate();
        isTrue(CRS.getU11().getField().equals(CRS.getB1()));
        isTrue(CRS.getU12().getField().equals(CRS.getB1()));
        isTrue(CRS.getU21().getField().equals(CRS.getB2()));
        isTrue(CRS.getU22().getField().equals(CRS.getB2()));
    }

    @Test
    public void canGenerateRandomCRS_allSubElementsInGi() {
        final CommonReferenceStringImpl CRS =  CommonReferenceStringImpl.generate();
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
        final CommonReferenceStringImpl CRS = CommonReferenceStringImpl.generate();
        CRS.iota(3, CRS.getG1().newRandomElement());
    }

    @Test
    public void testIota_firstElementIsZero() {
        final CommonReferenceStringImpl CRS = CommonReferenceStringImpl.generate();
        isTrue(CRS.iota(1, CRS.getG1().newRandomElement()).getX().isZero());
        isTrue(!CRS.iota(1, CRS.getG1().newRandomElement()).getY().isZero());
        isTrue(CRS.iota(2, CRS.getG2().newRandomElement()).getX().isZero());
        isTrue(!CRS.iota(2, CRS.getG2().newRandomElement()).getY().isZero());
    }

    @Test
    public void testIota_mapsToCorrectField() {
        final CommonReferenceStringImpl CRS = CommonReferenceStringImpl.generate();
        isTrue(CRS.iota(1, CRS.getG1().newRandomElement()).getField().equals(CRS.getB1()));
        isTrue(CRS.iota(2, CRS.getG2().newRandomElement()).getField().equals(CRS.getB2()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommit2_wrongIndex() {
        final CommonReferenceStringImpl CRS = CommonReferenceStringImpl.generate();
        CRS.commit(3, CRS.getG1().newRandomElement(), CRS.getZr().newRandomElement(), CRS.getZr().newRandomElement());
    }

    @Test
    public void testCommit_mapsToCorrectField() {
        final CommonReferenceStringImpl CRS = CommonReferenceStringImpl.generate();
        isTrue(CRS.commit(1, CRS.getG1().newRandomElement(), CRS.getZr().newRandomElement(), CRS.getZr().newRandomElement()).getField().equals(CRS.getB1()));
        isTrue(CRS.commit(2, CRS.getG2().newRandomElement(), CRS.getZr().newRandomElement(), CRS.getZr().newRandomElement()).getField().equals(CRS.getB2()));
    }
}
