package org.requirementsascode;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.requirementsascode.builder.BuildModelTest;

@RunWith(Suite.class)
@SuiteClasses({ CreateModelTest.class, ExceptionsThrownTest.class, ExceptionHandlingTest.class,
		SystemReactionTest.class, AdaptedSystemReactionTest.class, BuildModelTest.class })
public class AllTests {

}
