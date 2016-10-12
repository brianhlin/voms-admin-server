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
package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.glite.security.voms.admin.error.NotFoundException;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.persistence.dao.generic.NamedEntityDAO;
import org.hibernate.criterion.Restrictions;

public class NamedEntityHibernateDAO<T, ID extends Serializable> extends
  GenericHibernateDAO<T, ID> implements NamedEntityDAO<T, ID> {

  public T findByName(String name) {

    if (name == null)
      throw new NullArgumentException(
        "Please provide a value for the 'name' argument! null is not a valid value in this context.");

    if (name == null)
      throw new NullArgumentException("name cannot be null!");

    List<T> retVal = findByCriteria(Restrictions.eq("name", name));

    if (retVal.isEmpty())
      return null;

    return retVal.get(0);

  }

  public T deleteByName(String name) {

    if (name == null)
      throw new NullArgumentException(
        "Please provide a value for the 'name' argument! null is not a valid value in this context.");

    T instance = findByName(name);
    if (instance == null)
      throw new NotFoundException("No '" + instance.getClass().getSimpleName()
        + "' instance found for name '" + name + "'.");

    makeTransient(instance);

    return instance;

  }

}
