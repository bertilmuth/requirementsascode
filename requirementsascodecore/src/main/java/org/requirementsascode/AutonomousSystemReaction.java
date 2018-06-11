package org.requirementsascode;

import java.util.function.Consumer;

public class AutonomousSystemReaction implements Consumer<ModelRunner>{
    private Runnable wrappedRunnable;

    AutonomousSystemReaction(Runnable wrappedRunnable) {
        this.wrappedRunnable = wrappedRunnable;
    }
    
    public Runnable getWrappedRunnable() {
        return wrappedRunnable;
    }

    @Override
    public void accept(ModelRunner modelRunner) {
        wrappedRunnable.run();
    }
}