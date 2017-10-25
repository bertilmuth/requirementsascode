<#function flowPosition flow>
	<#local result = ""/>
	<#if flow.flowPosition.isPresent()>
		<#local flowPosition = flow.flowPosition.get()/>
		<#local flowPositionWords = lowerCaseWordsOfClassName(flowPosition.class)/>
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
		<#local whenWords = "when " + lowerCaseWordsOfClassName(when.class)/>
	</#if>
	<#return whenWords/>
</#function>

<#macro flowPredicate f>
<#local predicate = flowPosition(f) + flowPredicateSeparator(f,", ") + when(f)>
<#local colon><#if predicate != "">:</#if></#local>
${predicate?cap_first}${colon}</#macro>
