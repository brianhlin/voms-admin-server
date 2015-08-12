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
package org.glite.security.voms.admin.view.actions.configuration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.util.URLBuilder;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@Result(name = BaseAction.SUCCESS, location = "configuration")
public class ConfigurationAction extends BaseAction implements Preparable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String vomsesConf;

  String mkGridmapConf;

  String contactString;

  String lsc;

  public void prepare() throws Exception {

    contactString = URLBuilder.baseVOMSURLFromConfiguration();

    vomsesConf = VOMSConfiguration.instance().getVomsesConfigurationString();

    HttpServletRequest request = ServletActionContext.getRequest();

    // Build mkgridmap configuration
    mkGridmapConf = "group vomss://" + request.getServerName() + ":"
      + request.getServerPort() + "/voms/"
      + VOMSConfiguration.instance().getVOName() + "   ."
      + VOMSConfiguration.instance().getVOName();

    lsc = VOMSConfiguration.instance().getLSCConfiguration();

  }

  public String getVomsesConf() {

    return vomsesConf;
  }

  public void setVomsesConf(String vomsesConf) {

    this.vomsesConf = vomsesConf;
  }

  public String getMkGridmapConf() {

    return mkGridmapConf;
  }

  public void setMkGridmapConf(String mkGridmapConf) {

    this.mkGridmapConf = mkGridmapConf;
  }

  public String getContactString() {

    return contactString;
  }

  public void setContactString(String contactString) {

    this.contactString = contactString;
  }

  public String getLSC() {

    return lsc;
  }

  public String getLsc() {

    return lsc;
  }

  public void setLsc(String lsc) {

    this.lsc = lsc;
  }

}
