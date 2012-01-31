<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title></title>
</head>
<body>

<s:if test="hasErrors()">
	<b>Erros:</b>
	<s:fielderror />
</s:if>

<s:form action="MeuWizard" method="post" name="frmMain" validate="true" theme="ajax">
	<s:textfield name="facade.seuNome" label="Qual o seu Nome?" size="50"/>
	<s:submit value="Next"/>
</s:form>

</body>
</html>