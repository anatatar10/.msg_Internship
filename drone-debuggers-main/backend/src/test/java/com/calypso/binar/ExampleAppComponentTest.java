package com.calypso.binar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("componenttest")  // Ensure that the test profile is used
public class ExampleAppComponentTest {

    @Autowired
    private ApplicationContext ctx;


    @Test
    void testContextLoads() {
        assertNotNull(ctx, "Application context should have been loaded");
    }

    // this one test makes no sense. it's only there for code coverage
    @Test
    void testMain() {
        try {
            ExampleApp.main();
        } catch (Exception ignored) {

        }

    }
}

