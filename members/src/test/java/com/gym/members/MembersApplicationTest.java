package com.gym.members;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MembersApplicationTest {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring arranca correctamente
    }

    @Test
    void mainMethodRuns() {
        MembersApplication.main(new String[]{});
    }
}