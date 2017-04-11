<#include "./lib/extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	use case: ${useCase}.
	<#list useCase.flows as useCaseFlow>
		flow: ${useCaseFlow} <@flowPredicate f=useCaseFlow/>
		<#list useCaseFlow.steps as step>
			step: ${step}. <@reactWhile s=step/><@userStep s=step/><@systemStep s=step/>
		</#list>
	</#list>
</#list>
</@compress>