package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewBacklogAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardIdMemory: AgileBoardIdMemory
) : Action {
    companion object {
        @JvmField
        val VIEW_BACKLOG = ActionType("View Backlog") { Unit }
    }

    private val logger: Logger = LogManager.getLogger(this::class.java)


    override fun run() {
        val id = boardIdMemory.recall()

        if (id == null) {
            logger.debug("Skipping View Backlog. I have no knowledge of Boards.")
        } else {
            meter.measure(
                key = VIEW_BACKLOG,
                action = {
                    val backlogBoard = jiraSoftware.goToBacklog(id)
                    backlogBoard.waitForBacklog()
                    backlogBoard
                },
                observation = { backlogBoard ->
                    val issueKeys = backlogBoard.getIssueKeys()
                    IssuesOnBoard(issues = issueKeys.size).serialize()
                }
            )
        }
    }
}