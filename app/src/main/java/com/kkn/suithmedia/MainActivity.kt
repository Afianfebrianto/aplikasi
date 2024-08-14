package com.kkn.suithmedia

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kkn.suithmedia.ui.theme.SuithmediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuithmediaTheme {
                MainNavGraph()
            }
        }
    }
}

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "first_screen") {
        composable("first_screen") {
            BgGradient {
                FirstScreen(navController)
            }
        }
        composable(
            route = "second_screen/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            SecondScreen(navController = navController, name = name)
        }
        composable("third_screen") {
            ThirdScreen(navController) { selectedUserName ->
                // Update selected user name in SecondScreen
                val backStackEntry = navController.previousBackStackEntry
                backStackEntry?.savedStateHandle?.set("selectedUserName", selectedUserName)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var palindrome by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var isPalindrome by rememberSaveable { mutableStateOf(false) }
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
                    .padding(8.dp),
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
                            dialogMessage = if (isPalindrome) "isPalindrome" else "bukan palindrome"
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

@Composable
fun ProfileImage() {
    val imageUrl = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        if (imageUrl.value.isEmpty()) R.drawable.ic_user
        else imageUrl.value
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
        Text(text = "Change profile picture")
    }
}

fun checkPalindrome(text: String): Boolean {
    val cleanText = text.replace("\\s".toRegex(), "").lowercase()
    return cleanText == cleanText.reversed()
}

@Composable
fun BgGradient(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    0.0f to Color.Magenta,
                    500.0f to Color.Cyan,
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        content()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FirstScreenPreview() {
//    SuithmediaTheme {
//        FirstScreen()
//    }
//}
