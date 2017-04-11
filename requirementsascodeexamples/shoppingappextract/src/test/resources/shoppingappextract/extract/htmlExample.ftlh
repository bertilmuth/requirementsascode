<#include "./lib/extract.ftl"/>
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase?capitalize}</h1>
		<#list useCase.flows as useCaseFlow>
	  		<h2>${useCaseFlow?capitalize}</h2>
	  		<div><@flowPredicate f=useCaseFlow/></div>
			<#list useCaseFlow.steps as step>
				<div>${step}: <@userStep s=step/><@systemStep s=step/></div>
			</#list>
		</#list>
  	</#list>
</body>
</html>