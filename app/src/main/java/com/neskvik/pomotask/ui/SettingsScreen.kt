package com.neskvik.pomotask.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.neskvik.pomotask.R
import com.neskvik.pomotask.setting.SettingsEvent
import com.neskvik.pomotask.setting.SettingsState
import kotlin.time.ExperimentalTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    onEvent: (SettingsEvent) -> Unit
){
    Scaffold(
        modifier = modifier.padding(16.dp)
    ) {

        var showWorkSessionSheet by remember { mutableStateOf(false) }
        var showShortBreakSheet by remember { mutableStateOf(false) }
        var showlongBreakSheet by remember { mutableStateOf(false) }


        if(showWorkSessionSheet){
            ModalBottomSheet(
                onDismissRequest = {showWorkSessionSheet = false
                    onEvent(SettingsEvent.SaveWorkSession)}
            ) {
                TextField(
                    value = settingsState.workSession,
                    onValueChange = {
                            onEvent(SettingsEvent.SetWorkSession(it))
                    },
                    placeholder = {
                        Text(
                            text = "Значение от 1 до 60"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1
                )
            }
        }

        if(showShortBreakSheet){
            ModalBottomSheet(
                onDismissRequest = {showShortBreakSheet = false
                    onEvent(SettingsEvent.SaveShortBreak)}
            ) {
                TextField(
                    value = settingsState.shortBreak,
                    onValueChange = {
                        onEvent(SettingsEvent.SetShortBreak(it))
                    },
                    placeholder = {
                        Text(
                            text = "Значение от 1 до 60"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1
                )
            }
        }

        if(showlongBreakSheet){
            ModalBottomSheet(
                onDismissRequest = {showlongBreakSheet = false
                    onEvent(SettingsEvent.SaveLongBreak)}
            ) {
                TextField(
                    value = settingsState.longBreak,
                    onValueChange = {
                        onEvent(SettingsEvent.SetLongBreak(it))
                    },
                    placeholder = {
                        Text(
                            text = "Значение от 1 до 60"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Work session: ${settingsState.workSession}",
                    modifier = Modifier.clickable{
                        showWorkSessionSheet = true
                    }
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = ""
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Short break: ${settingsState.shortBreak}",
                    modifier = Modifier.clickable{
                        showShortBreakSheet = true
                    }
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = ""
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Long break: ${settingsState.longBreak}",
                    modifier = Modifier.clickable{
                        showlongBreakSheet = true
                    }
                )
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = ""
                )
            }
        }
    }
}