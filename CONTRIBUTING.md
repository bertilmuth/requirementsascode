# How to contribute to requirements as code

Do you want to report a bug, implement a new feature or support the project by creating documentation?

Please create a GitHub issue, that contains at least the following information:
* A clear, descriptive title
* For a new feature:
  * What is the feature that you want to contribute?
  * Why do you want to contribute the feature? What is the benefit for the users of requirementsascode?
  * If possible: an outline of the technical solution
* For a bug:
  * A description of the unexpected behavior, and what behavior you would have expected
  * A step-by-step description on how to reproduce the behavior
  * The requirementsascode version you used
  * The environment in which you found the bug (Linux or Windows, Eclipse or IntelliJ etc.)
* For documentation:
  * What is the documentation that you want to contribute?
  * What is the benefit for the users of requirementsascode?
  
Once your issue is accepted, you can suggest changes by creating a branch first, and then a pull request.

Apart from the code you write, please write JUnit tests to ensure quality.

Note that requirementsascodecore uses Gradle as a build tool.
So if you can use it, please use it, as it simplifies multi project builds.
This is not mandatory, though.
