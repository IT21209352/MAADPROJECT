package com.example.helpinghand

import android.widget.EditText
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.Models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.*




/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.helpinghand", appContext.packageName)
    }
}


@RunWith(AndroidJUnit4::class)
class MainActivity2InstrumentedTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity2>()

    @Test
    fun testNavigation() {
        onView(withId(R.id.imgBtnHome)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnChat)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnNewPost)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnMyPosts)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
        onView(withId(R.id.imgBtnProfile)).perform(click())
        onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()))
    }
}


class CommentTest {

    private lateinit var firebaseDatabase: DatabaseReference

    @Before
    fun setUp() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("comments")
    }

    @Test
    fun test_save_comment_to_db() {
        val texts = "Test comment"
        val user = "Test user"
        val id = "00001"
        // Call the function to save the comment
        saveComment(texts,user,id)
        // Verify if the comment has been saved in the Firebase database
        val query = firebaseDatabase.child("post_comments").orderByChild("comments_owner").equalTo(user)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var commentFound = false
                for (childSnapshot in snapshot.children) {
                    val comment = childSnapshot.getValue(Comment::class.java)
                    if (comment != null && comment.comments_comment == texts && comment.comments_owner == user) {
                        commentFound = true
                        break
                    }
                }
                assertTrue("Comment not found in the database", commentFound)
            }
            override fun onCancelled(error: DatabaseError) {
                fail("Database read failed: ${error.message}")
            }
        })
    }

    private fun saveComment(text : String,user : String,id :String) {

        val textMap = hashMapOf(
            "comments_comment" to text,
            "comments_owner" to user,
            "comment_id" to id
        )
        val firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("comments")
        val commentID = firebaseDatabase.child("post_comments").push().key
    }


}
//class FirebaseSendMessageTest {
//
//    @get:Rule
//    var firebaseRule = FirebaseTestRule()
//
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var mDbRef: DatabaseReference
//
//    @Before
//    fun setUp() {
//        mAuth = FirebaseAuth.getInstance()
//        mDbRef = FirebaseDatabase.getInstance().reference.child("test-messages")
//    }
//
//    @After
//    fun tearDown() {
//        mDbRef.setValue(null)
//    }
//
//    @Test
//    fun testSendMessage() {
//        // Mock user id and chat id
//        val userId = "1"
//        val otherUserId = "2"
//        val chatId = "123"
//
//        // Set up message text
//        val messageText = "Hello, world!"
//
//        // Set up message object
//        val message = Messages(chatId, "456", messageText, userId, otherUserId, 123456789, false)
//
//        // Generate random message id
//        val messageId = mDbRef.push().key!!
//
//        // Save message to database
//        mDbRef.child(messageId).setValue(message)
//
//        // Retrieve message from database
//        var messageFromDb: Messages? = null
//        val latch = CountDownLatch(1)
//        mDbRef.child(messageId).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                messageFromDb = snapshot.getValue(Messages::class.java)
//                latch.countDown()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                fail("onCancelled was called")
//            }
//        })
//        latch.await()
//
//        // Assert that message was saved correctly
//        assertNotNull(messageFromDb)
//        assertEquals(message.chatId, messageFromDb!!.chatId)
//        assertEquals(message.messageId, messageFromDb!!.messageId)
//        assertEquals(message.message, messageFromDb!!.message)
//        assertEquals(message.senderId, messageFromDb!!.senderId)
//        assertEquals(message.receiverId, messageFromDb!!.receiverId)
//        assertEquals(message.time, messageFromDb!!.time)
//        assertEquals(message.isRead, messageFromDb!!.isRead)
//    }
//}

@RunWith(AndroidJUnit4::class)
class ChatActivityTest{

    @Test
    fun testButtonClickListener(){

        //Start the activity under test
        val scenario = ActivityScenario.launch(ChatMessagesActivity::class.java)

        //Click the button
        onView(withId(R.id.imgBackBtn)).perform(click())

        //Verify that the activity is finished
        scenario.onActivity { activity->
            assertTrue(activity.isFinishing)

        }
    }


}

@RunWith(AndroidJUnit4::class)
class ChatFragmentNavigation {

    @Test
    fun testNav() {

        //Start the activity under test
        ActivityScenario.launch(MainActivity2::class.java)

        //Click the button
        onView(withId(R.id.imgBtnChat)).perform(click())

        //Check whether the correct fragment is replaced
        onView(withId(R.id.fragmentContainerView)).check(ViewAssertions.matches(isDisplayed()))
    }
}





