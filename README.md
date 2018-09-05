# Jira software coverage

This module defines Jira Software Virtual Users coverage

## Jira Software actions

Action              | Activity              |
------------------- | ----------------------
View Board          | Go to Board → Wait for Board to display columns
Browse Boards       | Go to Browse Boards Page → Wait for the board list to load

## Jira Software scenario

Jira Software scenario covers both Jira Core and Jira Software actions. 
Check [jira-actions module](https://bitbucket.org/atlassian/jira-actions/src/master/README.md) 
to read about Jira Core actions. For each VU these actions are shuffled in a specific proportion and executed in a loop.

Action              | Composition              |
------------------- | ----------------------
Create Issue        | 4.2% (5/119)
Create Issue Submit | 4.2% (5/119)
Search with JQL     | 16.8% (20/119)
View Issue          | 46.2% (55/119)
Project Summary     | 4.2% (5/119)
View Dashboard      | 8.4% (10/119)
Edit Issue          | 4.2% (5/119)
Edit Issue Submit   | 4.2% (5/119)
Add Comment         | 1.6% (2/119)
Add Comment Submit  | 1.6% (2/119)
Browse Projects     | 4.2% (5/119)
View Board          | 8.4% (10/119)
Browse Boards       | 1.6% (2/119)

## Reporting issues

We track all the changes in a [public issue tracker](https://ecosystem.atlassian.net/secure/RapidBoard.jspa?rapidView=457&projectKey=JPERF).
All the suggestions and bug reports are welcome.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

##License
Copyright (c) 2018 Atlassian and others.
Apache 2.0 licensed, see [LICENSE.txt](LICENSE.txt) file.