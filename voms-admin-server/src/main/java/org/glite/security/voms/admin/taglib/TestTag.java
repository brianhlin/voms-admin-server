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
package org.glite.security.voms.admin.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class TestTag extends TagSupport {

  protected List contextList = new ArrayList();

  public int doStartTag() throws JspException {

    try {
      pageContext.getOut().write("<div class='testTag'>");
    } catch (IOException e) {
      throw new JspTagException(e.getMessage());
    }

    return EVAL_BODY_INCLUDE;
  }

  public int doEndTag() throws JspException {

    Iterator i = contextList.iterator();

    while (i.hasNext()) {

      try {
        pageContext.getOut().write(
          "<div class='innerTag'>" + i.next() + "</div>\n");
      } catch (IOException e) {
        throw new JspTagException(e.getMessage());
      }
    }
    return super.doEndTag();
  }

}
