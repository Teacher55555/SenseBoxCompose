package com.example.sensebox.ui.compose.boxdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sensebox.R
import com.example.sensebox.data.fake.FakeBoxDataProvider
import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.BoxDetail
import com.example.sensebox.data.model.SensorDetail
import com.example.sensebox.ui.compose.boxlist.BoxUiState
import com.example.sensebox.ui.compose.boxlist.ErrorScreen
import com.example.sensebox.ui.compose.boxlist.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BoxDetailScreen(
    viewModel: BoxDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Boolean
) {
    when (val uiState = viewModel.uiState) {
        is BoxDetailUiState.Loading -> LoadingScreen()
        is BoxDetailUiState.Failure -> ErrorScreen()
        is BoxDetailUiState.Success -> BoxDetail(
            box = uiState.detailBox,
            onNavigateBack = onNavigateBack,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxDetail (
    modifier: Modifier = Modifier,
    viewModel: BoxDetailViewModel,
    box : BoxDetail,
    onNavigateBack: () -> Boolean,
) {
    val isFavorite by viewModel.isFavorite.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var descriptionExpanded by remember { mutableStateOf(false) }
    var favoriteMessageShown by remember { mutableStateOf(false) }

    fun showFavoriteStatusMessage() {
        scope.launch {
            if (!favoriteMessageShown) {
                favoriteMessageShown = true
                delay(2000L)
                favoriteMessageShown = false
            }
        }
    }

    Scaffold(
        topBar =  {
            TopAppBar(
                title = { Text(text = box.name, maxLines = 1) },
                modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = Icons.Filled.ArrowBack.name
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorite),
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clip(ShapeDefaults.Medium)
                            .clickable {
                                showFavoriteStatusMessage()
                                viewModel.favClickHandle(box)
                            }
                    )
                }
            )
        },
    )
    { padding ->
        Column(
            modifier = modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Box {
                Card(
                    modifier = Modifier.padding(8.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(box.image)
                            .crossfade(true)
                            .error(R.drawable.dummysensor)
                            .build(),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable { descriptionExpanded = !descriptionExpanded }
                    )
                    AnimatedVisibility (visible = box.description.isNotEmpty() && descriptionExpanded) {
                        Text(
                            text = box.description,
                            textAlign = TextAlign.Justify,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp),
                            lineHeight = 16.sp
                        )
                    }
                }
                Card(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(22.dp)
                ) {
                    Text(
                        text = box.exposure,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 6.dp)
                    )
                }
               ShowMessage(shown = favoriteMessageShown, isFavorite = isFavorite)
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .verticalScroll(scrollState)
                ) {
                    box.sensors.forEachIndexed { index, sensor ->
                            SensorItem(sensor = sensor)
                            if (index < box.sensors.size - 1) {
                                Divider(color = MaterialTheme.colorScheme.surface)
                            }
                        }
                    }
                }
            }
        }
    }


@Composable
fun SensorItem(
    modifier: Modifier = Modifier,
    sensor: SensorDetail
) {
    var isIncreased by remember { mutableStateOf(false) }
    val animatedSizeDp by animateDpAsState(
        targetValue = if (isIncreased) 100.dp else 0.dp,
        label = "Animate content size"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .heightIn(min = animatedSizeDp)
            .clickable { isIncreased = !isIncreased }
    ) {
        Row() {
            Image(
                // There should be the correct icon for each sensor type. Temporarily here is the temperature icon for all sensors
                painter = painterResource(id = R.drawable.temp),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Text(
                text = sensor.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
        if (sensor.lastMeasurement.value.isNotEmpty()) {
            Column(
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = String.format("%.7s", sensor.lastMeasurement.value),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = sensor.unit,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    /**
                     * Here must be converter from time stamp to real time. Temporarily it "vor 8 Minuten" hardcoded
                     */
                    text = stringResource(R.string.vor_8_minuten),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text(
                text = stringResource(R.string.no_measurement),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ShowMessage(shown: Boolean, isFavorite: Boolean) {
    AnimatedVisibility(
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)

        ) + fadeOut(),
        visible = shown
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp
        ) {
            Text(
                text = if (isFavorite) stringResource(R.string.fav_add_message)
                else stringResource(R.string.fav_remove_message),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SensorItemPreview() {
    SensorItem(sensor = FakeBoxDataProvider.fakeDetailBox.sensors[0])
}

@Preview(showBackground = true)
@Composable
fun BoxDetailPreview() {
    BoxDetail(
        box = FakeBoxDataProvider.fakeDetailBox,
        onNavigateBack = { true },
        viewModel = hiltViewModel()
    )
}