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
package org.glite.security.voms.admin.view.actions.role;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.SingleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.roles.DeleteRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({

  @Result(name = BaseAction.SUCCESS, location = "/role/search.action",
    type = "redirect"),
  @Result(name = BaseAction.INPUT, location = "search", type = "chain") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class DeleteMultipleAction extends BaseAction {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  List<Long> roleIds;

  @Override
  public void validate() {

    if (roleIds == null)
      addActionError("No roles selected!");
    else if (roleIds.contains("ognl.NoConversionPossible"))
      addActionError("No roles selected!");

  }

  @Override
  public String execute() throws Exception {

    SingleArgumentOperationCollection<Long> deleteOps = new SingleArgumentOperationCollection<Long>(
      roleIds, DeleteRoleOperation.class);

    deleteOps.execute();

    return SUCCESS;
  }

  public List<Long> getRoleIds() {

    return roleIds;
  }

  public void setRoleIds(List<Long> roleIds) {

    this.roleIds = roleIds;
  }

}
