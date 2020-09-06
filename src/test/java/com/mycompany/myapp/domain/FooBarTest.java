package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class FooBarTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FooBar.class);
        FooBar fooBar1 = new FooBar();
        fooBar1.setId(1L);
        FooBar fooBar2 = new FooBar();
        fooBar2.setId(fooBar1.getId());
        assertThat(fooBar1).isEqualTo(fooBar2);
        fooBar2.setId(2L);
        assertThat(fooBar1).isNotEqualTo(fooBar2);
        fooBar1.setId(null);
        assertThat(fooBar1).isNotEqualTo(fooBar2);
    }
}
