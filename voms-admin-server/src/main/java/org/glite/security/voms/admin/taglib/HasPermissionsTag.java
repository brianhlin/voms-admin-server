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
package org.glite.security.voms.admin.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class HasPermissionsTag extends TagSupport implements AuthorizableTag {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Map permissionMap = new HashMap();

  String context;
  String permission;
  String var;

  public int doStartTag() throws JspException {

    if (context != null && permission != null) {
      boolean result = TagUtils.isAuthorized(pageContext, context, permission);
      pageContext.setAttribute(var, new Boolean(result));
      return SKIP_BODY;
    }

    return EVAL_BODY_INCLUDE;
  }

  public int doEndTag() throws JspException {

    if (context != null && permission != null)
      return EVAL_PAGE;

    boolean result = TagUtils.hasPermissions(pageContext, permissionMap);

    pageContext.setAttribute(var, new Boolean(result));
    return EVAL_PAGE;

  }

  public String getContext() {

    return context;
  }

  public void setContext(String context) {

    this.context = context;
  }

  public String getPermission() {

    return permission;
  }

  public void setPermission(String permissions) {

    this.permission = permissions;
  }

  public String getVar() {

    return var;
  }

  public void setVar(String var) {

    this.var = var;
  }

  public Map getPermissionMap() {

    return permissionMap;
  }

}
