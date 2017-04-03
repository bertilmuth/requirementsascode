<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>

<#macro wordsLowerCase camelCase>
<#assign sentence = camelCase?replace('(\\p{Upper})', ' $1', 'r')?lower_case/><#assign words = sentence?word_list/><#list words as word> ${word}<#if word?index == 0>s</#if></#list></#macro> 

 	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase}</h1>
		<#list useCase.flows as flow>
	  		<h2>${flow?cap_first}</h1>
	  	  	<ul>
			<#list flow.steps as step>
				<li>${step}: ${step.as?join(", ")?capitalize}<@wordsLowerCase camelCase=step.system.class.simpleName/></li>
			</#list>
			</ul>	
		</#list>
  	</#list>
</body>
</html>