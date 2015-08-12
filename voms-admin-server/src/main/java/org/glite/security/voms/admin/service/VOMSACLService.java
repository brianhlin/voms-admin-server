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
package org.glite.security.voms.admin.service;

import java.rmi.RemoteException;

import org.apache.commons.lang.StringUtils;
import org.glite.security.voms.VOMSException;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.acls.DeleteACLEntryOperation;
import org.glite.security.voms.admin.operations.acls.LoadACLOperation;
import org.glite.security.voms.admin.operations.acls.SaveACLEntryOperation;
import org.glite.security.voms.admin.operations.acls.SetACLOperation;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.ACLDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchACLException;
import org.glite.security.voms.admin.persistence.error.NoSuchAdminException;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.glite.security.voms.service.acl.ACLEntry;
import org.glite.security.voms.service.acl.VOMSACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSACLService implements VOMSACL {

  private static final Logger log = LoggerFactory
    .getLogger(VOMSACLService.class);

  public void addACLEntry(String container, ACLEntry entry,
    boolean propagateToChildrenContexts) throws RemoteException, VOMSException {

    log.info("addACLEntry('container=" + container + "', entry='" + entry
      + "');");

    if (container == null)
      throw new NullArgumentException("Container cannot be null!");

    if (entry == null)
      throw new NullArgumentException("Container cannot be null!");

    try {

      ACL theACL = (ACL) LoadACLOperation.instance(container).execute();

      VOMSAdmin theAdmin = getAdminFromEntry(entry);

      if (theAdmin == null)
        throw new NoSuchAdminException("Unknown or illegal admin! '"
          + entry.getAdminSubject() + "," + entry.getAdminIssuer() + "'");

      VOMSPermission perms = VOMSPermission.fromBits(entry
        .getVomsPermissionBits());

      ServiceUtils.limitUnauthenticatedClientPermissions(theAdmin, perms);

      SaveACLEntryOperation.instance(theACL, theAdmin, perms,
        propagateToChildrenContexts).execute();

      HibernateFactory.commitTransaction();

    } catch (RuntimeException e) {

      ServiceExceptionHelper.handleServiceException(log, e);
      throw e;

    }

  }

  public void addDefaultACLEntry(String group, ACLEntry entry)
    throws RemoteException, VOMSException {

    log.info("addDefaultACLEntry("
      + StringUtils.join(new Object[] { group, entry }, ',') + ");");

    if (group == null)
      throw new NullArgumentException("group cannot be null!");

    if (entry == null)
      throw new NullArgumentException("entry cannot be null!");

    VOMSGroup g = null;

    try {

      g = (VOMSGroup) FindGroupOperation.instance(group).execute();

      if (g == null)
        throw new NoSuchGroupException("Group '" + g
          + "' not found in database!");

      ACL acl = g.getDefaultACL();

      if (acl == null)
        acl = ACLDAO.instance().create(g, true);

      VOMSAdmin theAdmin = getAdminFromEntry(entry);

      VOMSPermission perms = VOMSPermission.fromBits(entry
        .getVomsPermissionBits());

      ServiceUtils.limitUnauthenticatedClientPermissions(theAdmin, perms);

      SaveACLEntryOperation.instance(acl, theAdmin, perms).execute();

      // Commit hibernate transaction
      HibernateFactory.commitTransaction();

    } catch (RuntimeException e) {

      // Clean up the just created ACL, if that's the case
      if (g != null)
        if (g.getDefaultACL() != null
          && g.getDefaultACL().getPermissions().isEmpty())
          ACLDAO.instance().delete(g.getDefaultACL());

      ServiceExceptionHelper.handleServiceException(log, e);

    }
  }

  protected ACLEntry[] getACLImpl(String container, boolean defaultACL)
    throws VOMSException {

    ACL acl = (ACL) LoadACLOperation.instance(container, defaultACL).execute();
    return ServiceUtils.toACLEntryArray(acl);

  }

  public ACLEntry[] getACL(String container) throws RemoteException,
    VOMSException {

    log.info("getACL(" + container + ");");

    if (container == null)
      throw new NullArgumentException("container cannot be null!");

    try {

      ACLEntry[] retVal = getACLImpl(container, false);
      HibernateFactory.commitTransaction();

      return retVal;

    } catch (RuntimeException e) {

      log.error(e.toString());

      if (log.isDebugEnabled()) {
        log.error(e.getMessage(), e);
      }

      throw e;
    }

  }

  public ACLEntry[] getDefaultACL(String container) throws RemoteException,
    VOMSException {

    log.info("getDefaultACL(" + container + ");");
    if (container == null)
      throw new NullArgumentException("container cannot be null!");

    try {

      ACLEntry[] retVal = getACLImpl(container, true);
      HibernateFactory.commitTransaction();

      return retVal;

    } catch (RuntimeException e) {

      log.error(e.toString());

      if (log.isDebugEnabled()) {
        log.error(e.getMessage(), e);
      }

      throw e;
    }
  }

  public void removeACLEntry(String container, ACLEntry entry,
    boolean removeFromChildrenContexts) throws RemoteException, VOMSException {

    log.info("removeACLEntry("
      + StringUtils.join(new Object[] { container, entry,
        removeFromChildrenContexts }, ',') + ");");

    if (container == null)
      throw new NullArgumentException("container cannot be null!");

    if (entry == null)
      throw new NullArgumentException("entry cannot be null!");

    try {

      ACL theACL = (ACL) LoadACLOperation.instance(container).execute();
      VOMSAdmin admin = getAdminFromEntry(entry);

      DeleteACLEntryOperation.instance(theACL, admin,
        removeFromChildrenContexts).execute();
      // Commit hibernate transaction
      HibernateFactory.commitTransaction();

    } catch (RuntimeException e) {

      ServiceExceptionHelper.handleServiceException(log, e);

    }

  }

  public void removeDefaultACLEntry(String group, ACLEntry entry)
    throws RemoteException, VOMSException {

    log.info("removeDefaultACLEntry("
      + StringUtils.join(new Object[] { group, entry }, ',') + ");");

    if (group == null)
      throw new NullArgumentException("group cannot be null!");

    if (entry == null)
      throw new NullArgumentException("entry cannot be null!");

    try {

      ACL theACL = (ACL) LoadACLOperation.instance(group, true).execute();

      if (theACL == null)
        throw new NoSuchACLException("Default ACL is not defined for group "
          + group);

      VOMSAdmin admin = getAdminFromEntry(entry);

      DeleteACLEntryOperation.instance(theACL, admin).execute();

      // Commit hibernate transaction
      HibernateFactory.commitTransaction();

    } catch (RuntimeException e) {

      ServiceExceptionHelper.handleServiceException(log, e);

    }

  }

  protected void setACLImpl(String container, boolean defaultACL,
    ACLEntry[] entries) throws VOMSException {

    if (container == null)
      throw new NullArgumentException("container cannot be null!");

    if (entries == null)
      throw new NullArgumentException("entries cannot be null!");

    VOMSGroup g = null;

    try {

      ACL theACL = (ACL) LoadACLOperation.instance(container, defaultACL)
        .execute();

      if (theACL == null && defaultACL) {

        g = VOMSGroupDAO.instance().findByName(container);
        theACL = ACLDAO.instance().create(g, true);
      }

      SetACLOperation.instance(theACL, ServiceUtils.toPermissionMap(entries))
        .execute();

      // Commit hibernate transaction
      HibernateFactory.commitTransaction();

    } catch (RuntimeException e) {

      // Clean up the just created ACL, if that's the case
      if (g != null)
        if (g.getDefaultACL() != null
          && g.getDefaultACL().getPermissions().isEmpty())
          ACLDAO.instance().delete(g.getDefaultACL());

      ServiceExceptionHelper.handleServiceException(log, e);
    }

  }

  public void setACL(String container, ACLEntry[] entries)
    throws RemoteException, VOMSException {

    log.info("setACL("
      + StringUtils.join(new Object[] { container, entries }, ',') + ");");

    setACLImpl(container, false, entries);

  }

  public void setDefaultACL(String group, ACLEntry[] entries)
    throws RemoteException, VOMSException {

    log.info("setDefaultACL("
      + StringUtils.join(new Object[] { group, entries }, ',') + ");");

    setACLImpl(group, true, entries);

  }

  protected VOMSAdmin getAdminFromEntry(ACLEntry entry) {

    if (entry.getAdminSubject() == null)
      throw new NullArgumentException("entry.adminSubject cannot be null!");

    if (entry.getAdminIssuer() == null)
      throw new NullArgumentException("entry.adminIssuer cannot be null!");

    String subject = entry.getAdminSubject();
    String issuer = entry.getAdminIssuer();

    VOMSAdmin admin = VOMSAdminDAO.instance().getByName(subject, issuer);

    if (admin == null) {

      // Admin not found, check if internal admin is requested
      if (issuer.equals(VOMSServiceConstants.VIRTUAL_CA)) {

        // ANYUSER admin
        return VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin();

      } else if (issuer.equals(VOMSServiceConstants.GROUP_CA)) {

        VOMSGroup g = VOMSGroupDAO.instance().findByName(subject);

        if (g == null)
          throw new NoSuchGroupException("Group '" + subject
            + "' is not defined in database!");

        return VOMSAdminDAO.instance().create(subject);

      } else if (issuer.equals(VOMSServiceConstants.ROLE_CA)) {

        String groupName = PathNamingScheme.getGroupName(subject);
        String roleName = PathNamingScheme.getRoleName(subject);

        VOMSGroup g = VOMSGroupDAO.instance().findByName(groupName);
        VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);

        if (g == null)
          throw new NoSuchGroupException("Group '" + groupName
            + "' is not defined in database!");

        if (r == null)
          throw new NoSuchRoleException("Role '" + roleName
            + "' is not defined in database!");

        return VOMSAdminDAO.instance().create(subject);
      }

      // Another type of admin
      return VOMSAdminDAO.instance().create(subject, issuer);

    }

    return admin;

  }
}
