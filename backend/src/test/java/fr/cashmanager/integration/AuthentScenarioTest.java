package fr.cashmanager.integration;

import org.junit.Test;

/**
 * AuthentScenarioTest
 */
public class AuthentScenarioTest extends ScenarioTestBase {

    protected int getServerPort() {
        return 3212;
    }

    @Test
    public void scenario() throws Exception {
        runScenario("/fr/cashmanager/scenarios/ConnectionScenario.txt");
    }
}