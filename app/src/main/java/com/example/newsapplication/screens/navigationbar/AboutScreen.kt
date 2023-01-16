package com.example.newsapplication.screens.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Markup of the "About the app" screen
@Composable
@Preview
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon Composable
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "profile",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        // Text to Display the current Screen
        Text(text = "About")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            VKCard() // A card with a link to the VK
            TelegramCard() // A card with a link to the Telegram
            //EmailCard() // A card with a link to the Mail
        }
    }
}

@Composable
private fun VKCard() {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .clickable { uriHandler.openUri(uri = "https://vk.com/prikhodko_nk") }
            .padding(vertical = 5.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = com.example.newsapplication.R.drawable.vk),
                contentDescription = "VK",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun TelegramCard() {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .clickable { uriHandler.openUri(uri = "https://t.me/doc_fr10") }
            .padding(vertical = 5.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = com.example.newsapplication.R.drawable.telegram),
                contentDescription = "Telegram",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
    }
}

/*
@Composable
private fun EmailCard() {
    //val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            //.clickable { uriHandler.openUri(uri = "docfr10@yandex.ru") }
            .padding(all = 5.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box {
            Image(
                painter = painterResource(id = com.example.newsapplication.R.drawable.email),
                contentDescription = "Email",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}
 */