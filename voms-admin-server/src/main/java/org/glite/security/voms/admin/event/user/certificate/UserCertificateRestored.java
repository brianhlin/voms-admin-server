package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;


public class UserCertificateRestored extends UserCertificateEvent {

  public UserCertificateRestored(VOMSUser payload, Certificate certificate) {

    super(payload, certificate);

  }

}