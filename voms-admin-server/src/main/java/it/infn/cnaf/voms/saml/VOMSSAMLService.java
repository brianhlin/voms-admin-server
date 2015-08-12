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
/**************************************************************************

 Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Authors: 	Valerio Venturi <valerio.venturi@cnaf.infn.it>
 Andrea Ceccanti <andrea.ceccanti@cnaf.infn.it>

 **************************************************************************/

package it.infn.cnaf.voms.saml;

import it.infn.cnaf.voms.saml.exceptions.IssuerPeerMismatchException;
import it.infn.cnaf.voms.saml.exceptions.UnauthorizedQueryException;
import it.infn.cnaf.voms.saml.exceptions.UnknownAttributeException;
import it.infn.cnaf.voms.saml.exceptions.UnsupportedQueryException;
import it.infn.cnaf.voms.saml.exceptions.VersionMismatchException;
import it.infn.cnaf.voms.saml.exceptions.X509SubjectWrongNameIDFormatException;
import it.infn.cnaf.voms.saml.exceptions.X509SubjectWrongNameIDValueException;

import java.rmi.RemoteException;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;

import org.glite.security.voms.admin.util.DNUtil;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Valerio Venturi (valerio.venturi@cnaf.infn.it)
 * @author Andrea Ceccanti (andrea.ceccanti@cnaf.infn.it)
 * 
 */
public class VOMSSAMLService {

  public static final String VOMS_SAML_FQAN_URI = "http://voms.forge.cnaf.infn.it/fqan";

  static private Logger logger = LoggerFactory.getLogger(VOMSSAMLService.class);

  private SAMLAssertionFactory sAMLAssertionFactory;

  private SAMLResponseFactory sAMLResponseFactory;

  private int maxAssertionLifetime;

  public VOMSSAMLService(SAMLAssertionFactory sAMLAssertionFactory,
    SAMLResponseFactory sAMLResponseFactory, int maxAssertionLifetime) {

    this.sAMLAssertionFactory = sAMLAssertionFactory;
    this.sAMLResponseFactory = sAMLResponseFactory;
    this.maxAssertionLifetime = maxAssertionLifetime;
  }

  public Response attributeQuery(AttributeQuery attributeQuery,
    HttpServletRequest httpServletRequest) throws RemoteException {

    try {

      // get the peer security context
      SecurityContextHelper peerSecurityContext = new SecurityContextHelper(
        httpServletRequest);

      checkAttributeQuery(attributeQuery, peerSecurityContext);

      /* prepare the Assertion */
      Assertion assertion = sAMLAssertionFactory.create(
        peerSecurityContext.getCertificate(), attributeQuery,
        maxAssertionLifetime);

      /* prepare the Response and return it */
      return sAMLResponseFactory.create(attributeQuery.getID(), assertion);

    } catch (Throwable exception) {
      logger.error(exception.getMessage());
      return sAMLResponseFactory.create(attributeQuery.getID(), exception);
    }
  }

  private void checkAttributeQuery(AttributeQuery attributeQuery,
    SecurityContextHelper peerSecurityContext) throws VersionMismatchException,
    X509SubjectWrongNameIDFormatException,
    X509SubjectWrongNameIDValueException, IssuerPeerMismatchException,
    UnauthorizedQueryException, UnsupportedQueryException,
    UnknownAttributeException {

    // check Version attribute for AttributeQuery is 2.0
    if (attributeQuery.getVersion() != SAMLVersion.VERSION_20)
      throw new VersionMismatchException();

    String subjectNameIDFormat = attributeQuery.getSubject().getNameID()
      .getFormat();

    // check the Format attribute of NameID in Subject conforme to SAML X509
    // Profile
    if (!subjectNameIDFormat.equals(NameID.X509_SUBJECT))
      throw new X509SubjectWrongNameIDFormatException(subjectNameIDFormat);

    String subjectNameIDValue = attributeQuery.getSubject().getNameID()
      .getValue();

    // check the distinguished name in Subject conform to RFC 2253
    if (!DNUtil.isRFC2253Conformant(subjectNameIDValue))
      throw new X509SubjectWrongNameIDValueException(subjectNameIDValue);

    X500Principal subject = new X500Principal(subjectNameIDValue);

    X500Principal issuer = new X500Principal(attributeQuery.getIssuer()
      .getValue());

    logger.debug("Received AttributeQuery issued by " + issuer.getName()
      + " for subject " + subjectNameIDValue);

    // check the peer identity corresponds to the issuer of the
    // AttributeQuery
    // if ( !peerSecurityContext.is( issuer ) )
    // throw new IssuerPeerMismatchException( issuer.getName(),
    // peerSecurityContext.getX500Principal().getName() );

    // Check the AttributeQuery is authorized
    // if ( !peerSecurityContext.is( subject ) )
    // throw new UnauthorizedQueryException( issuer.getName(), subject
    // .getName() );

  }
}