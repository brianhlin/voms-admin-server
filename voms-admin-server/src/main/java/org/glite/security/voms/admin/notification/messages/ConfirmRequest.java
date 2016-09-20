/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.notification.messages;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;

public class ConfirmRequest extends AbstractVelocityNotification {

  private String confirmURL;
  private String cancelURL;

  public ConfirmRequest(String recipient, String confirmURL, String cancelURL) {

    addRecipient(recipient);
    this.confirmURL = confirmURL;
    this.cancelURL = cancelURL;
  }

  public void buildMessage() {

    VOMSConfiguration conf = VOMSConfiguration.instance();
    String voName = conf.getVOName();
    setSubject("Your membership request for VO " + voName);

    context.put("voName", voName);
    context.put("recipient", getRecipientList().get(0));
    context.put("confirmURL", confirmURL);
    context.put("cancelURL", cancelURL);

    super.buildMessage();

  }
}
