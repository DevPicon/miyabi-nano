package dev.picon.android.miyabinano.domain.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TestDataRepositoryTest {
    @Test
    fun fixtures_haveUniqueStableIdsSizeBandsAndQualitativeChecks() {
        val fixtures = TestDataRepository.getAllTestCases()

        assertEquals(fixtures.size, fixtures.map { it.id }.distinct().size)
        assertTrue(fixtures.all { it.id.isNotBlank() })
        assertTrue(fixtures.all { it.qualitativeCheck.isNotBlank() })
        assertTrue(fixtures.all { it.sizeBand.name.isNotBlank() })
    }
}
