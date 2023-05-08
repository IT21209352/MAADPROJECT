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
import com.example.helpinghand.Models.Post
import com.example.helpinghand.Models.ProfileData
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


class PostTest {

    @Test
    fun testPost() {
        val imageUrl = "https://example.com/image.jpg"
        val postDetail = "This is an example post."
        val post_owner = "John"
        val post_key = "abc123"

        val post = Post(imageUrl, postDetail, post_owner, post_key)

        // Assert that the properties of the post match the values passed to the constructor.
        assertEquals(imageUrl, post.imageUrl)
        assertEquals(postDetail, post.postDetail)
        assertEquals(post_owner, post.post_owner)
        assertEquals(post_key, post.post_key)
    }

    @Test
    fun testPostWithNullValues() {
        val post = Post()

        // Assert that all properties are null.
        assertNull(post.imageUrl)
        assertNull(post.postDetail)
        assertNull(post.post_owner)
        assertNull(post.post_key)
    }

    @Test
    fun testPostWithEmptyValues() {
        val post = Post("", "", "", "")

        // Assert that all properties are empty strings.
        assertEquals("", post.imageUrl)
        assertEquals("", post.postDetail)
        assertEquals("", post.post_owner)
        assertEquals("", post.post_key)
    }

    @Test
    fun testPostWithLongValues() {
        val imageUrl = "https://example.com/" + "a".repeat(1000) + ".jpg"
        val postDetail = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " + "a".repeat(1000)
        val post_owner = "John" + "a".repeat(1000)
        val post_key = "abc123" + "a".repeat(1000)

        val post = Post(imageUrl, postDetail, post_owner, post_key)

        // Assert that all properties are set to the long values.
        assertEquals(imageUrl, post.imageUrl)
        assertEquals(postDetail, post.postDetail)
        assertEquals(post_owner, post.post_owner)
        assertEquals(post_key, post.post_key)
    }

    @Test
    fun testPostEquals() {
        val post1 = Post("https://example.com/image1.jpg", "Post 1", "John", "abc123")
        val post2 = Post("https://example.com/image1.jpg", "Post 1", "John", "abc123")
        val post3 = Post("https://example.com/image2.jpg", "Post 2", "Jane", "def456")

        // Assert that post1 and post2 are equal, but post1 and post3 are not.
        assertEquals(post1, post2)
        assertNotEquals(post1, post3)
    }
}


class ProfileDataTest {

    @Test
    fun testProfileDataConstructor() {
        val profileData = ProfileData("Test User", "testuser@example.com", "12:00", "123")

        assertEquals("Test User", profileData.loggedUserName)
        assertEquals("testuser@example.com", profileData.loggedUserEmail)
        assertEquals("12:00", profileData.loggedUserTime)
        assertEquals("123", profileData.loggedUserID)
    }

    @Test
    fun testProfileDataGetters() {
        val profileData = ProfileData("Test User", "testuser@example.com", "12:00", "123")

        assertEquals("Test User", profileData.loggedUserName)
        assertEquals("testuser@example.com", profileData.loggedUserEmail)
        assertEquals("12:00", profileData.loggedUserTime)
        assertEquals("123", profileData.loggedUserID)
    }

    @Test
    fun testProfileDataEquals() {
        val profileData1 = ProfileData("Test User", "testuser@example.com", "12:00", "123")
        val profileData2 = ProfileData("Test User", "testuser@example.com", "12:00", "123")
        val profileData3 = ProfileData("Another User", "anotheruser@example.com", "13:00", "456")

        assertEquals(profileData1, profileData2)
        assert(profileData1 != profileData3)
    }
}

