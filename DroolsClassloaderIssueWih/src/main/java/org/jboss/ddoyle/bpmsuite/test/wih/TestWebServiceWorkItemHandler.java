/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.ddoyle.bpmsuite.test.wih;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWebServiceWorkItemHandler implements WorkItemHandler {
    
    private static Logger logger = LoggerFactory.getLogger(TestWebServiceWorkItemHandler.class);
    
    private KieSession ksession;
    private ClassLoader classLoader;
    
    public TestWebServiceWorkItemHandler(KieSession ksession) {
        this.ksession = ksession;
    }
    
    public TestWebServiceWorkItemHandler(KieSession ksession, ClassLoader classloader) {
    	this.ksession = ksession;
        this.classLoader = classloader;
    }
    
    public TestWebServiceWorkItemHandler(KieSession ksession, int timeout) {
        this.ksession = ksession;
    }

    public void executeWorkItem(WorkItem workItem, final WorkItemManager manager) {
    	
    	boolean foundIt = false;
    	
    	String resourceName = "cxf.xml";
    	
    	URL cxfResource = this.classLoader.getResource(resourceName);
    	
    	if (cxfResource != null) {
    		foundIt = true;
    		logger.warn("FOUND ITTTTT!!!!!");
    	}
    	URL cxfResourceSlash = this.classLoader.getResource("/" + resourceName);
    	if (cxfResourceSlash != null) {
    		foundIt = true;
    		logger.warn("FOUND ITTTTT!!!!!");
    	}
    	
    	if (!foundIt) {
    		logger.warn("No!!! We haven't found it.");
    	}
    	
    	Map<String, Object> results = new HashMap<>();
    	manager.completeWorkItem(workItem.getId(), results);
    }
    
   

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // Do nothing, cannot be aborted
    }
    
    private ClassLoader getInternalClassLoader() {
		if (this.classLoader != null) {
			return this.classLoader;
		}
		return Thread.currentThread().getContextClassLoader();
	}
    
    public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	protected String nonNull(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}
}
