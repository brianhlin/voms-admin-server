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

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.hibernate.criterion.Restrictions;

public class GroupManagerDAOHibernate extends
  GenericHibernateDAO<GroupManager, Long> implements GroupManagerDAO {

  @Override
  public GroupManager findByName(String name) {

    return findByCriteriaUniqueResult(Restrictions.eq("name", name));
  }
  
  public List<GroupManager> findAll() {
    
    CriteriaBuilder cb = getSession().getCriteriaBuilder();
    CriteriaQuery<GroupManager> crit = cb.createQuery(GroupManager.class);
    Root<GroupManager> root = crit.from(GroupManager.class);
    
    return HibernateFactory.getSession().createQuery(crit).getResultList();
    
  }
}
