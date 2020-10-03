## Self-Evaluation Form for Milestone 1

### General 

We will run self-evaluations for each milestone this semester.  The
graders will evaluate them for accuracy and completeness.

Every self-evaluation will go out into your Enterprise GitHub repo
within a short time afrer the milestone deadline, and you will have 24
hours to answer the questions and push back a completed form.

This one is a practice run to make sure you get


### Specifics 


- does your analysis cover the following ideas:

  - the need for an explicit Interface specification between the (remote) AI 
    players and the game system?
    
    **We misinterpreted the instructions for the assignment, so we focused on the Fish Game exclusively and forgot to mention the AI players. Our Controller referee would allow the ability for an AI to interface with it based on what we discussed amongst each other, but we didn't explicitly mention the details of this Interface.**
  - the need for a referee sub-system for managing individual games
    
    **In our example, the referee would tightly coupled with the Fish Game itself acting as a controller that would decide who did what and when. It would manage the AI and the state of the game(removing hexagons, placing penguins or moving penguins). We discussed after the code walks that it would make more sense to have a referee that was not tightly coupled with the Fish Game.**

  - the need for a tournament management sub-system for grouping
    players into games and dispatching to referee components
    
    **We didn't focus on the tournament as we thought the Fish Game system just included the rules of the Fish game outside of the context of dot game.  We spoke mostly of the MVC model of the fish game itself.**


- does your building plan identify concrete milestones with demo prototypes:

  - for running individual games
    
    **In our third milestone, we mentioned that we would have a fully functional Fish game.**

    **This can be found in the last two sentences of the last paragraph in milestones.pdf**

  - for running complete tournaments on a single computer 
    
    **We didn't include the possibility of running a tournament in our milestones.**

  - for running remote tournaments on a network
   
    **We failed to mention remote tournaments in our milestones as well.**




- for the English of your memo, you may wish to check the following:

  - is each paragraph dedicated to a single topic? does it come with a
    thesis statement that specifies the topic?
    
    **We have a thesis and overview of our systems.pdf in the first paragraph.  Each paragraph in both memos is dedicated to a single topic.**

  - do sentences make a point? do they run on?
    
    **We proof read our memos and double checked our punctuation. We wanted to avoid run on sentences and comma splices. We'll refer to our entire memos for the grammar specific inquiries.**
  - do sentences connect via old words/new words so that readers keep
    reading?
    
    **Yes!**

  - are all sentences complete? Are they missing verbs? Objects? Other
    essential words?
    
    **Yes, we both proof read the memos to make sure.**

  - did you make sure that the spelling is correct? ("It's" is *not* a
    possessive; it's short for "it is". "There" is different from
    "their", a word that is too popular for your generation.)
    
    **Yes, Google Drive warns us of spelling errors.**


The ideal feedback are pointers to specific sentences in your memo.
For PDF, the paragraph/sentence number suffices. 

For **code repos**, we will expect GitHub line-specific links. 


