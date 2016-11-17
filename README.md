# requirementsascode
Living specification of application requirements in code, for long term changeability.

Note that the API is likely to change.
At least Java 8 is required.

Subprojects:
* [requirementsascodecore](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodecore): the core library to be used, with no third-party dependencies.
* [requirementsascodeexamples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples): example projects illustrating the use of requirementsascodecore.

A good place to start are the [helloworld examples](https://github.com/bertilmuth/requirementsascode/tree/master/requirementsascodeexamples/helloworld).

# Why bother?
Documenting a requirement provides value, not only when it is implemented, but especially when it changes later on and the impact of the change can be analysed by traceability. However, the documentation needs to be updated with every change or it will get outdated very quickly, rendering it worthless - that's why people often say "the truth is in the code".

To avoid the effort, many agile teams do not even maintain documentation of a requirement after it has been implemented, for example they throw away the user stories. The risk of that approach is that over time, without requirements and traceability, it becomes unclear *where* in the code *what changes* need to be performed when a requirement changes. This is especially true if many teams and developers with different programming styles work on an application. 

As long as ideas are exchanged between stakeholders and the requirements are discussed but not yet implemented, it makes sense to have a lightweight documentation of requirements outside the code, maybe as part of a backlog or a lightweight specification.

For the long term changeability of a requirement once it has been implemented, requirementsascode proposes a different way to document the requirement: in the code itself. A specific part of the code controls both the behavior of the application and documents the requirements in a formal way. The requirement documentation is similar to [user goal level use case](https://en.wikipedia.org/wiki/Use_case#Goal_levels) narratives (inside the code).

Benefits of this approach include:
* Perform impact analysis of changes: how does a change to a requirement affect the software components?
* Always up-to-date traceability from requirements to code (inside the code)
* Clear separation between requirements and realization, making technological changes easier
* Clear separation between user interface and backend, making it easier to test the application without user interface, and vary or change the user interface.

In the future it will also be possible to generate always up-to-date documentation from the code 
that documents how the application really works.

# Related topics
* The work of Ivar Jacobson on Use Cases. As an example, have a look at [Use Case 2.0](https://www.ivarjacobson.com/publications/white-papers/use-case-ebook).
* The work of Alistair Cockburn on Use Cases, specifically the different goal levels. Look [here](http://alistair.cockburn.us/Use+case+fundamentals) to get started, or read the book "Writing Effective Use Cases".
* Behavior Driven Development ([BDD](https://dannorth.net/introducing-bdd/)): There are similarities in the purpose of BDD and requirementsascode. Both try to provide a "ubiquitous language" to be spoken by both business people and developers. While the focus of BDD is more on specific examples (that may or may not be executed by automated tests), the focus of requirementsascode is on implementation of requirements in production code. While examples and tests focus on the specific state or behavior resulting from a specific input to the software, the requirements describe the software's behavior in more general terms. 
* The Boundary Control Entity Pattern ([BCE](http://epf.eclipse.org/wikis/openup/core.tech.common.extend_supp/guidances/guidelines/entity_control_boundary_pattern_C4047897.html)): The UseCaseModelRunner in requirementsascode plays the role of the Control class in BCE, but it is configured by an underlying UseCaseModel.
* Robert C. Martin's [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) and the architectures it is based upon.
