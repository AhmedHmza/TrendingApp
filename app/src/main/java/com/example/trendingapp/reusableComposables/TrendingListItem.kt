package com.example.trendingapp.reusableComposables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.trendingapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trendingapp.model.Trending

@Composable
fun ListItemWithImageTitleDescription(
    trending: Trending
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                alpha = animatedProgress
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .align(Alignment.Top)
            ) {
                Text(
                    text = trending.projectName!!,
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = trending.userName!!,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if(!trending.language.isNullOrEmpty()){
                    Row(Modifier.padding( top = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        BlueCircle()
                        Text(
                            modifier = Modifier.fillMaxWidth(0.6f).padding(start = 5.dp),
                            text = trending.description!!,
                            style = TextStyle(
                                fontSize = 12.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        trending.starCount?.let {

                            Image(
                                painter = painterResource(id = R.drawable.ic_star_icon_foreground),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(start = 20.dp),
                                colorFilter = ColorFilter.tint(Color.Yellow)
                            )

                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                text = it,
                                style = TextStyle(
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f)
                .height(1.dp)
                .background(color = Color.Gray)

        )
    }
}