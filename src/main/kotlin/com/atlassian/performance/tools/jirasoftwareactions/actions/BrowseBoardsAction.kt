package com.atlassian.performance.tools.jirasoftwareactions.actions

import com.atlassian.performance.tools.jiraactions.BROWSE_BOARDS
import com.atlassian.performance.tools.jiraactions.action.Action
import com.atlassian.performance.tools.jiraactions.measure.ActionMeter
import com.atlassian.performance.tools.jirasoftwareactions.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.memories.AgileBoardIdMemory
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class BrowseBoardsAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardsMemory: AgileBoardIdMemory
) : Action {
    override fun run() {
        val browseBoardsPage =
            meter.measure(BROWSE_BOARDS) { jiraSoftware.goToBrowseBoards().waitForBoardsList() }

        boardsMemory.remember(browseBoardsPage.getBoardIds())
    }
}