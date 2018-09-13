package org.requirementsascode;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BuildModelTest.class, RunStopAndRestartTest.class, FlowTest.class, CanReactToTest.class, ReactToTypesTest.class, FlowlessTest.class,
	ExceptionsThrownTest.class, ExceptionHandlingTest.class, NonStandardEventHandlingTest.class,
	IncludesTest.class })
public class AllTests {
}
