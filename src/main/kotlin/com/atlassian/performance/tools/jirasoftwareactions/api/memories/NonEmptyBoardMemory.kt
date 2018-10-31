package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jiraactions.api.memories.Memory
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board

/**
 * This memory supports only non-empty boards and boards with an unknown number of issues.
 */

@Deprecated(message = "Use ViewBoardAction.filter")
class NonEmptyBoardMemory<T : Board>(
    private val random: SeededRandom
) : Memory<T> {
    private val boards = mutableSetOf<T>()

    override fun recall(): T? {
        return random.pick(boards.toList())
    }

    override fun remember(memories: Collection<T>) {
        memories.forEach(::add)
    }

    private fun add(board: T) {
        if (board.isWorthVisiting()) {
            if (!boards.add(board)) {
                boards.remove(board)
                boards.add(board)
            }
        } else if (boards.contains(board)) {
            boards.remove(board)
        }
    }
}