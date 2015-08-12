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
package org.glite.security.voms.admin.view.actions.acl;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.ACLDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public abstract class ACLActionSupport extends BaseAction implements
  Preparable, ModelDriven<ACL> {

  public static final Logger log = LoggerFactory
    .getLogger(ACLActionSupport.class);

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long aclId = -1L;
  Long adminId = -1L;

  ACL model;
  VOMSAdmin admin;

  protected Boolean propagate;

  public void prepare() throws Exception {

    if (getModel() == null) {

      if (getAclId() != -1)
        model = ACLDAO.instance().getById(getAclId());

      if (getAdminId() != -1)
        admin = VOMSAdminDAO.instance().getById(getAdminId());

    }

  }

  public ACL getModel() {

    return model;
  }

  public Long getAclId() {

    return aclId;
  }

  public void setAclId(Long aclId) {

    this.aclId = aclId;
  }

  public Long getAdminId() {

    return adminId;
  }

  public void setAdminId(Long adminId) {

    this.adminId = adminId;
  }

  public VOMSAdmin getAdmin() {

    return admin;
  }

  public void setAdmin(VOMSAdmin admin) {

    this.admin = admin;
  }

  public Boolean getPropagate() {

    return propagate;
  }

  public void setPropagate(Boolean propagate) {

    this.propagate = propagate;
  }

  protected void limitUnauthenticatedClientPermissions(VOMSPermission perms) {

    // Implement limiting on permissions for unauthenticated clients
    if (admin.isUnauthenticated()) {
      VOMSPermission permMask = VOMSConfiguration.instance()
        .getUnauthenticatedClientPermissionMask();
      addActionMessage("Unauthenticated client permissions limited to: '"
        + permMask + "' for this context!");
      perms.limitToPermissions(permMask);
    }
  }

}
