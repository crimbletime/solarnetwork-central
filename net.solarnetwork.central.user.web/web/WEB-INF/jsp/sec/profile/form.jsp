<c:url value="/u/sec/profile/save" var="postUrl"/>
<form:form action="${postUrl}" method="post" modelAttribute="user" cssClass="form-horizontal">
	<form:hidden path="id"/>
	<c:import url="/WEB-INF/jsp/user/detail-form.jsp"/>
	<div class="form-actions">
		<button type="submit" class="btn btn-primary"><fmt:message key='save.label'/></button>
	</div>
</form:form>