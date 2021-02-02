---
layout: default
title: Risk assessment
parent: Documentation
---

# Risk assessment

[Deliverable (.pdf)](/assets/deliverables/Risk1.pdf){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 }

{: .no_toc }

<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

---

## Risk method justification

We have identified risks that we believe are relevant to this project by going through a number of sources.
1. Researching general software development risks.
2. Discussing with other people who have had previous experience.
3. Discussing hypotheticals amongst ourselves and evaluating them.

We have chosen to deliver this in a tabular format. There are 6 defined columns for each risk. The first column being each risk’s unique ID. The second column is the type of risk. The third column is a brief description of the risk. The fourth column is the likelihood of that risk occurring on the following scale:
* Low - Very unlikely to happen, with a minuscule chance of occurring.
* Medium - It could happen or not, depending on the mitigation taken.
* High - Very large chance of occurring, and common.

The fifth column is the severity of the risk, so how much damage it could cause. This is on a scale of:
* Low - Not much damage, a couple of hours work at most. Not worth reporting
* Medium - A week of work, this could affect internal deadlines. Group members would be informed to recover swiftly.
* High - A large sum of work or all of it is affected. Project deadline is affected. Group members and teaching staff informed to resolve.

The seventh and final column is the ownership of the risk. They will be held responsible for managing and mitigating that risk.

Our risk ownership is determined by who is most likely to experience that risk and who could help mitigate the risk.

Our risk assessment has been updated continuously since we have started this project. With it being reviewed every fortnight. Ensuring that the risks were still relevant even with a change in scope or if new risks have been identified.

## Risks

| ID | Type | Description | Likelihood | Severity | Mitigation | Owner|
|---------------------------------------------------------------------|
|1|Project|Loss of work due to corruption or human error|Low|Medium|Frequent creation of backups both internally and externally|All|
|2|Project|Loss of a team member|Low|High|Have a backup for everybody’s role (high bus factor)|All|
|3|Product|Game engine limitations|Medium|Medium|Focus on simple mechanics that meet the requirements|All|
|4|Product and Project|Requirement changes|Low|Medium|Have a flexible code that we can change easily|All|
|5|Product|Unavailable documentation for the libraries|Medium|Low|Use a popular library with lots of tutorials online|All|
|6|Product|Difficulty in creating graphic assets|Medium|Low|Use simplistic art-style and/or look for assets online|All|
|7|Difficulty in finding a fitting soundtrack|Low|Low|Settle for non-copyrighted music from online sources|All|
|8|Project|GitHub servers become unavailable|Low|High|Store files across a variety of platforms, including google docs and local copies|All|
|9|Product|Difficulty implementing the project architecture|Low|Medium|Build a clear architecture and help team members to implement the project in a simple and straightforward manner through code reviews|All|
|10|Product|Player controls don’t work as expected|Medium|Medium|Properly test the implemented features|Dragos|
|11|Product|UI becomes pixelated when scaled up|Medium|Low|Implement the UI using a different format|Sam|
|12|Product|AI doesn’t behave as expected|Medium|Low|Consider every possible scenario and do lots of testing, also getting reviews from other team members|Dragos|
|13|Product|The game map does not properly fit the game screen|Medium|Low|Properly study and understand the different scales we need to use, based on aspect ratio and resolution|Dragon, Quentin|
|14|Product|UI fails to react to the events in the game in a timely manner|Low|Low|Collaborate with team members to ensure code is optimized, and use a different format if necessary|Sam|
|15|Product|Collisions do not work as expected|Medium|Medium|Make sure the physics properties of each body are correct, so the interactions between them don’t affect other features|Dragos|
|16|Product|Transitions between game stages don’t work|Medium|Medium|Keep the UI classes and the game classes separate so each feature is independent and merging is easy|Dragos, Sam|
|17|Product|Boats don’t recognise they are outside of the lanes so they don’t get penalised|Low|Medium|Implement an easy way of checking if the boat is still withing the lane boundaries|Dragos|
|18|Business|Difficulty acquiring the right software to develop the game|Low|Medium|Search around different valid combinations of software that could be used to develop the game|All|