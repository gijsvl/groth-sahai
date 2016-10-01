package edu.jhu.isi.grothsahai.json;

import it.unisa.dia.gas.jpbc.Field;

import java.util.Objects;

class FieldEquals {
    public static boolean equals(final Field field1, final Field field2) {
        if (field1 == field2) return true;
        if (field1 == null || field2 == null || field1.getClass() != field2.getClass()) return false;
        return Objects.equals(field1.getLengthInBytes(), field2.getLengthInBytes()) &&
                Objects.equals(field1.getOrder(), field2.getOrder());
    }
}
