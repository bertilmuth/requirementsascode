<#function flowPositionName flow><#return (flow.flowPosition.class.simpleName)!""/></#function>
<#function whenName flow><#return (flow.when.class.simpleName)!""/></#function>
<#function systemReactionName step><#return (step.systemReaction.class.simpleName)!""/></#function>
<#function hasSystemReaction step><#return "IgnoreIt" != systemReactionName(step)></#function>
<#function verbNoun camelCaseName>
	<#local verb = (firstWordOf(camelCaseName) + "s")?lower_case/>
	<#local noun = afterFirstWordOf(camelCaseName)?lower_case/>
	<#local result = [verb, noun]?join(" ")?trim/>
	<#return result/>
</#function>

<#macro flow f>
${f}</#macro>

<#macro flowPosition f>
<#local flowPositionWords = wordsOf(flowPositionName(f))/>
<#local stepName = (f.flowPosition.stepName)!""/>
${flowPositionWords?lower_case} ${stepName}</#macro>

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
<#if hasSystemReaction(s)>
	<#local dot = ". "/>
<#else>
	<#local dot = "."/>
</#if>
<#local actors = s.actors?join("/")?capitalize/>
<#if actors != systemActorName>
	<#local name = s.userEventClass.simpleName/>
${actors} ${verbNoun(name)}${dot}</#if></#macro>  

<#macro systemStep s>
<#local name = systemReactionName(s)/>
<#if hasSystemReaction(s)>
	<#if name == "ContinueAt" || name == "ContinueAfter" || name == "ContinueWithoutAlternativeAt">
		<#local stepName = " " + s.systemReaction.stepName/>
	</#if>
System ${verbNoun(name)}${stepName!""}.</#if></#macro>