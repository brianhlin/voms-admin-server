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

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = BaseAction.INPUT, location = "register"),
  @Result(name = BaseAction.SUCCESS, location = "registerConfirmation") })
public abstract class RegisterActionSupport extends BaseAction implements
  Preparable, ModelDriven<NewVOMembershipRequest> {

  public static final String CONFIRMATION_NEEDED = "needToConfirm";
  public static final String PLEASE_WAIT = "pleaseWait";
  public static final String ALREADY_MEMBER = "alreadyAMember";

  public static final String REGISTRATION_DISABLED = "registrationDisabled";
  public static final String UNAUTHENTICATED_CLIENT = "unauthenticatedClient";
  public static final String PLUGIN_VALIDATION_ERROR = "pluginValidationError";

  public static final String REQUEST_ATTRIBUTES = "requestAttributes";
  public static final String SELECT_MANAGER = "selectManager";

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long requestId = -1L;

  protected NewVOMembershipRequest request;

  protected RequesterInfo requester;

  AUPVersion currentAUPVersion;

  protected String checkExistingPendingRequests() {

    RequestDAO dao = DAOFactory.instance().getRequestDAO();

    NewVOMembershipRequest req = dao.findActiveVOMembershipRequest(requester);

    if (req != null) {

      request = req;
      if (req.getStatus().equals(STATUS.SUBMITTED))
        return CONFIRMATION_NEEDED;

      if (req.getStatus().equals(STATUS.CONFIRMED)
        || req.getStatus().equals(STATUS.PENDING))
        return PLEASE_WAIT;

    }
    return null;
  }

  protected RequesterInfo requesterInfoFromCurrentAdmin() {

    RequesterInfo i = new RequesterInfo();
    CurrentAdmin admin = CurrentAdmin.instance();
    i.setCertificateSubject(admin.getRealSubject());
    i.setCertificateIssuer(admin.getRealIssuer());
    i.setEmailAddress(admin.getRealEmailAddress());

    return i;

  }

  public void prepare() throws Exception {

    requester = requesterInfoFromCurrentAdmin();

    AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

    currentAUPVersion = aupDAO.getVOAUP().getActiveVersion();

    if (getModel() == null) {

      if (requestId != -1L) {
        RequestDAO dao = DAOFactory.instance().getRequestDAO();
        request = (NewVOMembershipRequest) dao.findById(requestId, true);
      }
    }

  }

  public NewVOMembershipRequest getModel() {

    return request;
  }

  public Long getRequestId() {

    return requestId;
  }

  public void setRequestId(Long requestId) {

    this.requestId = requestId;
  }

  public RequesterInfo getRequester() {

    return requester;
  }

  public void setRequester(RequesterInfo requester) {

    this.requester = requester;
  }

  public boolean registrationEnabled() {

    return VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true);
  }

  public boolean attributeRequestsEnabled() {

    return VOMSConfiguration.instance()
      .getBoolean(
        VOMSConfigurationConstants.VO_MEMBERSHIP_ENABLE_ATTRIBUTES_REQUEST,
        false);

  }

  public boolean requireGroupManagerSelection() {

    return VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.VO_MEMBERSHIP_REQUIRE_GROUP_MANAGER_SELECTION,
      true);
  }

  public AUPVersion getCurrentAUPVersion() {

    return currentAUPVersion;
  }

  public String getRegistrationType() {

    return VOMSConfiguration.instance().getRegistrationType();
  }

}
