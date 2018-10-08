package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.BROWSE_BOARDS
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AgileBoardIdMemory
import net.jcip.annotations.NotThreadSafe

@NotThreadSafe
class BrowseBoardsAction(
    private val jiraSoftware: WebJiraSoftware,
    private val meter: ActionMeter,
    private val boardsMemory: AgileBoardIdMemory,
    private val scrumBoardsMemory: AgileBoardIdMemory
) : Action {

    constructor(
        jiraSoftware: WebJiraSoftware,
        meter: ActionMeter,
        boardsMemory: AgileBoardIdMemory
    ) : this(
        jiraSoftware = jiraSoftware,
        meter = meter,
        boardsMemory = boardsMemory,
        scrumBoardsMemory = object : AgileBoardIdMemory {
            override fun recall(): String? {
                return null
            }

            override fun remember(memories: Collection<String>) {

            }
        }
    )

    override fun run() {
        val browseBoardsPage =
            meter.measure(BROWSE_BOARDS) { jiraSoftware.goToBrowseBoards().waitForBoardsList() }

        boardsMemory.remember(browseBoardsPage.getBoardIds())
        scrumBoardsMemory.remember(browseBoardsPage.getScrumBoardIds())
    }
}