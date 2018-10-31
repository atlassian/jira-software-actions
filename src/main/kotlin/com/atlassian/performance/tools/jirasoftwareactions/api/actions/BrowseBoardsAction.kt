package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.BROWSE_BOARDS
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jirasoftwareactions.CompatibleBoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.CompatibleScrumBoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class BrowseBoardsAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardsMemory: BoardMemory<AgileBoard>,
    private val scrumBoardsMemory: BoardMemory<ScrumBoard>
) : Action {

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardsMemory: AgileBoardIdMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardsMemory = CompatibleBoardMemory(boardsMemory),
        scrumBoardsMemory = object : BoardMemory<ScrumBoard> {
            override fun recall(): ScrumBoard? {
                return null
            }

            override fun recall(filter: (ScrumBoard) -> Boolean): ScrumBoard? {
                return null
            }

            override fun remember(memories: Collection<ScrumBoard>) {

            }
        }
    )

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardsMemory: AgileBoardIdMemory,
        scrumBoardsMemory: AgileBoardIdMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardsMemory = CompatibleBoardMemory(boardsMemory),
        scrumBoardsMemory = CompatibleScrumBoardMemory(scrumBoardsMemory)
    )

    override fun run() {
        val browseBoardsPage =
            meter.measure(BROWSE_BOARDS) { jiraSoftware.goToBrowseBoards().waitForBoardsList() }

        boardsMemory.remember(browseBoardsPage.getBoardIds().map { AgileBoard(it) })
        scrumBoardsMemory.remember(browseBoardsPage.getScrumBoardIds().map { ScrumBoard(it) })
    }
}