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
package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.Collections;
import java.util.List;

import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class IgnoreMembershipEndTimeStrategy implements
  HandleExpiredMembersStrategy, ExpiredMembersLookupStrategy,
  ExpiringMembersLookupStrategy, HandleExpiringMembersStrategy {

  public List<VOMSUser> findExpiredMembers() {

    return Collections.EMPTY_LIST;
  }

  public void handleExpiredMembers(List<VOMSUser> expiredMembers) {

    // Do NOTHING
  }

  public void handleMembersAboutToExpire(List<VOMSUser> expiringMembers) {

    // Do NOTHING
  }

  public List<VOMSUser> findExpiringMembers() {

    return Collections.EMPTY_LIST;
  }

}
