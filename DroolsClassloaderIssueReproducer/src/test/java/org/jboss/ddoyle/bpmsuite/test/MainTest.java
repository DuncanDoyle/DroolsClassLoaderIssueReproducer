package org.jboss.ddoyle.bpmsuite.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.drools.core.common.InternalRuleBase;
import org.drools.core.impl.InternalKnowledgeBase;
import org.jboss.ddoyle.bpmsuite.test.wih.TestWebServiceWorkItemHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class MainTest {

	private static KieContainer kieContainer;
	

	@BeforeClass
	public static void initKieContainer() {
		KieServices kieServices = KieServices.Factory.get();
		ReleaseId kjarReleaseId = kieServices.newReleaseId("org.jboss.ddoyle.bpms.classloaderissue", "DroolsClassloaderIssueKJar",
				"1.0.0-SNAPSHOT");
		kieContainer = kieServices.newKieContainer(kjarReleaseId);
	}

	/*
	 * Load and initialize a new KieSession on every test.
	 */
	@Before
	public void initKieSession() {
	}

	@Test
	public void testWithKieContainerClassLoader() {
		KieSession kieSession = kieContainer.newKieSession();
		ClassLoader kieClassLoader = kieContainer.getClassLoader();
		
		TestWebServiceWorkItemHandler wih = new TestWebServiceWorkItemHandler(kieSession, kieClassLoader);
		kieSession.getWorkItemManager().registerWorkItemHandler("WebService", wih);
		kieSession.startProcess("WebServiceIntegrationProcess.SecuredWebServiceProcess");
		kieSession.dispose();
		
		assertTrue(wih.isFoundResource());
	}
	
	@Test
	public void testWithRuleBaseClassLoader() {
		KieSession kieSession = kieContainer.newKieSession();
		ClassLoader kieClassLoader = ((InternalRuleBase) ((InternalKnowledgeBase) kieSession.getKieBase()).getRuleBase()).getRootClassLoader();
		TestWebServiceWorkItemHandler wih = new TestWebServiceWorkItemHandler(kieSession, kieClassLoader);
		kieSession.getWorkItemManager().registerWorkItemHandler("WebService", wih);
		kieSession.startProcess("WebServiceIntegrationProcess.SecuredWebServiceProcess");
		kieSession.dispose();
		
		assertTrue(wih.isFoundResource());
	}

}
