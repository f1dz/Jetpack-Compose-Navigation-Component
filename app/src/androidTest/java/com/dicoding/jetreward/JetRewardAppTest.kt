package com.dicoding.jetreward

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.dicoding.jetreward.model.FakeRewardDataSource
import com.dicoding.jetreward.ui.navigation.Screen
import com.dicoding.jetreward.ui.theme.JetRewardTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JetRewardAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetRewardTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                JetRewardApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination(){
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailWithData(){
        composeTestRule.onNodeWithTag("RewardList").performScrollToIndex(10)
        composeTestRule.onNodeWithText(FakeRewardDataSource.dummyRewards[10].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailReward.route)
        composeTestRule.onNodeWithText(FakeRewardDataSource.dummyRewards[10].title).assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavShowingCorrectPage(){
        composeTestRule.onNodeWithTag(Screen.Home.route).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithTag(Screen.Cart.route).performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithTag(Screen.Profile.route).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
    }

    @Test
    fun navHost_backIconClick_navigateToHome(){
        composeTestRule.onNodeWithText(FakeRewardDataSource.dummyRewards[3].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailReward.route)
        composeTestRule.onNodeWithTag("BackButton").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_checkout_rightBackStack(){
        composeTestRule.onNodeWithText(FakeRewardDataSource.dummyRewards[4].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailReward.route)
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick()
        composeTestRule.onNodeWithContentDescription("Order Button").performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }
}