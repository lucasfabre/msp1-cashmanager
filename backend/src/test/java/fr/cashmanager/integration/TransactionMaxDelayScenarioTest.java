package fr.cashmanager.integration;

import org.junit.Test;

import fr.cashmanager.config.Preference;

/**
 * AuthentScenarioTest
 */
public class TransactionMaxDelayScenarioTest extends ScenarioTestBase {

    protected int getServerPort() {
        return 3214;
    }

    @Test
    public void scenario() throws Exception {
        mockedLocalFileConfig.setPreference(Preference.MAX_TRANSACTION_DELAY.getName(), "-2");
        runScenario("/fr/cashmanager/scenarios/TransactionMaxDelayScenario.txt");
    }
}