package org.requirementsascode;

import java.util.function.Consumer;

public class AutonomousSystemReaction implements Consumer<ModelRunner>{
    private Consumer<ModelRunner> modelRunnerConsumer;
    private Class<? extends Object> systemReactionClass;

    AutonomousSystemReaction(Runnable wrappedRunnable) {
	this(ignoredRunner -> wrappedRunnable.run());
	this.systemReactionClass = wrappedRunnable.getClass();
    }
    
    AutonomousSystemReaction(Consumer<ModelRunner> modelRunnerConsumer) {
	this.modelRunnerConsumer = modelRunnerConsumer;
	this.systemReactionClass = modelRunnerConsumer.getClass();
    }
    
    public Class<? extends Object> getSystemReactionClass() {
        return systemReactionClass;
    }

    @Override
    public void accept(ModelRunner modelRunner) {
	modelRunnerConsumer.accept(modelRunner);
    }
}