<#macro systemOfStep step>
<#assign words = words(step.system.class.simpleName)/>
${words}</#macro> 
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase}</h1>
		<#list useCase.flows as flow>
	  		<h2>${flow?cap_first}</h1>
	  	  	<ul>
			<#list flow.steps as step>
				<li>${step}: ${step.as?join(", ")?capitalize} <@systemOfStep step=step/>.</li>
			</#list>
			</ul>	
		</#list>
  	</#list>
</body>
</html>