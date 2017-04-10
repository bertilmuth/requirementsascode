<#include "./extract.ftl"/>
<@compress single_line=true>
<#list useCaseModel.useCases as useCase>
	use case: ${useCase}.
	<#list useCase.flows as useCaseFlow>
		flow: <@flow f=useCaseFlow/> <@flowPosition f=useCaseFlow/><@separator f=useCaseFlow sep=" "/><@when f=useCaseFlow/>.
		<#list useCaseFlow.steps as step>
			step: ${step}. <@userStep s=step/><@systemStep s=step/>
		</#list>
	</#list>
</#list>
</@compress>