package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewBoardAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardIdMemory: AgileBoardIdMemory,
    private val issueKeyMemory: IssueKeyMemory
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    override fun run() {
        val id = boardIdMemory.recall()
        if (id == null) {
            logger.debug("Skipping View Board. I have no knowledge of Boards.")
        } else {
            meter.measure(
                key = VIEW_BOARD,
                action = {
                    val issueBoard = jiraSoftware.goToViewBoard(id)
                    issueBoard.waitForBoard()
                    issueBoard
                },
                observation = { issueBoard ->
                    val issueKeys = issueBoard.getIssueKeys()
                    issueKeyMemory.remember(issueKeys)
                    IssuesOnBoard(issues = issueKeys.size).serialize()
                }
            )
        }
    }
}