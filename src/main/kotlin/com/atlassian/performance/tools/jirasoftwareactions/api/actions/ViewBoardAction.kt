package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.VIEW_BOARD
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.memories.IssueKeyMemory
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.CompatibleBoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AdaptiveBoardIdMemory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ViewBoardAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardMemory: Memory<Board>,
    private val issueKeyMemory: IssueKeyMemory
) : Action {
    private val logger: Logger = LogManager.getLogger(this::class.java)

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardIdMemory: AdaptiveBoardIdMemory,
        issueKeyMemory: IssueKeyMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardMemory = CompatibleBoardMemory(boardIdMemory),
        issueKeyMemory = issueKeyMemory
    )

    override fun run() {
        val board = boardMemory.recall()

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
                    boardMemory.remember(listOf(AgileBoard(
                        id = board.id,
                        issuesOnBoard = issueKeys.size
                    )))
                    IssuesOnBoard(issues = issueKeys.size).serialize()
                }
            )
        }
    }
}