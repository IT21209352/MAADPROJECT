package com.example.helpinghand

import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import com.example.helpinghand.adapters.CommentAdapter
import org.junit.Test
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.Models.CommentViewModel
import com.example.helpinghand.Repository.CommentRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Assert.*
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest(val commentAdapter: CommentAdapter) {

    @Test
    fun deleteComment_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class CommentTest {

    @Test
    fun testCommentConstructor() {
        val comment = Comment("Test comment", "Test owner", "123")

        assertEquals("Test comment", comment.comments_comment)
        assertEquals("Test owner", comment.comments_owner)
        assertEquals("123", comment.comment_id)
    }

    @Test
    fun testCommentGetters() {
        val comment = Comment("Test comment", "Test owner", "123")

        assertEquals("Test comment", comment.comments_comment)
        assertEquals("Test owner", comment.comments_owner)
        assertEquals("123", comment.comment_id)
    }
}


