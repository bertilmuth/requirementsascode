<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title>Requirements as Code - Extract Example</title>
</head>
<body>
  	<#list useCaseModel.useCases as useCase>
  		<h1>Use Case: ${useCase?capitalize}</h1>
		<#list useCase.flows as f>
	  		<h2>${f}</h2>
	  		<div>${flowCondition(f)}</div>
			<#list f.steps as s>
				<div>${s}. ${reactWhileOfStep(s)}${actorPartOfStep(s)}${userPartOfStep(s)}${systemPartOfStep(s)}</div>
			</#list>
		</#list>
  	</#list>
</body>
</html>