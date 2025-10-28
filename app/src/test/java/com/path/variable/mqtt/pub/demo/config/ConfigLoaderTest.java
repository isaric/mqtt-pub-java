package com.path.variable.mqtt.pub.demo.config;

import static org.junit.jupiter.api.Assertions.*;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConfigLoaderTest {

    private static final String SAMPLE_YAML = """
            mqtt:
              addr: tcp://localhost:1883
              clientId: test-client
              topic: sensors/room
              intervalMs: 250
            sensors:
              - label: temperature
                min: 18
                max: 28
                step: 0.5
                integer: false
              - label: co2
                min: 400
                max: 1500
                step: 50
                initial: 500
                integer: true
            """;

    @Test
    void loadsFromAppConfigEnvVariable() throws Exception {
        AppConfig cfg = SystemLambda.withEnvironmentVariable(ConfigLoader.ENV_CONFIG_YAML, SAMPLE_YAML)
                .execute(() -> ConfigLoader.load());

        assertNotNull(cfg);
        assertEquals("tcp://localhost:1883", cfg.mqtt().addr());
        assertEquals("test-client", cfg.mqtt().clientId());
        assertEquals("sensors/room", cfg.mqtt().topic());
        assertEquals(250, cfg.mqtt().intervalMs());

        assertEquals(2, cfg.sensors().size());
        SensorConfig s0 = cfg.sensors().get(0);
        assertEquals("temperature", s0.label());
        assertEquals(18.0, s0.min());
        assertEquals(28.0, s0.max());
        assertEquals(0.5, s0.step());
        assertNull(s0.initial());
        assertEquals(Boolean.FALSE, s0.integer());

        SensorConfig s1 = cfg.sensors().get(1);
        assertEquals("co2", s1.label());
        assertEquals(400.0, s1.min());
        assertEquals(1500.0, s1.max());
        assertEquals(50.0, s1.step());
        assertEquals(500.0, s1.initial());
        assertEquals(Boolean.TRUE, s1.integer());
    }

    @Test
    void loadsFromAppConfigPathEnvVariable() throws Exception {
        Path temp = Files.createTempFile("config", ".yaml");
        Files.writeString(temp, SAMPLE_YAML);

        AppConfig cfg = SystemLambda.withEnvironmentVariable(ConfigLoader.ENV_CONFIG_PATH, temp.toString())
                .execute(() -> ConfigLoader.load());

        assertNotNull(cfg);
        assertEquals(2, cfg.sensors().size());
    }

    @Test
    void errorsWhenNoConfigProvided() throws Exception {
        Exception ex = assertThrows(IllegalStateException.class, () -> ConfigLoader.load());
        assertTrue(ex.getMessage().contains("No configuration provided"));
    }

    @Test
    void validatesYamlStructure() throws Exception {
        String badYaml = """
                mqtt:
                  addr: tcp://x
                  topic: t
                # sensors missing
                """;
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                SystemLambda.withEnvironmentVariable(ConfigLoader.ENV_CONFIG_YAML, badYaml)
                        .execute(() -> ConfigLoader.load())
        );
        assertTrue(ex.getMessage().contains("sensors"));

        String badSensor = """
                mqtt:
                  addr: tcp://x
                  topic: t
                sensors:
                  - label: temp
                    min: 10
                    max: 5
                    step: 1
                """;
        Exception ex2 = assertThrows(IllegalArgumentException.class, () ->
                SystemLambda.withEnvironmentVariable(ConfigLoader.ENV_CONFIG_YAML, badSensor)
                        .execute(() -> ConfigLoader.load())
        );
        assertTrue(ex2.getMessage().contains("max < min"));
    }
}
