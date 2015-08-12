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
package org.glite.security.voms.admin.persistence.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AUPAcceptanceRecord implements Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  Long id;

  AUPVersion aupVersion;

  VOMSUser user;

  Date lastAcceptanceDate;

  Boolean valid;

  public AUPAcceptanceRecord(VOMSUser u, AUPVersion aup) {

    this.user = u;
    this.aupVersion = aup;
    this.valid = true;
  }

  public AUPAcceptanceRecord() {

    // TODO Auto-generated constructor stub
  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof AUPAcceptanceRecord))
      return false;

    if (other == null)
      return false;

    AUPAcceptanceRecord that = (AUPAcceptanceRecord) other;

    // Implement meaningful checks here
    return (getUser().equals(that.getUser()) && getAupVersion().equals(
      that.getAupVersion()));

  }

  @Override
  public int hashCode() {

    if (getId() != null)
      return getId().hashCode();

    return super.hashCode();
  }

  /**
   * @return the id
   */
  public Long getId() {

    return id;
  }

  /**
   * @return the aupVersion
   */
  public AUPVersion getAupVersion() {

    return aupVersion;
  }

  /**
   * @return the user
   */
  public VOMSUser getUser() {

    return user;
  }

  /**
   * @return the lastAcceptanceDate
   */
  public Date getLastAcceptanceDate() {

    return lastAcceptanceDate;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @param aupVersion
   *          the aupVersion to set
   */
  public void setAupVersion(AUPVersion aupVersion) {

    this.aupVersion = aupVersion;
  }

  /**
   * @param user
   *          the user to set
   */
  public void setUser(VOMSUser user) {

    this.user = user;
  }

  /**
   * @param lastAcceptanceDate
   *          the lastAcceptanceDate to set
   */
  public void setLastAcceptanceDate(Date lastAcceptanceDate) {

    this.lastAcceptanceDate = lastAcceptanceDate;
  }

  @Override
  public String toString() {

    return String.format(
      "[user: %s, aupVersion: %s, lastAcceptanceDate: %s, valid: %s]", user,
      aupVersion, lastAcceptanceDate, valid);
  }

  public boolean hasExpired() {

    if (!valid)
      return true;

    if (lastAcceptanceDate.before(aupVersion.getLastUpdateTime()))
      return true;

    Date now = new Date();
    if (getExpirationDate().before(now))
      return true;

    return false;
  }

  public long getDaysSinceLastAcceptance() {

    Date now = new Date();

    long timeDiff = now.getTime() - lastAcceptanceDate.getTime();

    return TimeUnit.MILLISECONDS.toDays(timeDiff);
  }

  public long getDaysBeforeExpiration() {

    Date now = new Date();

    if (!valid)
      return 0;

    long timeDiff = getExpirationDate().getTime() - now.getTime();

    return TimeUnit.MILLISECONDS.toDays(timeDiff);

  }

  public Date getExpirationDate() {

    Calendar c = Calendar.getInstance();

    // If the record has been invalidated the expiration date is now.
    if (!valid)
      return c.getTime();

    c.setTime(lastAcceptanceDate);

    c.add(Calendar.DAY_OF_YEAR, aupVersion.getAup().getReacceptancePeriod());

    return c.getTime();
  }

  public Boolean getValid() {

    return valid;
  }

  public void setValid(Boolean valid) {

    this.valid = valid;
  }

}
