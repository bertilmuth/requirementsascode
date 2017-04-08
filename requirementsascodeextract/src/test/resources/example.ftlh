<#include "./extract.ftl"/>
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase?capitalize}</h1>
		<#list useCase.flows as f>
	  		<h2><@useCaseFlow flow=f/></h1>
			<#list f.steps as s>
				<div>${s}: <@userStep step=s/><@systemStep step=s/></div>
			</#list>
		</#list>
  	</#list>
</body>
</html>