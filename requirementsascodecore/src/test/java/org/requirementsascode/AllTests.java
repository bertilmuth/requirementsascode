package org.requirementsascode;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BuildModelTest.class, RunnAndStopTest.class, FlowTest.class, FlowlessTest.class, ExceptionsThrownTest.class,
	ExceptionHandlingTest.class, AdaptedSystemReactionTest.class, IncludesTest.class })
public class AllTests {

}
