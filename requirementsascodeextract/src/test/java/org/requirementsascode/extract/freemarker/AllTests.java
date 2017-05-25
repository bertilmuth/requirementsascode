package org.requirementsascode.extract.freemarker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.requirementsascode.extract.freemarker.methodmodel.AfterFirstWordOfMethodTest;
import org.requirementsascode.extract.freemarker.methodmodel.FirstWordOfMethodTest;
import org.requirementsascode.extract.freemarker.methodmodel.WordsOfMethodTest;

@RunWith(Suite.class)
@SuiteClasses({FreemarkerEngineTest.class, AfterFirstWordOfMethodTest.class, FirstWordOfMethodTest.class, WordsOfMethodTest.class})
public class AllTests {}
