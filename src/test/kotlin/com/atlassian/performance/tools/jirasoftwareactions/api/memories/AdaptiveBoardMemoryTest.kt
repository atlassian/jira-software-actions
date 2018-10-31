package com.atlassian.performance.tools.jirasoftwareactions.api.memories

import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AdaptiveBoardMemoryTest {
    private val adaptiveBoardMemory: AdaptiveBoardMemory<Board> = AdaptiveBoardMemory(SeededRandom())

    @Test
    fun testRemember() {
        val board = AgileBoard("board")
        adaptiveBoardMemory.remember(listOf(board))

        assertEquals(adaptiveBoardMemory.recall(), board)
    }
}