package fr.cashmanager.integration;

import org.junit.Test;

/**
 * AuthentScenarioTest
 */
public class TransactionScenarioTest extends ScenarioTestBase {

    protected int getServerPort() {
        return 3213;
    }

    @Test
    public void scenario() throws Exception {
        runScenario("/fr/cashmanager/scenarios/TransactionScenario.txt");
    }
}