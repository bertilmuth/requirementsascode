<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	Use case: ${useCase}.
	<#list useCase.flows as f>
		Flow: ${f} ${flowCondition(f)}
		<#list f.steps as s>
			Step: ${s}. ${reactWhileOfStep(s)}${actorPartOfStep(s)}${userPartOfStep(s)}${systemPartOfStep(s)}
		</#list>
	</#list>
</#list>
</@compress>