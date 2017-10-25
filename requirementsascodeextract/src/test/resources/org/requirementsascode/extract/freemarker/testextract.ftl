<#include "./lib/extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	use case: ${useCase}.
	<#list useCase.flows as useCaseFlow>
		flow: ${useCaseFlow} <@flowPredicate f=useCaseFlow/>
		<#list useCaseFlow.steps as s>
			step: ${s}. <@reactWhile s=s/>${userPartOfStep(s)}${systemPartOfStep(s)}
		</#list>
	</#list>
</#list>
</@compress>