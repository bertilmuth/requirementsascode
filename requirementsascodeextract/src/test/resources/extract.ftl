<#macro useCaseFlow flow>
<#assign flowPosition = ""/>
<#assign stepName = ""/>
<#if flow.flowPosition??>
	<#assign flowPositionWords = wordsOf(flow.flowPosition.class.simpleName)/>
	<#assign flowPosition = " " + flowPositionWords/>
	<#assign stepName = " " + flow.flowPosition.stepName/>
</#if>
<#assign when = ""/>
<#if flow.when??>
	<#assign whenWords = wordsOf(flow.when.class.simpleName)/>
	<#assign when = " when " + whenWords/>
</#if>
${flow}${flowPosition?lower_case}${stepName}${when?lower_case}</#macro>

<#macro userStep step>
<#assign systemActorName = step.useCaseModel.systemActor.name?capitalize>
<#assign systemReactionName = step.systemReaction.class.simpleName/>
<#if systemReactionName != "IgnoreIt">
	<#assign dot = ". "/>
<#else>
	<#assign dot = "."/>
</#if>
<#assign actors = step.actors?join("/")?capitalize/>
<#if actors != systemActorName>
	<#assign userEventName = step.userEventClass.simpleName/>
	<#assign verb = firstWordOf(userEventName)/>
	<#assign noun = afterFirstWordOf(userEventName)/>
	<#if noun != "">
		<#assign noun = " " + noun/>
	</#if>
${actors} ${verb?lower_case}s${noun?lower_case}${dot}</#if></#macro>  

<#macro systemStep step>
<#assign systemReactionName = step.systemReaction.class.simpleName/>
<#if systemReactionName != "IgnoreIt">
<#assign verb = firstWordOf(systemReactionName)/>
<#assign noun = afterFirstWordOf(systemReactionName)/>
<#if noun != "">
<#assign noun = " " + noun/>
</#if>
<#assign stepName = ""/>
<#if systemReactionName == "ContinueAt" || systemReactionName == "ContinueAfter" || systemReactionName == "ContinueWithoutAlternativeAt">
<#assign stepName = " " + step.systemReaction.stepName/>
</#if>
System ${verb?lower_case}s${noun?lower_case}${stepName}.</#if></#macro>