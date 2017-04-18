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
package org.glite.security.voms.admin.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.glite.security.voms.service.attributes.AttributeClass;
import org.glite.security.voms.service.attributes.AttributeValue;

@MappedSuperclass
public abstract class VOMSBaseAttribute implements VomsAttributeValue,
		Serializable {

	@Id
	@ManyToOne
	@PrimaryKeyJoinColumn(name="a_id")
	VOMSAttributeDescription a_id;
	
	@Column(name="a_value")
	String value;
	
	@Transient
	String context;

	public AttributeValue asAttributeValue() {

		AttributeClass aClass = getAttributeDescription().asAttributeClass();

		AttributeValue val = new AttributeValue();
		val.setAttributeClass(aClass);

		val.setContext(getContext());

		val.setValue(getValue());

		return val;
	}

	protected VOMSBaseAttribute() {

	}

	protected VOMSBaseAttribute(VOMSAttributeDescription desc, String value) {

		this.a_id = desc;
		this.value = value;
	}

	public VOMSAttributeDescription getAttributeDescription() {

		return a_id;
	}

	public void setAttributeDescription(
			VOMSAttributeDescription a_id) {

		this.a_id = a_id;
	}

	public void setContext(String context) {

		this.context = context;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {

		this.value = value;
	}

	public String getName() {

		return a_id.getName();
	}

	public void setName(String name) {

		a_id.setName(name);
	}
}
