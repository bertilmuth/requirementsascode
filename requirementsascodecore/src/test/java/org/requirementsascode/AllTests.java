package org.requirementsascode;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BuildModelTest.class, ExceptionsThrownTest.class, ExceptionHandlingTest.class,
		SystemReactionTest.class, AdaptedSystemReactionTest.class, BuildModelTest.class })
public class AllTests {

}
