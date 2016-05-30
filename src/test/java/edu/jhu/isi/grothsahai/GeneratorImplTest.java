package edu.jhu.isi.grothsahai;

import edu.jhu.isi.grothsahai.api.Generator;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;

public class GeneratorImplTest extends BaseTest {
    @Test
    public void canCreateProver() {
        Generator generator = new GeneratorImpl();
        notNull(generator);
    }
}
