<#include "./lib/extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	Use case: ${useCase}.
	<#list useCase.flows as useCaseFlow>
		Flow: ${useCaseFlow} <@flowPredicate f=useCaseFlow/>
		<#list useCaseFlow.steps as s>
			Step: ${s}. <@reactWhile s=s/>${actorPartOfStep(s)}${userPartOfStep(s)}${systemPartOfStep(s)}
		</#list>
	</#list>
</#list>
</@compress>