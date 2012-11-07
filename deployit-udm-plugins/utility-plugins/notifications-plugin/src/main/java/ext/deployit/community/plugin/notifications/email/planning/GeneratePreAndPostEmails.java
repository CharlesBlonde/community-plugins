/*
 * @(#)CheckRequiredChangeRequest.java     26 Aug 2011
 *
 * Copyright © 2010 Andrew Phillips.
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package ext.deployit.community.plugin.notifications.email.planning;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import com.xebialabs.deployit.plugin.api.deployment.execution.DeploymentStep;
import com.xebialabs.deployit.plugin.api.deployment.planning.DeploymentPlanningContext;
import com.xebialabs.deployit.plugin.api.deployment.planning.PostPlanProcessor;
import com.xebialabs.deployit.plugin.api.deployment.planning.PrePlanProcessor;
import com.xebialabs.deployit.plugin.api.deployment.planning.ReadOnlyRepository;
import com.xebialabs.deployit.plugin.api.deployment.specification.Delta;
import com.xebialabs.deployit.plugin.api.deployment.specification.DeltaSpecification;
import com.xebialabs.deployit.plugin.api.deployment.specification.Operation;
import com.xebialabs.deployit.plugin.api.flow.Step;
import com.xebialabs.deployit.plugin.api.reflect.Type;
import com.xebialabs.deployit.plugin.api.udm.Container;
import com.xebialabs.deployit.plugin.api.udm.DeployedApplication;
import com.xebialabs.deployit.plugin.api.udm.Environment;

import ext.deployit.community.plugin.notifications.email.ci.MailServer;
import ext.deployit.community.plugin.notifications.email.deployed.SentTemplateEmail0;
import ext.deployit.community.plugin.notifications.util.StepAdapter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Sets.filter;
import static com.xebialabs.deployit.plugin.api.reflect.DescriptorRegistry.getDescriptor;
import static ext.deployit.community.plugin.notifications.util.Predicates.subtypeOf;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;

public class GeneratePreAndPostEmails {
    private static final String ENV_REQUIRES_PRE_EMAIL = "sendDeploymentStartNotification";
    private static final String ENV_REQUIRES_POST_EMAIL = "sendDeploymentEndNotification";
    private static final Type PRE_EMAIL_TYPE = Type.valueOf("notify.DeploymentStartNotification"); 
    private static final Type POST_EMAIL_TYPE = Type.valueOf("notify.DeploymentEndNotification");
    private static final Type MAIL_SERVER_TYPE = Type.valueOf(MailServer.class);
    private static final List<Step> NO_STEPS = ImmutableList.of();

    @PrePlanProcessor
    public static List<Step> generatePreEmails(DeltaSpecification spec) {
        return generateEmails(spec.getDeployedApplication(), 
                ENV_REQUIRES_PRE_EMAIL, PRE_EMAIL_TYPE);
    }

    @PostPlanProcessor
    public static List<Step> generatePostEmails(DeltaSpecification spec) {
        return generateEmails(spec.getDeployedApplication(), 
                ENV_REQUIRES_POST_EMAIL, POST_EMAIL_TYPE);
    }
    
    protected static List<Step> generateEmails(DeployedApplication deployedApplication, 
            String triggerProperty, Type sentEmailType) {
        // property may also be null
        if (!TRUE.equals(deployedApplication.getEnvironment().getProperty(triggerProperty))) {
            return NO_STEPS;
        }

        StepCollector steps = new StepCollector();
        getDelegate(sentEmailType, deployedApplication).executeCreate(steps, null);
        return steps.steps;
    }
    
    protected static SentTemplateEmail0 getDelegate(Type sentEmailType,
            DeployedApplication deployedApplication) {
        SentTemplateEmail0 delegate = getDescriptor(sentEmailType).newInstance();
        delegate.setContainer(findMailServer(deployedApplication.getEnvironment()));
        delegate.setDeployedApplication(deployedApplication);
        return delegate;
    }

    private static MailServer findMailServer(Environment environment) {
        Set<Container> mailServers = filter(environment.getMembers(),
                new Predicate<Container>() {
                    @Override
                    public boolean apply(Container input) {
                        return subtypeOf(MAIL_SERVER_TYPE).apply(input.getType());
                    }
                });
        checkArgument(mailServers.size() == 1, "Cannot send pre- or post-deployment notification emails unless there is exactly 1 'notify.MailServer' in the target environment");
        return (MailServer) mailServers.iterator().next();
    }

    private static class StepCollector implements DeploymentPlanningContext {
        private final List<Step> steps = newLinkedList();
        
        @Override
        public void addStep(Step step) {
            steps.add(step);
        }

        @Override
        public void addSteps(Step... steps) {
            addSteps(asList(steps));
        }

        @Override
		public void addSteps(Iterable<Step> steps) {
        	for (Step step : steps) {
				addStep(step);
			}
		}

        @Override
        public Object getAttribute(String name) {
            throw new UnsupportedOperationException("TODO Auto-generated method stub");
        }

        @Override
        public void setAttribute(String name, Object value) {
            throw new UnsupportedOperationException("TODO Auto-generated method stub");
        }

        @Override
        public DeployedApplication getDeployedApplication() {
                throw new UnsupportedOperationException("TODO Auto-generated method stub");
        }

        @Override
        public ReadOnlyRepository getRepository() {
                throw new UnsupportedOperationException("TODO Auto-generated method stub");
        }

		@Override
		public void addStep(DeploymentStep step) {
			addStep(StepAdapter.wrapIfNeeded(step));
		}
		
		@Override
		public void addSteps(DeploymentStep... steps) {
			for (DeploymentStep step : steps) {
				addStep(step);
			}
		}

		@Override
		public void addSteps(Collection<DeploymentStep> steps) {
			for (DeploymentStep step : steps) {
				addStep(step);
			}
		}

        @Override
        public void addCheckpoint(Step arg0, Delta arg1) {
        }

        @Override
        public void addCheckpoint(Step arg0, Iterable<Delta> arg1) {
        }

        @Override
        public void addCheckpoint(Step arg0, Delta arg1, Operation arg2) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void addStepWithCheckpoint(Step arg0, Delta arg1) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void addStepWithCheckpoint(Step arg0, Iterable<Delta> arg1) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void addStepWithCheckpoint(Step arg0, Delta arg1, Operation arg2) {
            // TODO Auto-generated method stub
            
        }
        
    }
}

