<#macro flow f>
<#assign flowPosition = ""/>
<#assign stepName = ""/>
<#if f.flowPosition??>
	<#assign flowPositionWords = wordsOf(f.flowPosition.class.simpleName)/>
	<#assign flowPosition = " " + flowPositionWords/>
	<#assign stepName = " " + f.flowPosition.stepName/>
</#if>
<#assign when = ""/>
<#if f.when??>
	<#assign whenWords = wordsOf(f.when.class.simpleName)/>
	<#assign when = " when " + whenWords/>
</#if>
${f}${flowPosition?lower_case}${stepName}${when?lower_case}</#macro>

<#macro userStep s>
<#assign systemActorName = s.useCaseModel.systemActor.name?capitalize>
<#assign systemReactionName = s.systemReaction.class.simpleName/>
<#if systemReactionName != "IgnoreIt">
	<#assign dot = ". "/>
<#else>
	<#assign dot = "."/>
</#if>
<#assign actors = s.actors?join("/")?capitalize/>
<#if actors != systemActorName>
	<#assign userEventName = s.userEventClass.simpleName/>
	<#assign verb = firstWordOf(userEventName)/>
	<#assign noun = afterFirstWordOf(userEventName)/>
	<#if noun != "">
		<#assign noun = " " + noun/>
	</#if>
${actors} ${verb?lower_case}s${noun?lower_case}${dot}</#if></#macro>  

<#macro systemStep s>
<#assign systemReactionName = s.systemReaction.class.simpleName/>
<#if systemReactionName != "IgnoreIt">
<#assign verb = firstWordOf(systemReactionName)/>
<#assign noun = afterFirstWordOf(systemReactionName)/>
<#if noun != "">
<#assign noun = " " + noun/>
</#if>
<#assign stepName = ""/>
<#if systemReactionName == "ContinueAt" || systemReactionName == "ContinueAfter" || systemReactionName == "ContinueWithoutAlternativeAt">
<#assign stepName = " " + s.systemReaction.stepName/>
</#if>
System ${verb?lower_case}s${noun?lower_case}${stepName}.</#if></#macro>