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
package org.glite.security.voms.admin.view.actions.register;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.INPUT, location = "requestAttributes"),
  @Result(name = BaseAction.SUCCESS, type = "chain",
    location = "set-request-confirmed"),
  @Result(name = BaseAction.ERROR, location = "registrationConfirmationError") })
public class RequestAttributesAction extends RegisterActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String confirmationId;

  List<String> requestedGroups;

  @Override
  public String execute() throws Exception {

    if (!registrationEnabled())
      return REGISTRATION_DISABLED;

    if (!getModel().getStatus().equals(STATUS.SUBMITTED)) {
      addActionError("Your request has already been confirmed!");
      return ERROR;
    }

    if (!getModel().getConfirmId().equals(confirmationId)) {
      addActionError("Wrong confirmation id!");
      return ERROR;
    }

    if (requestedGroups != null && !requestedGroups.isEmpty()) {
      Integer requestedGroupsSize = requestedGroups.size();

      getModel().getRequesterInfo().addInfo(
        RequesterInfo.MULTIVALUE_COUNT_PREFIX + "requestedGroup",
        requestedGroupsSize.toString());

      for (int i = 0; i < requestedGroupsSize; i++)
        getModel().getRequesterInfo().addInfo("requestedGroup" + i,
          requestedGroups.get(i));

    }

    return SUCCESS;
  }

  @RequiredFieldValidator(type = ValidatorType.FIELD,
    message = "A confirmation id is required!")
  public String getConfirmationId() {

    return confirmationId;
  }

  public void setConfirmationId(String confirmationId) {

    this.confirmationId = confirmationId;
  }

  public List<String> getRequestedGroups() {

    return requestedGroups;
  }

  public void setRequestedGroups(List<String> requestedGroups) {

    this.requestedGroups = requestedGroups;
  }

}
