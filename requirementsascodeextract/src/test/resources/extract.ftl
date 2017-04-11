<#function flowPositionName flow><#return (flow.flowPosition.class.simpleName)!""/></#function>
<#function whenName flow><#return (flow.when.class.simpleName)!""/></#function>
<#function userEventName step><#return (step.userEventClass.simpleName)!""/></#function>
<#function systemReactionName step><#return (step.systemReaction.class.simpleName)!""/></#function>
<#function hasUser step><#return (step.useCaseModel.systemActor.name != step.actors[0])></#function>
<#function hasSystemReaction step><#return "IgnoreIt" != systemReactionName(step)></#function>
<#function verbNoun camelCaseName>
	<#local verb = (firstWordOf(camelCaseName) + "s")?lower_case/>
	<#local noun = afterFirstWordOf(camelCaseName)?lower_case/>
	<#local result = [verb, noun]?join(" ")?trim/>
	<#return result/>
</#function>

<#function flowPosition f>
	<#local flowPositionWords = wordsOf(flowPositionName(f))/>
	<#local stepName = (f.flowPosition.stepName)!""/>
	<#local result = [flowPositionWords?lower_case, stepName]?join(" ")?trim/>
	<#return result/>
</#function>

<#function flowPredicateSeparator f sep>
	<#if flowPositionName(f) != "" && whenName(f) != "">
		<#local result = sep/>
	</#if>
	<#return result!""/>
</#function>

<#function when f>
	<#local whenWords = ""/>
	<#if f.when??>
		<#local whenWords = "when " + wordsOf(whenName(f))/>
	</#if>
	<#return whenWords?lower_case/>
</#function>

<#macro flowPredicate f>
${flowPosition(f)}${flowPredicateSeparator(f,", ")}${when(f)}</#macro>

<#macro userStep s>
<#if hasUser(s)>
	<#local actors = s.actors?join("/")?capitalize/>
	<#local userEvent = verbNoun(userEventName(s))/>
	<#if hasSystemReaction(s)>
		<#local space = " "/>
	</#if>
${actors} ${userEvent}.${space!""}</#if></#macro>  

<#macro systemStep s>
<#local name = systemReactionName(s)/>
<#if hasSystemReaction(s)>
	<#local systemReaction = verbNoun(name)/>
	<#if name == "ContinueAt" || name == "ContinueAfter" || name == "ContinueWithoutAlternativeAt">
		<#local stepName = " " + s.systemReaction.stepName/>
	</#if>
System ${systemReaction}${stepName!""}.</#if></#macro>