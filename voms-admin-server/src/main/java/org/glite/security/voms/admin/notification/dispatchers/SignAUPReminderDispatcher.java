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

import java.util.Date;
import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskReminderEvent;
import org.glite.security.voms.admin.notification.NotificationServiceFactory;
import org.glite.security.voms.admin.notification.messages.SignAUPReminderMessage;

public class SignAUPReminderDispatcher
  extends AbstractEventLister<SignAUPTaskReminderEvent> {

  public static SignAUPReminderDispatcher instance() {

    return new SignAUPReminderDispatcher();
  }

  private SignAUPReminderDispatcher() {

    super(EnumSet.of(EventCategory.UserAUPEvent),
      SignAUPTaskReminderEvent.class);

  }

  @Override
  protected void doFire(SignAUPTaskReminderEvent e) {

    SignAUPReminderMessage msg = new SignAUPReminderMessage(e);
    NotificationServiceFactory.getNotificationService()
      .send(msg);

    e.getTask()
      .setLastNotificationTime(new Date());

  }

}
