package com.kkn.suithmedia

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kkn.suithmedia.model.User

@Composable
fun FirstScreen(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var palindrome by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Menempatkan seluruh konten di tengah
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileImage()
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Name", color = Color.Gray.copy(alpha = 0.6f)) },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()


                )

            }

            Row(
                modifier = Modifier
                    .padding(bottom = 45.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = palindrome,
                    onValueChange = { palindrome = it },
                    placeholder = {
                        Text(
                            text = "Palindrome",
                            color = Color.Gray.copy(alpha = 0.8f),
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                )

            }

            Row(
                modifier = Modifier
                    .padding(bottom = 25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (palindrome.isEmpty()) {
                            dialogMessage = "Palindrome field cannot be empty."
                            showDialog = true
                        } else {
                            val isPalindrome = checkPalindrome(palindrome)
                            dialogMessage = if (isPalindrome) "isPalindrome" else "not palindrome"
                            showDialog = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Check")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (name.isEmpty()) {
                            dialogMessage = "Name field cannot be empty."
                            showDialog = true
                        } else {
                            navController.navigate("second_screen/$name")
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Next")
                }
            }

            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }, title = {
                    Text("Alert")
                }, text = {
                    Text(dialogMessage)
                })
            }

        }
    }


}


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
                    text = name, style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = selectedUserName, style = MaterialTheme.typography.headlineLarge.copy(
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
fun ThirdScreen(navController: NavController) {
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileImage() {
    val imageUrl = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        imageUrl.value.ifEmpty { R.drawable.ic_user }
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUrl.value = it.toString() }
    }


    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(116.dp) // Ukuran lingkaran
                .background(
                    color = Color.White.copy(alpha = 0.3f), // Semi transparan
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(imageUrl.value),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize() // Mengisi seluruh lingkaran
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // Overlay icon
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp) // Ukuran ikon lebih kecil
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun checkPalindrome(text: String): Boolean {
    val cleanText = text.replace("\\s".toRegex(), "").lowercase()
    return cleanText == cleanText.reversed()
}


@OptIn(ExperimentalCoilApi::class)
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

