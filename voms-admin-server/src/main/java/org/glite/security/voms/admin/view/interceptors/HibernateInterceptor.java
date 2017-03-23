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
package org.glite.security.voms.admin.view.interceptors;

import org.apache.struts2.StrutsStatics;
import org.glite.security.voms.admin.persistence.HibernateFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class HibernateInterceptor extends AbstractInterceptor implements
  StrutsStatics {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  
  
  @Override
  public String intercept(ActionInvocation ai) throws Exception {

    String result = ai.invoke();
    
    try {

      HibernateFactory.commitTransaction();

    } finally {

      HibernateFactory.closeSession();

    }

    return result;

  }

}
