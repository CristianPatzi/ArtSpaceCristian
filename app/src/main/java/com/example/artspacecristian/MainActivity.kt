package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme
import perfetto.protos.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceTheme(

            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize().background(Color.White),
                ) { innerPadding ->
                    ArtSpacePreview()
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ArtSpaceLayout(modifier: Modifier = Modifier) {
    val cardItemLists = listOf(
        CardItem(image = R.drawable.image_1, title = "Kaneki", artist = "Tokyo Ghoul", year = 2021),
        CardItem(image = R.drawable.image_2, title = "Light Yagami", artist = "Death Note", year = 2022),
        CardItem(image = R.drawable.image_3, title = "Sukuna", artist = "Jujutsu Kaisen", year = 2023),
        CardItem(image = R.drawable.image_4, title = "Satoru Gojo", artist = "Jujutsu Kaisen", year = 2024)
    )

    var position by remember { mutableStateOf(0) };
    val pagerState  = rememberPagerState(
        pageCount = { cardItemLists.size },
        initialPage =  0
    )

    LaunchedEffect(position) {
        pagerState.animateScrollToPage(
            page = position,
            animationSpec = tween(300)
        )
    }


    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f),
        ) { page ->
            ArtSpaceCard(card = cardItemLists[page], position = page)
        }
        Actions(
            modifier = Modifier.padding(10.dp),
            onNext = {
                position = (position + 1) % cardItemLists.size;
            },
            onPrevious = {
                position = (position - 1 + cardItemLists.size) % cardItemLists.size;
            }
        )
    }
}

@Composable
fun ArtSpaceCard(card: CardItem, position: Int) {
    Column(
    ) {
        Image(
            painter = painterResource(card.image),
            contentDescription = card.title,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.6f)
                .padding(10.dp)
                .shadow(10.dp, RectangleShape)
                .border(20.dp, Color.White, RectangleShape),
            contentScale = ContentScale.Crop,
            )
        Description(cardItem = card)
    }
}

@Composable
fun Actions(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onPrevious: () -> Unit
){
    Row(
        modifier = modifier,
    ) {
        Button(
            onClick =onPrevious,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            )
        ) {
            Text(text = stringResource(R.string.previous))
        }
        Box(modifier = Modifier.padding(10.dp))
        Button(
            onClick = onNext ,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}

@Composable
fun Description(modifier: Modifier = Modifier, cardItem: CardItem) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = cardItem.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),

            )
        Row {
            Text(
                text = cardItem.artist,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            )
            Box(modifier = Modifier.padding(end = 5.dp))
            Text(
                text = "(${cardItem.year})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontStyle = FontStyle.Italic
            )

        }

    }

}
@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    ArtSpaceTheme {
        ArtSpaceLayout(modifier = Modifier.fillMaxSize())
    }
}