package com.kkn.suithmedia

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SecondScreen(navController: NavController, name: String, selectedUserName: String) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Second Screen") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Welcome", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))
//            Text(text = "Name: $name", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))
//            Text(text = "Selected User: $selectedUserName", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))
//            Button(onClick = {
//                navController.navigate("third_screen/$name")
//            }) {
//                Text(text = "Choose a User")
//            }
//        }
//    }
//}

@Composable
fun SecondScreen(navController: NavController, name: String) {
    var selectedUserName by rememberSaveable { mutableStateOf("No User Selected") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))

        Text(text = "Name: $name", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))

        Text(text = "Selected User: $selectedUserName", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(8.dp))

        Button(onClick = {
            navController.navigate("third_screen")
        }) {
            Text(text = "Choose a User")
        }
    }
}

@Composable
fun ThirdScreen() {
    // Your third screen content goes here
}



//@Preview(showBackground = true)
//@Composable
//fun SecondScreenPreview() {
//    SuithmediaTheme {
//
//        SecondScreen(navController = NavController , name = "Afian")
//    }
//}