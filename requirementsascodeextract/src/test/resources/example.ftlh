<#macro asOf step>
<#assign actors = step.as?join("/")?capitalize/>
${actors}</#macro> 
<#macro systemOf step>
<#assign systemReaction = step.system.class.simpleName/>
<#assign verb = firstWordOf(systemReaction)/>
<#assign noun = afterFirstWordOf(systemReaction)/>
${verb}s ${noun}</#macro> 
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