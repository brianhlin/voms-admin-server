/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.preparers.attribute;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.operations.attributes.ListAttributeDescriptionsOperation;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSAttributeDescription;

public class ListAttributeClassesPreparer extends ViewPreparerSupport {

  @Override
  public void execute(TilesRequestContext tilesContext,
    AttributeContext attributeContext) throws PreparerException {

    List<VOMSAttributeDescription> attributeClasses = (List<VOMSAttributeDescription>) ListAttributeDescriptionsOperation
      .instance().execute();

    tilesContext.getRequestScope().put("attributeClasses", attributeClasses);
  }

}
