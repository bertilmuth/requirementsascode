package org.requirementsascode;

import java.util.function.Consumer;

public class AutonomousSystemReaction implements Consumer<ModelRunner>{
    private Consumer<ModelRunner> modelRunnerConsumer;
    private Class<? extends Object> systemReactionClass;
    private Object systemReaction;

    AutonomousSystemReaction(Runnable wrappedRunnable) {
	this(ignoredRunner -> wrappedRunnable.run());
	this.systemReactionClass = wrappedRunnable.getClass();
	this.systemReaction = wrappedRunnable;
    }
    
    AutonomousSystemReaction(Consumer<ModelRunner> modelRunnerConsumer) {
	this.modelRunnerConsumer = modelRunnerConsumer;
	this.systemReactionClass = modelRunnerConsumer.getClass();
	this.systemReaction = modelRunnerConsumer;
    }
    
    public Class<? extends Object> getSystemReactionClass() {
        return systemReactionClass;
    }
    
    public Object getSystemReaction() {
        return systemReaction;
    }

    @Override
    public void accept(ModelRunner modelRunner) {
	modelRunnerConsumer.accept(modelRunner);
    }
}