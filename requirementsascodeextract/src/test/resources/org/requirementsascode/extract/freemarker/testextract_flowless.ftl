<@compress single_line=true>
<#list model.useCases as useCase>
	Use case: ${useCase}.
	<#list useCase.steps as s>
		Step: ${s}. ${reactWhileOfStep(s)}${actorPartOfStep(s)}${userPartOfStep(s)}${systemPartOfStep(s)}
	</#list>
</#list>
</@compress>