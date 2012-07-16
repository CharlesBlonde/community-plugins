/*
 * @(#)WebContent.java     18 Aug 2011
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
package ext.deployit.community.plugin.notifications.email.deployed;

import com.xebialabs.deployit.plugin.api.udm.Metadata;

import ext.deployit.community.plugin.notifications.email.ci.MailServer;
import ext.deployit.community.plugin.notifications.email.step.EmailSendStep;
import ext.deployit.community.plugin.notifications.email.step.TemplateEmailSendStep;

@SuppressWarnings("serial")
@Metadata(virtual = true, description = "A template email sent via a notify.MailServer")
public class SentTemplateEmail0 extends SentEmail {

    @Override
    protected EmailSendStep getEmailSendStep() {
         return new TemplateEmailSendStep(getCreateOrder(), getDescription(getCreateVerb()), 
                (MailServer) getContainer(), getFromAddress(), getToAddresses(), 
                getCcAddresses(), getBccAddresses(), getSubject(), 
                getDeployedAsFreeMarkerContext(), getTemplate());
    }
}
