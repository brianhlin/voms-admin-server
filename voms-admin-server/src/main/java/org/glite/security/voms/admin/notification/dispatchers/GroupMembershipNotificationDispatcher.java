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
package org.glite.security.voms.admin.notification.dispatchers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.request.GroupMembershipApprovedEvent;
import org.glite.security.voms.admin.event.request.GroupMembershipRejectedEvent;
import org.glite.security.voms.admin.event.request.GroupMembershipRequestEvent;
import org.glite.security.voms.admin.event.request.GroupMembershipSubmittedEvent;
import org.glite.security.voms.admin.notification.BaseNotificationDispatcher;
import org.glite.security.voms.admin.notification.NotificationServiceFactory;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;

public class GroupMembershipNotificationDispatcher
  extends BaseNotificationDispatcher {

  private static GroupMembershipNotificationDispatcher instance = null;

  public static GroupMembershipNotificationDispatcher instance() {

    if (instance == null)
      instance = new GroupMembershipNotificationDispatcher();

    return instance;
  }

  private GroupMembershipNotificationDispatcher() {

    super(EnumSet.of(EventCategory.GroupMembershipRequestEvent));
  }

  public void fire(Event event) {

    GroupMembershipRequestEvent e = (GroupMembershipRequestEvent) event;

    GroupMembershipRequest req = e.getPayload();

    if (e instanceof GroupMembershipSubmittedEvent) {

      GroupMembershipSubmittedEvent ee = (GroupMembershipSubmittedEvent) e;

      VOMSContext context = VOMSContext.instance(ee.getPayload()
        .getGroupName());

      List<String> admins = new ArrayList<String>();

      VOMSRole groupManagerRole = VOMSRoleDAO.instance()
        .findByName(VOMSConfiguration.instance()
          .getGroupManagerRoleName());

      if (groupManagerRole != null) {

        Set<String> groupManagersRoleEmailAddresses = groupManagerRole
          .getMembersEmailAddresses(context.getGroup());

        if (!groupManagersRoleEmailAddresses.isEmpty()) {
          admins.addAll(groupManagersRoleEmailAddresses);
        }
      }

      if (admins.isEmpty()) {

        if (context.getGroup()
          .getManagers()
          .size() != 0) {
          for (GroupManager gm : context.getGroup()
            .getManagers()) {
            admins.add(gm.getEmailAddress());
          }
        } else {
          admins = NotificationUtil.getAdministratorsEmailList(context,
            VOMSPermission.getRequestsRWPermissions());
        }
      }

      HandleRequest msg = new HandleRequest(req, ee.getManagementURL(), admins);

      NotificationServiceFactory.getNotificationService()
        .send(msg);

    }

    if (e instanceof GroupMembershipApprovedEvent) {

      RequestApproved msg = new RequestApproved(req);
      NotificationServiceFactory.getNotificationService()
        .send(msg);

    }

    if (e instanceof GroupMembershipRejectedEvent) {

      RequestRejected msg = new RequestRejected(req, null);

      NotificationServiceFactory.getNotificationService()
        .send(msg);
    }
  }
}
