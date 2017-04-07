<#macro userStep step>
<#assign systemActorName = step.useCaseModel.systemActor.name?capitalize>
<#assign systemReaction = step.systemReaction.class.simpleName/>
<#if systemReaction != "IgnoreIt">
	<#assign dot = ". "/>
<#else>
	<#assign dot = "."/>
</#if>
<#assign actors = step.actors?join("/")?capitalize/>
<#if actors != systemActorName>
	<#assign userEvent = step.userEventClass.simpleName/>
	<#assign verb = firstWordOf(userEvent)/>
	<#assign noun = afterFirstWordOf(userEvent)/>
	<#if noun != "">
		<#assign noun = " " + noun/>
	</#if>
${actors} ${verb?lower_case}s${noun?lower_case}${dot}</#if></#macro>  

<#macro systemStep step>
<#assign systemReaction = step.systemReaction.class.simpleName/>
<#if systemReaction != "IgnoreIt">
<#assign verb = firstWordOf(systemReaction)/>
<#assign noun = afterFirstWordOf(systemReaction)/>
<#if noun != "">
<#assign noun = " " + noun/>
</#if>
System ${verb?lower_case}s${noun?lower_case}.</#if></#macro>