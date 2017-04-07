<#include "./extract.ftl"/>
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase?capitalize}</h1>
		<#list useCase.flows as flow>
	  		<h2>${flow?capitalize}</h1>
	  	  	
			<#list flow.steps as s>
				<div>${s}: <@userStep step=s/><@systemStep step=s/></div>
			</#list>
		</#list>
  	</#list>
</body>
</html>