<#macro asOf step>
<#assign result = step.as?join("/")?capitalize/>
${result}</#macro> 
<#macro systemOf step>
<#assign result = wordsOf(step.system.class.simpleName)/>
${result}</#macro> 
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
			<#list flow.steps as s>
				<li>${s}: <@asOf step=s/> <@systemOf step=s/>.</li>
			</#list>
			</ul>	
		</#list>
  	</#list>
</body>
</html>