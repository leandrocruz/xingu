package br.com.ibnetwork.xingu.container;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses(value = {
		ContainerTest.class, 
		ConfigurationManagerTest.class, 
		EnvironmentTest.class,
		ResetContainerTest.class,
		RebindOnceTest.class})
public class AllTests
{}
