package com.atlassian.performance.tools.jirasoftwareactions.api.memories


import com.atlassian.performance.tools.jiraactions.api.SeededRandom
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.AgileBoard
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.Board
import com.atlassian.performance.tools.jirasoftwareactions.api.boards.ScrumBoard
import org.junit.Assert.assertEquals
import org.junit.Test

internal class NonEmptyBoardMemoryTest {
    private val emptyBoardMemory: NonEmptyBoardMemory<Board> = NonEmptyBoardMemory(SeededRandom())

    @Test
    fun testRejectEmptyBoard() {
        val emptyAgileBoard = AgileBoard("agile", 0)
        val emptyScrumBoard = ScrumBoard("scrum", 0, 0)

        emptyBoardMemory.remember(listOf(emptyAgileBoard, emptyScrumBoard))

        assertEquals(emptyBoardMemory.recall(), null)
    }

    @Test
    fun testRememberNonEmptyBoard() {
        val agileBoard = AgileBoard("agile", 1)

        emptyBoardMemory.remember(listOf(agileBoard))

        assertEquals(emptyBoardMemory.recall(), agileBoard)
    }

    @Test
    fun testRememberUnknownBoard() {
        val agileBoard = AgileBoard("unknown")

        emptyBoardMemory.remember(listOf(agileBoard))

        assertEquals(emptyBoardMemory.recall(), agileBoard)
    }

    @Test
    fun testRemoveEmptyBoard() {
        val unknownBoard = AgileBoard("unknown")

        // should remember board because has no knowledge about number of issues
        emptyBoardMemory.remember(listOf(unknownBoard))

        val knownBoard = AgileBoard(unknownBoard.id, 0)

        // should remove board because is empty
        emptyBoardMemory.remember(listOf(knownBoard))

        assertEquals(emptyBoardMemory.recall(), null)
    }
}