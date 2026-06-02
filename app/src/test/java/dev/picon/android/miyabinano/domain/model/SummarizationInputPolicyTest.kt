package dev.picon.android.miyabinano.domain.model

import dev.picon.android.miyabinano.domain.repository.TestDataRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SummarizationInputPolicyTest {
    @Test
    fun articleAtHardBoundary_isRejected() {
        val evaluation = SummarizationInputPolicy.evaluate("a".repeat(400))

        assertFalse(evaluation.meetsMinimum)
    }

    @Test
    fun articleAboveHardBoundary_isAcceptedButCanRemainSuboptimal() {
        val evaluation = SummarizationInputPolicy.evaluate("word ".repeat(81))

        assertTrue(evaluation.meetsMinimum)
        assertFalse(evaluation.meetsRecommendation)
    }

    @Test
    fun summarizationFixtures_areValidAndIncludeRecommendedRangeArticle() {
        val evaluations = TestDataRepository.summarizationTests.map { testCase ->
            SummarizationInputPolicy.evaluate(testCase.inputText)
        }

        assertTrue(evaluations.all { it.meetsMinimum })
        assertTrue(evaluations.any { it.meetsRecommendation })
    }
}
