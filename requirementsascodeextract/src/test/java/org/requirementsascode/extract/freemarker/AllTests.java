package org.requirementsascode.extract.freemarker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.requirementsascode.extract.freemarker.methodmodel.WordsTest;

@RunWith(Suite.class)
@SuiteClasses({FreemarkerEngineTest.class, WordsTest.class})
public class AllTests {}
