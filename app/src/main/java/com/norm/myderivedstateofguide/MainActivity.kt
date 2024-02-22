package com.norm.myderivedstateofguide

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norm.myderivedstateofguide.ui.theme.MyDerivedStateOfGuideTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDerivedStateOfGuideTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = rememberLazyListState()
                    var isEnabled by remember {
                        mutableStateOf(true)
                    }
                    val items = (1..100).toList()

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize(),
                        floatingActionButton = {
                            ScrollToTopButton(
                                state = state,
                                isEnabled = isEnabled
                            )
                        }
                    ) { pading ->
                        LazyColumn(
                            state = state,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(pading),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(items) { item ->
                                ItemBlock("Item is ${item}") {
                                    isEnabled = !isEnabled
                                    Log.d("My Log", "ItemBlock onClick")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemBlock(
    text: String,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick()
            }
            .background(MaterialTheme.colorScheme.primary)
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f),
        ) {
            Text(
                text = text,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ScrollToTopButton(
    state: LazyListState,
    isEnabled: Boolean,
) {
    val scope = rememberCoroutineScope()

    val showScrollToButton by remember(isEnabled) {
        derivedStateOf {
            state.firstVisibleItemIndex >= 5 && isEnabled
        }
    }

//    val showScrollToButton = remember(state.firstVisibleItemIndex) {
//        state.firstVisibleItemIndex >= 5
//    }

    if (showScrollToButton) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    state.animateScrollToItem(0)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null
            )
        }
    }
}