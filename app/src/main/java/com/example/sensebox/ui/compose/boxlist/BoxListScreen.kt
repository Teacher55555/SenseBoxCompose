package com.example.sensebox.ui.compose.boxlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensebox.R
import com.example.sensebox.data.fake.FakeBoxDataProvider
import com.example.sensebox.data.model.Box

@Composable
fun BoxListScreen(
    viewModel: BoxListViewModel = hiltViewModel(),
    onBoxClick: (String) -> Unit,
    onNavigateBack: () -> Boolean
) {
    when (val uiState = viewModel.uiState) {
        is BoxUiState.Loading -> LoadingScreen()
        is BoxUiState.Failure -> ErrorScreen()
        is BoxUiState.Success -> BoxList(
            boxes = uiState.boxes,
            onBoxClick = onBoxClick,
            onNavigateBack = onNavigateBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun BoxList (
    modifier: Modifier = Modifier,
    boxes : List<Box>,
    onBoxClick: (String) -> Unit,
    onNavigateBack: () -> Boolean
) {
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    Scaffold(
        topBar =  {
            TopAppBar(
                title = { Text("Boxes: ${boxes.size}") },
                modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { padding ->
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = modifier.padding(padding)
        ) {
            LazyColumn {
                items(items = boxes, key = { box -> box.id }) { box ->
                    /**
                     * Each element of the list is passed to a Compose function to draw it.
                     */
                    BoxListItem(
                        onBoxClick = onBoxClick,
                        box = box,
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                            .animateEnterExit(
                                enter = slideInVertically(
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                        dampingRatio = Spring.DampingRatioLowBouncy
                                    ),
                                    initialOffsetY = { it },
                                )
                            )
                            .animateContentSize(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun BoxListItem (
    modifier: Modifier = Modifier,
    box: Box,
    onBoxClick: (String) -> Unit
) {
    var detailEnabled by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState (
        if (detailEnabled) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = TweenSpec(durationMillis = 500),
        label = "background color animation"
    )
    Card(
        modifier = modifier
            .clickable {
                onBoxClick(box.id)
           },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)) {
            Column (modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BoxNameAndSensorColumn(box = box, detailEnabled)
                if (detailEnabled) {
                    Text(text = box.description, style = MaterialTheme.typography.bodySmall)
                }
            }
            if (box.description.isNotEmpty()) {
                IconButton(onClick = { detailEnabled = !detailEnabled }) {
                    Icon(
                        imageVector = if (detailEnabled) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun BoxNameAndSensorColumn (
    box: Box,
    detailEnabled: Boolean = false
) {
    val boxIconRotateState by animateFloatAsState(
        targetValue = if (detailEnabled) 0f else 180f,
        animationSpec = tween(300),
        label = "box rotate state animation")
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.box_icon),
            contentDescription = box.name,
            modifier = Modifier
                .rotate(boxIconRotateState)
                .size(22.dp)
        )
        Text(
            text = box.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            maxLines = 1
        )
    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.sensor_icon),
            contentDescription = "${box.sensors.count()}",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Text(
            text = stringResource(id = R.string.sensors_count, "${box.sensors.size}"),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.surfaceTint
        )
    }
}

@Preview (showBackground = true)
@Composable
fun BoxListItemPreview(
) {
    BoxListItem(box = FakeBoxDataProvider.fakeBoxListItem , onBoxClick = {})
}


@Composable
fun LoadingScreen () {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
            Text(text = stringResource(R.string.loading), style = MaterialTheme.typography.titleLarge)
            Text(text = stringResource(R.string.please_wait), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun ErrorScreen () {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.loading_error_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    LoadingScreen()
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    ErrorScreen()
}


