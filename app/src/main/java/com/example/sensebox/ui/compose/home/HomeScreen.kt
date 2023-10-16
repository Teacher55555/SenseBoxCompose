package com.example.sensebox.ui.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sensebox.R
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    modifier: Modifier = Modifier,
    onStartClick: (Int) -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarWithMenu(
                onMenuClick = onMenuClick,
                title = stringResource(id = R.string.app_name)
            )
        },
    ) { paddings ->
        var distance by rememberSaveable { mutableStateOf(10f) }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.senseboxlogo),
                        contentDescription = stringResource(R.string.logo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.distance, distance.roundToInt()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 30.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier.padding(30.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Slider(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 2.dp,
                        bottom = 2.dp
                    ),
                    value = distance,
                    valueRange = 10f..1000f,
                    colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.background),
                    onValueChange = { distance = it })
            }
            Button(
                modifier = Modifier
                    .size(80.dp)
                    .padding(top = 20.dp),
                onClick = { onStartClick(distance.roundToInt()) },
                shape = CircleShape
            ) {
                Text(
                    text = stringResource(R.string.go),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithMenu(
    onMenuClick: () -> Unit,
    modifier : Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        title = { Text(text = title, maxLines = 1) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
    )
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview () {
    HomeScreen(
        onMenuClick = {},
        onStartClick = {}
    )
}