package com.redhat.insights.agent;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Optional;

public class InsightsAutoConfigurationCustomizer implements AutoConfigurationCustomizerProvider {
    @Override
    public void customize(AutoConfigurationCustomizer autoConfigurationCustomizer) {
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        int count = 0;
        String agentArgs = "";
        for (String arg : args) {
            if (arg.startsWith("-javaagent:")) {
                count += 1;
                agentArgs = arg.substring("-javaagent:".length());
            }
        }
        if (count == 0 || count > 1) {
            System.err.println("Unable to start Red Hat Insights client: Exactly 1 Java agent allowed");
        }
        AgentMain.startAgent(agentArgs, Optional.empty());
    }
}
