package com.example.trendingapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.trendingapp.model.Trending
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrendingDaoTest {

    private lateinit var trendingDao: TrendingDao
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        trendingDao = database.trendingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldInsertAndRetrieveTrendingList() = runBlocking {
        val trendingList = listOf(
            Trending(
                userName = "User1",
                projectName = "Project1",
                description = "Desc1",
                language = "Kotlin",
                starCount = "100"
            ),
            Trending(
                userName = "User2",
                projectName = "Project2",
                description = "Desc2",
                language = "Java",
                starCount = "50"
            )
        )
        trendingDao.insertTrendingList(trendingList)
        val retrievedList = trendingDao.getAllTrending().first()

        assertTrendingListsEqualIgnoringIds(trendingList, retrievedList)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTrendingList() = runBlocking {
        // Insert test data
        val trendingList = listOf(
            Trending(
                userName = "User1",
                projectName = "Project1",
                description = "Desc1",
                language = "Kotlin",
                starCount = "100"
            ),
            Trending(
                userName = "User2",
                projectName = "Project2",
                description = "Desc2",
                language = "Java",
                starCount = "50"
            )
        )
        trendingDao.insertTrendingList(trendingList)

        // Get the list from the database
        val retrievedList = trendingDao.getTrendingList()

        // Check if the retrieved list matches the inserted list
        assert(retrievedList.size == trendingList.size)
        assert(retrievedList.containsAll(trendingList))
    }

    @Test
    @Throws(Exception::class)
    fun removeAll() = runBlocking {
        // Insert test data
        val trendingList = listOf(
            Trending(
                userName = "User1",
                projectName = "Project1",
                description = "Desc1",
                language = "Kotlin",
                starCount = "100"
            ),
            Trending(
                userName = "User2",
                projectName = "Project2",
                description = "Desc2",
                language = "Java",
                starCount = "50"
            )
        )
        trendingDao.insertTrendingList(trendingList)

        // Remove all entries
        trendingDao.removeAll()

        // Get the list from the database after removal
        val retrievedList = trendingDao.getTrendingList()

        // Check if the retrieved list is empty
        assert(retrievedList.isEmpty())
    }

    private fun assertTrendingListsEqualIgnoringIds(
        expected: List<Trending>,
        actual: List<Trending>
    ) {
        assert(expected.size == actual.size)

        for (i in expected.indices) {
            val expectedItem = expected[i]
            val actualItem = actual[i]

            assert(expectedItem.userName == actualItem.userName)
            assert(expectedItem.projectName == actualItem.projectName)
            assert(expectedItem.description == actualItem.description)
            assert(expectedItem.language == actualItem.language)
            assert(expectedItem.starCount == actualItem.starCount)
        }
    }
}
