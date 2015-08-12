/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public abstract class BaseUserAdministrativeOperation extends BaseVomsOperation {

  public static final Logger log = LoggerFactory
    .getLogger(BaseUserAdministrativeOperation.class);

  protected VOMSUser authorizedUser;

  @Override
  final protected AuthorizationResponse isAllowed() {

    CurrentAdmin admin = CurrentAdmin.instance();

    if (!admin.isVoUser()) {
      log
        .debug("Current admin has no corresponding VO user, falling back to base authorization method.");
      return super.isAllowed();
    }

    boolean usersMatch = admin.getVoUser().equals(authorizedUser);
    log.debug("Admin's user match with authorized user: " + usersMatch);

    if (usersMatch) {
      return AuthorizationResponse.permit();
    }

    return super.isAllowed();

  }

  public VOMSUser getAuthorizedUser() {

    return authorizedUser;
  }

  public void setAuthorizedUser(VOMSUser authorizedUser) {

    this.authorizedUser = authorizedUser;
  }

}
