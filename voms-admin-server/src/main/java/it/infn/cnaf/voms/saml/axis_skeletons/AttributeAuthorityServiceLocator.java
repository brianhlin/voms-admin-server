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
/**
 * AttributeAuthorityServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.infn.cnaf.voms.saml.axis_skeletons;

import javax.xml.rpc.Stub;

import org.apache.axis.transport.http.HTTPConstants;
import org.glite.security.voms.admin.service.CSRFGuardHandler;

public class AttributeAuthorityServiceLocator extends
  org.apache.axis.client.Service implements
  it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityService {

  public AttributeAuthorityServiceLocator() {

  }

  public AttributeAuthorityServiceLocator(
    org.apache.axis.EngineConfiguration config) {

    super(config);
  }

  public AttributeAuthorityServiceLocator(java.lang.String wsdlLoc,
    javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {

    super(wsdlLoc, sName);
  }

  // Use to get a proxy class for AttributeAuthorityPortType
  private java.lang.String AttributeAuthorityPortType_address = "http://voms.cnaf.infn.it";

  public java.lang.String getAttributeAuthorityPortTypeAddress() {

    return AttributeAuthorityPortType_address;
  }

  // The WSDD service name defaults to the port name.
  private java.lang.String AttributeAuthorityPortTypeWSDDServiceName = "AttributeAuthorityPortType";

  public java.lang.String getAttributeAuthorityPortTypeWSDDServiceName() {

    return AttributeAuthorityPortTypeWSDDServiceName;
  }

  public void setAttributeAuthorityPortTypeWSDDServiceName(java.lang.String name) {

    AttributeAuthorityPortTypeWSDDServiceName = name;
  }

  public it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType getAttributeAuthorityPortType()
    throws javax.xml.rpc.ServiceException {

    java.net.URL endpoint;
    try {
      endpoint = new java.net.URL(AttributeAuthorityPortType_address);
    } catch (java.net.MalformedURLException e) {
      throw new javax.xml.rpc.ServiceException(e);
    }
    return getAttributeAuthorityPortType(endpoint);
  }

  public it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType getAttributeAuthorityPortType(
    java.net.URL portAddress) throws javax.xml.rpc.ServiceException {

    try {
      it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthoritySoapBindingStub _stub = new it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthoritySoapBindingStub(
        portAddress, this);
      _stub.setPortName(getAttributeAuthorityPortTypeWSDDServiceName());
      return _stub;
    } catch (org.apache.axis.AxisFault e) {
      return null;
    }
  }

  public void setAttributeAuthorityPortTypeEndpointAddress(
    java.lang.String address) {

    AttributeAuthorityPortType_address = address;
  }

  /**
   * For the given interface, get the stub implementation. If this service has
   * no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(Class serviceEndpointInterface)
    throws javax.xml.rpc.ServiceException {

    try {
      if (it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType.class
        .isAssignableFrom(serviceEndpointInterface)) {
        it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthoritySoapBindingStub _stub = new it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthoritySoapBindingStub(
          new java.net.URL(AttributeAuthorityPortType_address), this);
        _stub.setPortName(getAttributeAuthorityPortTypeWSDDServiceName());
        return _stub;
      }
    } catch (java.lang.Throwable t) {
      throw new javax.xml.rpc.ServiceException(t);
    }
    throw new javax.xml.rpc.ServiceException(
      "There is no stub implementation for the interface:  "
        + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface
          .getName()));
  }

  /**
   * For the given interface, get the stub implementation. If this service has
   * no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(javax.xml.namespace.QName portName,
    Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {

    if (portName == null) {
      return getPort(serviceEndpointInterface);
    }
    java.lang.String inputPortName = portName.getLocalPart();
    if ("AttributeAuthorityPortType".equals(inputPortName)) {
      return getAttributeAuthorityPortType();
    } else {
      java.rmi.Remote _stub = getPort(serviceEndpointInterface);
      ((org.apache.axis.client.Stub) _stub).setPortName(portName);
      return _stub;
    }
  }

  public javax.xml.namespace.QName getServiceName() {

    return new javax.xml.namespace.QName("http://voms.cnaf.infn.it",
      "AttributeAuthorityService");
  }

  private java.util.HashSet ports = null;

  public java.util.Iterator getPorts() {

    if (ports == null) {
      ports = new java.util.HashSet();
      ports.add(new javax.xml.namespace.QName("http://voms.cnaf.infn.it",
        "AttributeAuthorityPortType"));
    }
    return ports.iterator();
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(java.lang.String portName,
    java.lang.String address) throws javax.xml.rpc.ServiceException {

    if ("AttributeAuthorityPortType".equals(portName)) {
      setAttributeAuthorityPortTypeEndpointAddress(address);
    } else { // Unknown Port Name
      throw new javax.xml.rpc.ServiceException(
        " Cannot set Endpoint Address for Unknown Port" + portName);
    }
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(javax.xml.namespace.QName portName,
    java.lang.String address) throws javax.xml.rpc.ServiceException {

    setEndpointAddress(portName.getLocalPart(), address);
  }

}
