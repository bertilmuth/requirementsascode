<#include "./extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	use case: ${useCase}.
	<#list useCase.flows as flow>
		flow: ${flow}.
		<#list flow.steps as s>
			step: ${s}. <@userStep step=s/><@systemStep step=s/>
		</#list>
	</#list>
</#list>
</@compress>