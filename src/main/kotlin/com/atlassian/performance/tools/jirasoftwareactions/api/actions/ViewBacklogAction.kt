package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.CompatibleScrumBoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewBacklogAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardMemory: BoardMemory<ScrumBoard>,
    private val filter: (ScrumBoard) -> Boolean
) : Action {
    companion object {
        @JvmField
        val VIEW_BACKLOG = ActionType("View Backlog") { Unit }
    }

    private val logger: Logger = LogManager.getLogger(this::class.java)

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardMemory: BoardMemory<ScrumBoard>
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardMemory = boardMemory,
        filter = { true }
    )

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardIdMemory: AgileBoardIdMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardMemory = CompatibleScrumBoardMemory(boardIdMemory),
        filter = { true }
    )

    override fun run() {
        val board = boardMemory.recall(filter)

        if (board == null) {
            logger.debug("Skipping View Backlog. I have no knowledge of Boards.")
        } else {
            meter.measure(
                key = VIEW_BACKLOG,
                action = {
                    val backlogBoard = jiraSoftware.goToBacklog(board.id)
                    backlogBoard.waitForBacklog()
                    backlogBoard
                },
                observation = { backlogBoard ->
                    val issueKeys = backlogBoard.getIssueKeys()
                    board.issuesInBacklog = issueKeys.size
                    IssuesOnBoard(issues = issueKeys.size).serialize()
                }
            )
        }
    }
}