# Jira software coverage

This module defines Jira Software Virtual Users coverage

## Jira Software actions

Action              | Activity              |
------------------- | ----------------------
View Board          | Go to Board → Wait for Board to display columns
View Backlog        | Go to Backlog → Wait for the backlog issue list
Browse Boards       | Go to Browse Boards Page → Wait for the board list to load

## Jira Software scenario

Jira Software scenario covers both Jira Core and Jira Software actions. 
Check [jira-actions module](https://bitbucket.org/atlassian/jira-actions/src/master/README.md) 
to read about Jira Core actions. For each VU these actions are shuffled in a specific proportion and executed in a loop.

Action              | Composition              |
------------------- | ----------------------
Create Issue        | 3.9% (5/129)
Search with JQL     | 15.5% (20/129)
View Issue          | 42.6% (55/129)
Project Summary     | 3.9% (5/129)
View Dashboard      | 7.8% (10/129)
Edit Issue          | 3.9% (5/129)
Add Comment         | 1.6% (2/129)
Browse Projects     | 3.9% (5/129)
View Board          | 7.8% (10/129)
View Backlog        | 7.8% (10/129)
Browse Boards       | 1.6% (2/129)

## Reporting issues

We track all the changes in a [public issue tracker](https://ecosystem.atlassian.net/secure/RapidBoard.jspa?rapidView=457&projectKey=JPERF).
All the suggestions and bug reports are welcome.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

##License
Copyright (c) 2018 Atlassian and others.
Apache 2.0 licensed, see [LICENSE.txt](LICENSE.txt) file.