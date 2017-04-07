<#include "./extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	use case: ${useCase?lower_case}.
	<#list useCase.flows as flow>
		flow: ${flow?lower_case}.
	</#list>
</#list>
</@compress>