package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.CompatibleBoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AdaptiveBoardIdMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewBoardAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardMemory: BoardMemory<AgileBoard>,
    private val issueKeyMemory: IssueKeyMemory,
    private val filter: (AgileBoard) -> Boolean
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardMemory: BoardMemory<AgileBoard>,
        issueKeyMemory: IssueKeyMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardMemory = boardMemory,
        issueKeyMemory = issueKeyMemory,
        filter = { true }
    )

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardIdMemory: AdaptiveBoardIdMemory,
        issueKeyMemory: IssueKeyMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardMemory = CompatibleBoardMemory(boardIdMemory),
        issueKeyMemory = issueKeyMemory,
        filter = { true }
    )

    override fun run() {
        val board = boardMemory.recall(filter)

        if (board == null) {
            logger.debug("Skipping View Board. I have no knowledge of Boards.")
        } else {
            meter.measure(
                key = VIEW_BOARD,
                action = {
                    val issueBoard = jiraSoftware.goToViewBoard(board.id)
                    issueBoard.waitForBoard()
                    issueBoard
                },
                observation = { issueBoard ->
                    val issueKeys = issueBoard.getIssueKeys()
                    issueKeyMemory.remember(issueKeys)
                    board.issuesOnBoard = issueKeys.size
                    IssuesOnBoard(issues = issueKeys.size).serialize()
                }
            )
        }
    }
}