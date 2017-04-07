<#macro userStep step>
<#assign systemActorName = step.useCaseModel.systemActor.name?capitalize>
<#assign actors = step.actors?join("/")?capitalize/>
<#if actors != systemActorName>
<#assign userEvent = step.userEventClass.simpleName/>
<#assign verb = firstWordOf(userEvent)/>
<#assign noun = afterFirstWordOf(userEvent)/>
${actors} ${verb}s ${noun}. </#if></#macro>  

<#macro systemStep step>
<#assign systemReaction = step.systemReaction.class.simpleName/>
<#if systemReaction != "IgnoreIt">
<#assign verb = firstWordOf(systemReaction)/>
<#assign noun = afterFirstWordOf(systemReaction)/>
System ${verb}s ${noun}.</#if></#macro>
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
				<li>${s}: <@userStep step=s/><@systemStep step=s/></li>
			</#list>
			</ul>	
		</#list>
  	</#list>
</body>
</html>