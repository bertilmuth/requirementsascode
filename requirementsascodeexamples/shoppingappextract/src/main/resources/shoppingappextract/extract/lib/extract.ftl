<#function flowPositionName flow><#return (flow.flowPosition.get().class.simpleName)!""/></#function>
<#function whenName flow><#return (flow.when.get().class.simpleName)!""/></#function>
<#function predicateName step><#return (step.predicate.class.simpleName)!""/></#function>
<#function reactWhileConditionName step><#return (step.predicate.reactWhileCondition.class.simpleName)!""/></#function>
<#function userEventName step><#return (step.userEventClass.simpleName)!""/></#function>
<#function systemReactionName step><#return (step.systemReaction.class.simpleName)!""/></#function>

<#function hasReactWhile step><#return "ReactWhile" == predicateName(step)/></#function>
<#function hasUser step><#return (step.useCaseModel.systemActor.name != step.actors[0])></#function>
<#function hasSystemReaction step><#return "IgnoreIt" != systemReactionName(step)></#function>

<#function verbNoun camelCaseName>
	<#local verb = (firstWordOf(camelCaseName))?lower_case/>
	<#local noun = afterFirstWordOf(camelCaseName)?lower_case/>
	<#local result = [verb, noun]?join(" ")?trim/>
	<#return result/>
</#function>

<#function flowPosition f>
	<#local result = ""/>
	<#if f.flowPosition.isPresent()>
		<#local flowPositionWords = wordsOf(flowPositionName(f))/>
		<#local stepName = (f.flowPosition.get().stepName)!""/>
		<#local result = [flowPositionWords?lower_case, stepName]?join(" ")?trim/>
	</#if>
	<#return result/>
</#function>

<#function flowPredicateSeparator f sep>
	<#if flowPosition(f) != "" && when(f) != "">
		<#local result = sep/>
	</#if>
	<#return result!""/>
</#function>

<#function when f>
	<#local whenWords = ""/>
	<#if f.when.isPresent()>
		<#local whenWords = "when " + wordsOf(whenName(f))/>
	</#if>
	<#return whenWords?lower_case/>
</#function>

<#macro flowPredicate f>
<#local predicate = flowPosition(f) + flowPredicateSeparator(f,", ") + when(f)>
<#local colon><#if predicate != "">:</#if></#local>
${predicate?cap_first}${colon}</#macro>

<#macro reactWhile s>
<#if hasReactWhile(s)>
	<#local reactWhileCondition = wordsOf(reactWhileConditionName(s))?lower_case/>
<#if reactWhileCondition != "">As long as ${reactWhileCondition}: </#if></#if></#macro>  

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
	<#if name == "ContinuesAt" || name == "ContinuesAfter" || name == "ContinuesWithoutAlternativeAt">
		<#local stepName = " " + s.systemReaction.stepName/>
	<#elseif name == "IncludesUseCase">
		<#local stepName = " " + s.systemReaction.includedUseCase.name/>
	</#if>
System ${systemReaction}${stepName!""}.</#if></#macro>