package org.requirementsascode;

import java.util.function.Consumer;

public class AutonomousSystemReaction implements Consumer<ModelRunner>{
    private Runnable wrappedRunnable;
    private Consumer<ModelRunner> modelRunnerConsumer;

    AutonomousSystemReaction(Runnable wrappedRunnable) {
	this(ignoredRunner -> wrappedRunnable.run());
    }
    
    AutonomousSystemReaction(Consumer<ModelRunner> modelRunnerConsumer) {
	this.modelRunnerConsumer = modelRunnerConsumer;
    }
    
    public Runnable getWrappedRunnable() {
        return wrappedRunnable;
    }

    @Override
    public void accept(ModelRunner modelRunner) {
	modelRunnerConsumer.accept(modelRunner);
    }
}