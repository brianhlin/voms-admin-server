<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@include file="/WEB-INF/p/shared/taglibs.jsp"%>

<s:if test="model == null">
	You're not a VO user. You'll see nothing around here.
</s:if>
<s:else>
  <tiles2:insertTemplate template="../shared/errorsAndMessages.jsp"/>
  
  <div id="welcomeUserName">
    <s:if test="name != null and surname != null">
      <div>
        <s:property value="name+ ' ' +surname" />
        <span style="font-weight: normal; font-size: smaller">( ${id} )</span>
        <s:if test="#attr.orgdbEnabled">
          <span style="font-weight: normal; font-size: smaller"> <tiles2:insertTemplate
              template="orgdbId.jsp" />
          </span>
        </s:if>
      </div>
      <div style="margin-top: .5em">
        <span class="institution"><s:property value="institution" /></span>
      </div>
    </s:if>
    <s:else>
      <s:property value="dn" />
    </s:else>
  </div>

  <s:url
    action="request-membership-removal-input"
    var="requestMembershipRemovalURL">
    <s:param
      name="userId"
      value="id" />
  </s:url>

  <tiles2:insertTemplate template="membershipStatusDetail.jsp" />
  
  <s:if test="hasPendingSignAUPTasks()">
      <div>
        <s:url
          value="/sign-aup" var="signAUPURL" />
        <input type="submit" value="Sign the AUP!" onclick="window.location.href= '${signAUPURL}'"/>
      </div>
  </s:if>
  
  <s:if test="#attr.registrationEnabled">
    <s:if test="pendingMembershipRemovalRequests.empty">
      <div style="clear: both; float: right; margin-bottom: .5em">
        <a
          href="${requestMembershipRemovalURL}"
          class="actionLink">Apply for membership removal</a>
      </div>
    </s:if>
  </s:if>

  <tiles2:insertTemplate template="personalInfoPane.jsp">
    <tiles2:putAttribute
      name="panelName"
      value="Your personal information" />
  </tiles2:insertTemplate>

  <tiles2:insertTemplate template="certificateRequestPane.jsp">
    <tiles2:putAttribute
      name="panelName"
      value="Your certificates" />
  </tiles2:insertTemplate>

  <tiles2:insertTemplate template="mappingsRequestPane.jsp" />

  <tiles2:insertTemplate template="attributesPane.jsp">
    <tiles2:putAttribute
      name="panelName"
      value="Your generic attributes" />
  </tiles2:insertTemplate>

  <s:if test="#attr.registrationEnabled">
    <tiles2:insertTemplate template="requestHistoryPane.jsp">
      <tiles2:putAttribute
        name="panelName"
        value="Your request history" />
    </tiles2:insertTemplate>
  </s:if>

</s:else>
