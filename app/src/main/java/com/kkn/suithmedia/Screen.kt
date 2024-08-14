package com.kkn.suithmedia

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.kkn.suithmedia.model.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(navController: NavController, name: String) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var selectedUserName by rememberSaveable { mutableStateOf("Selected User Name") }

    // Observe changes in selectedUserName from SavedStateHandle
    savedStateHandle?.getLiveData<String>("selectedUserName")
        ?.observeAsState()?.value?.let { userName ->
            selectedUserName = userName
        }
    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Second Screen", style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color(0xFF554AF0)
                )
            }
        })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "$name", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = "$selectedUserName", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Button(
                onClick = { navController.navigate("third_screen") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Choose a User")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(navController: NavController, onUserSelected: (String) -> Unit) {
    val viewModel: UsersViewModel = viewModel()
    val users by viewModel.users.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isEmpty by viewModel.isEmpty.observeAsState(true)

    val listState = rememberLazyListState()

    Scaffold(topBar = {
        TopAppBar(title = {  Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Third Screen", style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        } }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color(0xFF554AF0)
                )
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isEmpty && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Users Found")
                }
            } else {
                LazyColumn(state = listState) {
                    items(users) { user ->
                        UserItem(user = user, onClick = {
                            val selectedUserName = "${user.first_name} ${user.last_name}"
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "selectedUserName", selectedUserName
                            )
                            navController.popBackStack()
                        })
                    }
                    if (isLoading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LazyListState.isScrolledToTheEnd(): Boolean {
    val layoutInfo = layoutInfo
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: 0
    val totalItems = layoutInfo.totalItemsCount
    return lastVisibleItemIndex >= totalItems - 1
}


@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }) {
        Image(
            painter = rememberImagePainter(data = user.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = "${user.first_name} ${user.last_name}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(text = user.email, style = MaterialTheme.typography.titleSmall)
        }
    }
}

