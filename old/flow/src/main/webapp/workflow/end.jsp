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

--> <s:text name="%{facade.msg}"/> 

<s:form action="MeuWizard.action?start" method="post" name="frmMain" validate="true" theme="ajax">
	<s:submit label="Início"/>
	<tfoot>
		<tr><td colspan="2" align="right">
			<input type="button" value="Início" onclick="javascript:inicio();"/>	
		</td></tr>
	</tfoot>
</s:form>

</body>
</html>