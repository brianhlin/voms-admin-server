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
package org.glite.security.voms.admin.operations.attributes;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.attribute.AttributeDescriptionDeletedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;

public class DeleteAttributeDescriptionOperation extends BaseVomsOperation {

  String name;

  private DeleteAttributeDescriptionOperation(String name) {

    this.name = name;
  }

  protected Object doExecute() {

    VOMSAttributeDAO dao  = VOMSAttributeDAO.instance();
    
    VOMSAttributeDescription desc = dao.getAttributeDescriptionByName(name);
    dao.deleteAttributeDescription(name);
    
    EventManager.instance().dispatch(new AttributeDescriptionDeletedEvent(desc));
    return desc;
  }

  protected void setupPermissions() {

    VOMSContext voContext = VOMSContext.getVoContext();

    addRequiredPermission(voContext, VOMSPermission.getContainerRWPermissions()
      .setAttributesReadPermission().setAttributesWritePermission());
  }

  public static DeleteAttributeDescriptionOperation instance(String name) {

    return new DeleteAttributeDescriptionOperation(name);
  }

}
