<#macro flow f>
${f}</#macro>

<#macro flowPosition f>
<#assign flowPositionWords = ""/>
<#assign stepName = ""/>
<#if f.flowPosition??>
	<#assign flowPositionWords = wordsOf(f.flowPosition.class.simpleName) + " "/>
	<#assign stepName = f.flowPosition.stepName/>
</#if>
${flowPositionWords?lower_case}${stepName}</#macro>

<#macro when f>
<#assign whenWords = ""/>
<#if f.when??>
	<#assign whenWords = "when " + wordsOf(f.when.class.simpleName)/>
</#if>
${whenWords?lower_case}</#macro>

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