package org.requirementsascode;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CreateModelTest.class, ExceptionsThrownTest.class, 
	ExceptionHandlingTest.class, SystemReactionTest.class })
public class AllTests {

}
