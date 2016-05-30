package edu.jhu.isi.grothsahai;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.util.Assert.notNull;

public class MathUtilTest extends BaseTest {
    @Autowired
    private MathUtil mathUtil;

    @Test
    public void existsMathUtil() {
        notNull(mathUtil);
    }
}
