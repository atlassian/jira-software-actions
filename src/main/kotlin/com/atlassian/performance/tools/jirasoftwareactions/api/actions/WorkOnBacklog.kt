package com.atlassian.performance.tools.jirasoftwareactions.api.actions

import com.atlassian.performance.tools.jiraactions.api.ActionType
import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.action.Action
import com.atlassian.performance.tools.jiraactions.api.measure.ActionMeter
import com.atlassian.performance.tools.jiraactions.api.observation.IssuesOnBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.WebJiraSoftware
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.AdaptiveBoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.memories.BoardMemory
import com.atlassian.performance.tools.jirasoftwareactions.api.page.ViewBacklogPage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.function.Predicate

class WorkOnBacklog private constructor(
    private val meter: ActionMeter,
    private val editSprintChance: Float,
    private val seededRandom: SeededRandom,
    private val jiraSoftware: WebJiraSoftware,
    private val filter: Predicate<ScrumBoard>,
    private val boardMemory: BoardMemory<ScrumBoard>
) : Action {
    companion object {
        @JvmField
        val VIEW_BACKLOG = ActionType("View Backlog") { }

        @JvmField
        val EDIT_SPRINT = ActionType("Edit sprint") { }
    }

    private val logger: Logger = LogManager.getLogger(this::class.java)
    private val newSprintName = "New Sprint Name"

    override fun run() {
        val board = boardMemory.recall { filter.test(it) }

        if (board == null) {
            logger.debug("Skipping View Backlog. I have no knowledge of Boards.")
        } else {
            val backlog = viewBacklog(board)
            if (roll(editSprintChance)) {
                editSprint(backlog)
            }
        }
    }

    private fun editSprint(backlog: ViewBacklogPage) {
        backlog.closePopups()
        val sprint = backlog.listSprints().firstOrNull()
        if (sprint != null) {
            meter.measure(
                key = EDIT_SPRINT,
                action = {
                    sprint
                        .openEditSprintDialog()
                        .waitForDialog()
                        .editSprintName(newSprintName)
                        .validateSprintName(newSprintName)
                }
            )
        }
    }

    private fun viewBacklog(board: ScrumBoard): ViewBacklogPage {
        return meter.measure(
            key = VIEW_BACKLOG,
            action = {
                jiraSoftware
                    .goToBacklog(board.id)
                    .also { it.waitForBacklog() }
            },
            observation = { backlogBoard ->
                val issueKeys = backlogBoard.getIssueKeys()
                board.issuesInBacklog = issueKeys.size
                IssuesOnBoard(issues = issueKeys.size).serialize()
            }
        )
    }

    private fun roll(
        chance: Float
    ): Boolean = (seededRandom.random.nextFloat() < chance)

    class Builder(
        private var jiraSoftware: WebJiraSoftware,
        private var meter: ActionMeter
    ) {
        private var editSprintChance: Float = 0.1f
        private var seededRandom: SeededRandom = SeededRandom(123)
        private var filter: Predicate<ScrumBoard> = Predicate { it.issuesInBacklog != 0 }
        private var boardMemory: BoardMemory<ScrumBoard> = AdaptiveBoardMemory(seededRandom)

        fun jiraSoftware(jiraSoftware: WebJiraSoftware) = apply { this.jiraSoftware = jiraSoftware }
        fun meter(meter: ActionMeter) = apply { this.meter = meter }
        fun editSprintChance(editSprintChance: Float) = apply { this.editSprintChance = editSprintChance }
        fun seededRandom(seededRandom: SeededRandom) = apply { this.seededRandom = seededRandom }
        fun filter(filter: Predicate<ScrumBoard>) = apply { this.filter = filter }
        fun boardMemory(boardMemory: BoardMemory<ScrumBoard>) = apply { this.boardMemory = boardMemory }

        fun build() = WorkOnBacklog(
            jiraSoftware = jiraSoftware,
            meter = meter,
            editSprintChance = editSprintChance,
            seededRandom = seededRandom,
            filter = filter,
            boardMemory = boardMemory
        )
    }
}
