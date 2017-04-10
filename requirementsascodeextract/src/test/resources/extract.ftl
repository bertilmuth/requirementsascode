<#function flowPositionName flow><#return (flow.flowPosition.class.simpleName)!""/></#function>
<#function whenName flow><#return (flow.when.class.simpleName)!""/></#function>
<#function systemReactionName step><#return (step.systemReaction.class.simpleName)!""/></#function>

<#macro flow f>
${f}</#macro>

<#macro flowPosition f>
<#local flowPositionWords = wordsOf(flowPositionName(f)) + " "/>
<#local stepName = (f.flowPosition.stepName)!""/>
${flowPositionWords?lower_case}${stepName}</#macro>

<#macro separator f sep>
	<#if flowPositionName(f) != "" && whenName(f) != "">
		${sep}
	</#if>
</#macro>

<#macro when f>
<#local whenWords = ""/>
<#if f.when??>
	<#local whenWords = "when " + wordsOf(whenName(f))/>
</#if>
${whenWords?lower_case}</#macro>

<#macro userStep s>
<#local systemActorName = s.useCaseModel.systemActor.name?capitalize>
<#if "IgnoreIt" != systemReactionName(s)>
	<#local dot = ". "/>
<#else>
	<#local dot = "."/>
</#if>
<#local actors = s.actors?join("/")?capitalize/>
<#if actors != systemActorName>
	<#local userEventName = s.userEventClass.simpleName/>
	<#local verb = firstWordOf(userEventName)/>
	<#local noun = afterFirstWordOf(userEventName)/>
	<#if noun != "">
		<#local noun = " " + noun/>
	</#if>
${actors} ${verb?lower_case}s${noun?lower_case}${dot}</#if></#macro>  

<#macro systemStep s>
<#local name = systemReactionName(s)/>
<#if "IgnoreIt" != name>
	<#local verb = (firstWordOf(name) + "s")?lower_case/>
	<#local noun = afterFirstWordOf(name)?lower_case/>
	<#local verbNoun = [verb, noun]?join(" ")?trim/>

	<#local stepName = ""/>
	<#if name == "ContinueAt" || name == "ContinueAfter" || name == "ContinueWithoutAlternativeAt">
		<#local stepName = " " + s.systemReaction.stepName/>
	</#if>
System ${verbNoun}${stepName}.</#if></#macro>