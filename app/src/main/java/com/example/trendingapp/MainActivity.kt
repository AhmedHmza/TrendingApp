package com.example.trendingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.trendingapp.model.Trending
import com.example.trendingapp.reusableComposables.ListItemWithImageTitleDescription
import com.example.trendingapp.ui.theme.TrendingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetch(false)
        setContent {
            val uiState = viewModel.uiState.collectAsState().value
            TrendingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Toolbar()
                        if (uiState.isLoading) {
                            ListItemWithShimmerEffect()
                        } else if (uiState.isError) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(
                                    R.raw.failure
                                )
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                LottieAnimation(
                                    composition = composition,
                                    iterations = LottieConstants.IterateForever,
                                )
                                ErrorColumn(
                                    title = "Something went wrong",
                                    description = "An alien is blocking your signal.",
                                    retryButton = "Retry",
                                    onRetryClick = { viewModel.fetch(true) }
                                )
                            }


                        } else {
                            val list = viewModel.trendingList.collectAsState(initial = emptyList()).value
                            TrendingList(list = list)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ListItemWithShimmerEffect() {
        Column {
            repeat(15) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)

                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                    }
                }

            }

        }
    }

    @Composable
    fun ShimmerEffect(modifier: Modifier) {
        val animatedProgress = remember { Animatable(0f) }

        LaunchedEffect(key1 = true) {
            while (true) {
                animatedProgress.animateTo(
                    1f, animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }

        Box(
            modifier = modifier
                .background(Color.Gray.copy(alpha = 0.2f))
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress.value)
                    .background(Color.Gray.copy(alpha = 0.6f))
            )
        }
    }


    @Composable
    fun TrendingList(list: List<Trending>) {
        LazyColumn {
            items(list) { item ->
                ListItemWithImageTitleDescription(item)
            }
        }
    }

    @Composable
    fun Toolbar() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Trending",

                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color.Black,
            )

            Image(
                painter = painterResource(id = R.drawable.ic_more_icon_foreground),
                contentDescription = null,
                modifier = Modifier
                    .clickable { viewModel.onToolbarIconClicked() }
                    .size(24.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }
    }


    @Composable
    fun ErrorColumn(
        title: String,
        description: String,
        retryButton: String,
        onRetryClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = onRetryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Green,
                    ),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Green,
                    containerColor = Color.White,
                ),
            ) {
                Text(text = retryButton)
            }
        }
    }
}
