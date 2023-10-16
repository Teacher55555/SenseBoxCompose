package com.example.sensebox.ui.compose.boxfavList

import android.graphics.drawable.shapes.OvalShape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sensebox.R
import com.example.sensebox.data.fake.FakeBoxDataProvider
import com.example.sensebox.data.model.Box
import com.example.sensebox.ui.compose.boxlist.BoxNameAndSensorColumn
import com.example.sensebox.ui.compose.home.TopBarWithMenu
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavBoxScreen (
    onMenuClick: () -> Unit,
    viewModel: BoxFavViewModel = hiltViewModel(),
    onBoxClick: (String) -> Unit,
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    val uiState by viewModel.favBoxUiState.collectAsState()
    var openDialogDeleteBox by remember { mutableStateOf(false ) }
    var openDialogDeleteAllBoxes by remember { mutableStateOf(false ) }
    var boxToDelete by remember { mutableStateOf("") }
    Scaffold(
        topBar = { TopBarWithMenu(onMenuClick = onMenuClick, title = stringResource(id = R.string.favorite_title, uiState.favBoxes.size))
        },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = { openDialogDeleteAllBoxes = true },
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_all),
                    )
                }
        }
    ) { padding ->
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
            ),
            exit = fadeOut()
        ) {
            Column() {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(items = uiState.favBoxes, key = { box -> box.id }) { box ->
                        FavBoxListItem(
                            box = box,
                            onBoxClick = onBoxClick,
                            onDeleteClick = {
                                openDialogDeleteBox = true
                                boxToDelete = it
                            },
                            onSwipeToDelete = viewModel::deleteFavBox,
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                                .animateItemPlacement(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        )
                    }
                }
            }

            if (openDialogDeleteBox) {
                DeleteConfirmationDialog(
                    title = stringResource(R.string.delete_box_warning),
                    text = stringResource(R.string.are_you_sure_warning),
                    onConfirm = {
                        viewModel.deleteFavBox(boxToDelete)
                        openDialogDeleteBox = false
                    },
                    onDismiss = { openDialogDeleteBox = false }
                )
            }
            if (openDialogDeleteAllBoxes) {
                DeleteConfirmationDialog(
                    title = stringResource(R.string.clear_all_items_warning),
                    text = stringResource(R.string.text_delete_all_database_warning),
                    onConfirm = {
                        viewModel.deleteAll()
                        openDialogDeleteAllBoxes = false
                    },
                    onDismiss = { openDialogDeleteAllBoxes = false }
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog (
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.width(250.dp),
        onDismissRequest = onDismiss,
        title = { Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        ) },
        text = {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
       },
        icon = { Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        ) },
        dismissButton = {
            Card  {
                Text(
                    text = stringResource(R.string.cancel),
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.titleMedium,
                )

            } },
        confirmButton = {
            Card {
                Text(
                    text = stringResource(R.string.ok),
                    modifier = Modifier
                        .clickable { onConfirm() }
                        .padding(start = 37.dp, end = 37.dp, top = 10.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            } }
    )
}

@Composable
fun FavBoxListItem (
    modifier: Modifier = Modifier,
    box: Box,
    onBoxClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit,
) {
//    Card(
//        modifier = modifier
//            .clickable {
//                onBoxClick(box.id)
//            },
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {

    Surface ( modifier = modifier
        .clickable { onBoxClick(box.id) }
        .swipeToDismiss { onSwipeToDelete(box.id) },
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(18)

    )  {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
//                .swipeToDismiss { }
        ) {
            Column (modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BoxNameAndSensorColumn(box = box)
            }
            IconButton(onClick = { onDeleteClick(box.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }
}

private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    // This `Animatable` stores the horizontal offset for the element.
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        // Record the position after offset
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            // Overwrite the `Animatable` value while the element is dragged.
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    println(targetOffsetX)
                    if (targetOffsetX.absoluteValue <= size.width) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else if (targetOffsetX >= size.width) {
                        print(" ===== swipe RIGHT to delete ====")
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    } else {
                        print(" === swipe LEFT to delete ====")
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    }
                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}


@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialogPreview() {
    DeleteConfirmationDialog(
        title = stringResource(R.string.clear_all_items_warning),
        text = stringResource(R.string.text_delete_all_database_warning),
        onConfirm = {},
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun FavBoxListItemPreview() {
    FavBoxListItem(box = FakeBoxDataProvider.fakeBoxListItem, onBoxClick = {}, onDeleteClick = { }, onSwipeToDelete = {})
}

