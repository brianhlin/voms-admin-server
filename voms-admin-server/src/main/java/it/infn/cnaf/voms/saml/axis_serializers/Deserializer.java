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
package it.infn.cnaf.voms.saml.axis_serializers;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opensaml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Valerio Venturi (valerio.venturi@cnaf.infn.it)
 *
 */
public class Deserializer extends DeserializerImpl {

  static Logger logger = LoggerFactory.getLogger(Deserializer.class);

  public void onStartElement(java.lang.String namespace,
    java.lang.String localName, java.lang.String prefix,
    org.xml.sax.Attributes attributes, DeserializationContext context)
    throws SAXException {

    try {
      MessageElement messageElement = context.getCurElement();
      Element element = messageElement.getAsDOM();

      /* call OpenSAML deserializing */

      UnmarshallerFactory unmarshallerFactory = Configuration
        .getUnmarshallerFactory();
      Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
      XMLObject unmarshalled = unmarshaller.unmarshall(element);

      setValue(unmarshalled);
    }

    catch (Exception exception) {
      throw new SAXException("Error deserializing " + " : "
        + exception.getClass() + " : " + exception.getMessage());
    }
  }
}
