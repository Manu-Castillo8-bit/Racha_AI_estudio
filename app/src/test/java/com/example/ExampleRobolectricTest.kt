package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RACHA", appName)
  }

  @Test
  fun `test streak calculation when today is studied`() {
    val today = DateUtils.getTodayDateString()
    val yesterday = DateUtils.getDateStringForDaysAgo(1)
    val twoDaysAgo = DateUtils.getDateStringForDaysAgo(2)

    val streak = DateUtils.calculateCurrentStreak(setOf(today, yesterday, twoDaysAgo))
    assertEquals(3, streak)
  }

  @Test
  fun `test streak calculation when today is not yet studied but yesterday is`() {
    val yesterday = DateUtils.getDateStringForDaysAgo(1)
    val twoDaysAgo = DateUtils.getDateStringForDaysAgo(2)

    val streak = DateUtils.calculateCurrentStreak(setOf(yesterday, twoDaysAgo))
    assertEquals(2, streak)
  }

  @Test
  fun `test last 7 days returns exactly 7 items`() {
    val days = DateUtils.getLast7Days(emptySet())
    assertEquals(7, days.size)
    assertEquals(true, days.last().isToday)
  }
}
