/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/

package org.glite.security.voms.admin.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.SecurityContext;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.common.InitSecurityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.operations.CurrentAdmin;

/**
 * 
 * @author andrea
 * 
 */
public class SecurityContextFilter implements Filter {

	protected Log log = LogFactory.getLog(SecurityContextFilter.class);

	public SecurityContextFilter() {

		super();
		// TODO Auto-generated constructor stub
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		InitSecurityContext.setContextFromRequest(req);
		SecurityContext theContext = SecurityContext.getCurrentContext();

		String clientDN = theContext.getClientName();
		String issuer = theContext.getIssuerName();

		String voName = VOMSConfiguration.instance().getVOName();
		req.setAttribute("voName", voName);
		req.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_KEY, CurrentAdmin
				.instance());

		chain.doFilter(req, res);

	}

	public void destroy() {

	}

}
