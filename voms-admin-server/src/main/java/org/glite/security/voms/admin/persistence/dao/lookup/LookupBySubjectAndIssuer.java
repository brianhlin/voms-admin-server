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
package org.glite.security.voms.admin.persistence.dao.lookup;

/**
 * A lookup strategy that relies on {@link
 * FindByCertificateDAO#findBySubjectAndIssuer(String, String)} to resolve an
 * entity based on certificate lookup.
 *
 */
public class LookupBySubjectAndIssuer implements LookupStrategy {

  @Override
  public <T> T lookup(FindByCertificateDAO<T> dao, String certificateSubject,
    String certificateIssuer) {

    return dao.findBySubjectAndIssuer(certificateSubject, certificateIssuer);
  }

}
