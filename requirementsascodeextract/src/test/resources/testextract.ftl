<#include "./extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	use case: ${useCase}.
	<#list useCase.flows as f>
		flow: <@useCaseFlow flow=f/>.
		<#list f.steps as s>
			step: ${s}. <@userStep step=s/><@systemStep step=s/>
		</#list>
	</#list>
</#list>
</@compress>