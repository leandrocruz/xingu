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

Este Wizard é Legal?
<s:form action="MeuWizard" method="post" name="frmMain" validate="true" theme="ajax">
	<tfoot>
		<tr><td colspan="2" align="right">
			<input type="button" value="Sim" onclick="javascript:sim();"/>	
			<input type="button" value="Não" onclick="javascript:nao();"/>	
			<input type="button" value="Back" onclick="javascript:back();"/>	
		</td></tr>
	</tfoot>
</s:form>

</body>
</html>

<script type="text/javascript">
	function sim(){
		document.forms[0].action='MeuWizard.action?facade.ehLegal=true';
		document.forms[0].submit();
	}
	
	function nao(){
		document.forms[0].action='MeuWizard.action?facade.ehLegal=false';
		document.forms[0].submit();
	}

	function back(){
		document.forms[0].action='MeuWizard.action?back';
		document.forms[0].submit();
	}
</script>