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
	<s:datepicker name="facade.dataAtual" label="Informe a Data de Hoje" size="50"/>
	<tfoot>
		<tr><td colspan="2" align="right">
			<input type="button" value="Back" onclick="javascript:back();"/>	
			<input type="button" value="Next" onclick="javascript:next();"/>	
		</td></tr>
	</tfoot>
</s:form>

</body>
</html>

<script type="text/javascript">
	function next(){
		document.forms[0].action='MeuWizard.action';
		document.forms[0].submit();
	}
	
	function back(){
		document.forms[0].action='MeuWizard.action?back';
		document.forms[0].submit();
	}
</script>