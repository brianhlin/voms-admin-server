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
package org.glite.security.voms.admin.operations.aup;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.aup.UserSignedAUPEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class CreateAcceptanceRecordOperation extends BaseVomsOperation {

  AUP aup;
  VOMSUser user;

  public CreateAcceptanceRecordOperation(AUP aup, VOMSUser user) {
    Validate.notNull(aup, "aup must be non-null");
    Validate.notNull(user, "user must be non-null");

    this.aup = aup;
    this.user = user;

  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission()
      .setSuspendPermission());

  }

  @Override
  protected Object doExecute() {

    if (aup == null)
      throw new IllegalArgumentException("AUP cannot be null!");

    if (user == null)
      throw new IllegalArgumentException("User cannot be null!");

    VOMSUserDAO.instance().signAUP(user, aup);

    EventManager.instance().dispatch(new UserSignedAUPEvent(user, aup));

    return user;

  }

  
  public static CreateAcceptanceRecordOperation instance(Long userId){
    VOMSUser user = VOMSUserDAO.instance().findById(userId);
    AUP aup = DAOFactory.instance().getAUPDAO().getVOAUP();
    return new CreateAcceptanceRecordOperation(aup, user);
  }
}
