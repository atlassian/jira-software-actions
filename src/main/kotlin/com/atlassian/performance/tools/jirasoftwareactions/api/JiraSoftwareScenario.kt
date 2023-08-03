package com.atlassian.performance.tools.jirasoftwareactions.api

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.WebJira
import com.atlassian.performance.tools.jiraactions.api.action.*
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.adaptive.*
import com.atlassian.performance.tools.jiraactions.api.scenario.Scenario
import com.atlassian.performance.tools.jiraactions.api.scenario.addMultiple
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.BrowseBoardsAction
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.ViewBoardAction
import com.atlassian.performance.tools.jirasoftwareactions.api.actions.WorkOnBacklog
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AdaptiveBoardMemory
import java.util.function.Predicate

class JiraSoftwareScenario : Scenario {
    /**
     * Different actions have different proportions. The goal is to make the traffic more realistic.
     */
    override fun getActions(
        jira: WebJira,
        seededRandom: SeededRandom,
        meter: ActionMeter
    ): List<Action> {
        val jqlMemory = AdaptiveJqlMemory(seededRandom)
        val projectMemory = JqlRememberingProjectMemory
            .Builder(
                delegate = AdaptiveProjectMemory(random = seededRandom),
                jqlMemory = LimitedJqlMemory(
                    delegate = jqlMemory,
                    limit = 3
                )
            )
            .build()
        val issueKeyMemory = AdaptiveIssueKeyMemory(seededRandom)
        val issueMemory = AdaptiveIssueMemory(issueKeyMemory, seededRandom)
        val agileBoardMemory = AdaptiveBoardMemory<AgileBoard>(seededRandom)
        val scrumBoardMemory = AdaptiveBoardMemory<ScrumBoard>(seededRandom)
        val commentMemory = AdaptiveCommentMemory(seededRandom)
        val scenario: MutableList<Action> = mutableListOf()
        val createIssue = CreateIssueAction(
            jira = jira,
            meter = meter,
            seededRandom = seededRandom,
            projectMemory = projectMemory
        )
        val searchWithJql = SearchJqlAction(
            jira = jira,
            meter = meter,
            jqlMemory = jqlMemory,
            issueKeyMemory = issueKeyMemory
        )
        val viewIssue = ViewIssueAction.Builder(jira, meter)
            .issueKeyMemory(issueKeyMemory)
            .issueMemory(issueMemory)
            .jqlMemory(jqlMemory)
            .build()
        val projectSummary = ProjectSummaryAction(
            jira = jira,
            meter = meter,
            projectMemory = projectMemory
        )
        val viewDashboard = ViewDashboardAction(
            jira = jira,
            meter = meter
        )
        val editIssue = EditIssueAction(
            jira = jira,
            meter = meter,
            issueMemory = issueMemory
        )
        val addComment = AddCommentAction(
            jira = jira,
            meter = meter,
            issueMemory = issueMemory
        )
        val browseProjects = BrowseProjectsAction(
            jira = jira,
            meter = meter,
            projectMemory = projectMemory
        )

        val jiraSoftware = WebJiraSoftware(jira)
        val viewBoard = ViewBoardAction(
            jiraSoftware = jiraSoftware,
            meter = meter,
            boardMemory = agileBoardMemory,
            issueKeyMemory = issueKeyMemory
        ) { it.issuesOnBoard != 0 }
        val browseBoards = BrowseBoardsAction(
            jiraSoftware = jiraSoftware,
            meter = meter,
            boardsMemory = agileBoardMemory,
            scrumBoardsMemory = scrumBoardMemory
        )
        val viewComment = ViewCommentAction(
            jira = jira,
            meter = meter,
            commentMemory = commentMemory
        )
        val viewHistoryTabAction = ViewHistoryTabAction(
            jira = jira,
            meter = meter,
            issueKeyMemory = issueKeyMemory
        )

        val workOnBacklog = WorkOnBacklog.Builder(jiraSoftware, meter)
            .seededRandom(seededRandom)
            .boardMemory(scrumBoardMemory)
            .build()
        val actionProportions = mapOf(
            createIssue to 5,
            searchWithJql to 20,
            viewIssue to 55,
            projectSummary to 5,
            viewDashboard to 10,
            editIssue to 5,
            addComment to 2,
            browseProjects to 5,
            viewBoard to 10,
            workOnBacklog to 10,
            browseBoards to 2,
            viewComment to 2,
            viewHistoryTabAction to 2
        )
        actionProportions.entries.forEach { scenario.addMultiple(element = it.key, repeats = it.value) }
        scenario.shuffle(seededRandom.random)
        return scenario
    }
}
