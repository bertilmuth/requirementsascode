<#function systemReactionName step><#return (step.systemReaction.class.simpleName)!""/></#function>

<#function hasReactWhile step><#return "ReactWhile" == step.predicate.class.simpleName/></#function>
<#function hasUser step><#return (step.useCaseModel.systemActor.name != step.actors[0])></#function>
<#function hasSystemReaction step><#return "IgnoreIt" != systemReactionName(step)></#function>

<#function flowPosition flow>
	<#local result = ""/>
	<#if flow.flowPosition.isPresent()>
		<#local flowPosition = flow.flowPosition.get()/>
		<#local flowPositionWords = lowerCaseWordsOf(flowPosition.class.simpleName)/>
		<#local stepName = (flowPosition.stepName)!""/>
		<#local result = [flowPositionWords, stepName]?join(" ")?trim/>
	</#if>
	<#return result/>
</#function>

<#function flowPredicateSeparator flow sep>
	<#if flowPosition(flow) != "" && when(flow) != "">
		<#local result = sep/>
	</#if>
	<#return result!""/>
</#function>

<#function when flow>
	<#local whenWords = ""/>
	<#if flow.when.isPresent()>
		<#local when = flow.when.get()/>
		<#local whenWords = "when " + lowerCaseWordsOf(when.class.simpleName)/>
	</#if>
	<#return whenWords/>
</#function>

<#macro flowPredicate f>
<#local predicate = flowPosition(f) + flowPredicateSeparator(f,", ") + when(f)>
<#local colon><#if predicate != "">:</#if></#local>
${predicate?cap_first}${colon}</#macro>

<#macro reactWhile s>
<#if hasReactWhile(s)>
	<#local reactWhileCondition = lowerCaseWordsOf(s.predicate.reactWhileCondition.class.simpleName)/>
<#if reactWhileCondition != "">As long as ${reactWhileCondition}: </#if></#if></#macro>  

<#macro userStep s>
<#if hasUser(s)>
	<#local actors = s.actors?join("/")?capitalize/>
	<#local userEvent = lowerCaseWordsOf(s.userEventClass.simpleName)/>
	<#if hasSystemReaction(s)>
		<#local space = " "/>
	</#if>
${actors} ${userEvent}.${space!""}</#if></#macro>  

<#macro systemStep s>
<#if hasSystemReaction(s)>
	<#local name = systemReactionName(s)/>
	<#local systemReaction = lowerCaseWordsOf(name)/>
	<#if name == "ContinuesAt" || name == "ContinuesAfter" || name == "ContinuesWithoutAlternativeAt">
		<#local stepName = " " + s.systemReaction.stepName/>
	<#elseif name == "IncludesUseCase">
		<#local stepName = " " + s.systemReaction.includedUseCase.name/>
	</#if>
System ${systemReaction}${stepName!""}.</#if></#macro>