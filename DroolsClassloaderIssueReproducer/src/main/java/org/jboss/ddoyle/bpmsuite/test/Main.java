package org.jboss.ddoyle.bpmsuite.test;

import org.drools.core.common.InternalRuleBase;
import org.drools.core.impl.InternalKnowledgeBase;
import org.jboss.ddoyle.bpmsuite.test.wih.TestWebServiceWorkItemHandler;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.runtime.manager.InternalRuntimeManager;

public class Main {
	
	
	public static void main(String[] args) throws Exception {
		
		KieServices kieServices = KieServices.Factory.get();
		
		ReleaseId kjarReleaseId = kieServices.newReleaseId("org.jboss.ddoyle.bpms.classloaderissue", "DroolsClassloaderIssueKJar", "1.0.0-SNAPSHOT");

		KieContainer kieContainer = kieServices.newKieContainer(kjarReleaseId);
		
		KieSession kieSession = kieContainer.newKieSession();
		
		//ClassLoader kieClassLoader = kieContainer.getClassLoader();
		ClassLoader kieClassLoader = ((InternalRuleBase)((InternalKnowledgeBase)kieSession.getKieBase()).getRuleBase()).getRootClassLoader();
		//ClassLoader kieClassLoader = Main.class.getClassLoader();
		
		TestWebServiceWorkItemHandler wih = new TestWebServiceWorkItemHandler(kieSession, kieClassLoader);
		kieSession.getWorkItemManager().registerWorkItemHandler("WebService", wih);
		kieSession.startProcess("WebServiceIntegrationProcess.SecuredWebServiceProcess");
		kieSession.dispose();
	}

}
