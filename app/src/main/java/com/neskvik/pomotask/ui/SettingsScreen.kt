package com.neskvik.pomotask.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neskvik.pomotask.R
import com.neskvik.pomotask.auth.AuthState
import com.neskvik.pomotask.setting.SettingsEvent
import com.neskvik.pomotask.setting.SettingsState
import com.neskvik.pomotask.ui.theme.BackgroundColor
import com.neskvik.pomotask.ui.theme.BlockColor
import com.neskvik.pomotask.ui.theme.Blue
import com.neskvik.pomotask.ui.theme.Bounded
import com.neskvik.pomotask.ui.theme.Montserrat
import kotlin.time.ExperimentalTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsState: SettingsState,
    authState: AuthState,
    onEvent: (SettingsEvent) -> Unit,
    onLoginClick: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(modifier = modifier.padding(16.dp)
        .background(BackgroundColor)
    ) {

        var showWorkSessionSheet by remember { mutableStateOf(false) }
        var showShortBreakSheet by remember { mutableStateOf(false) }
        var showLongBreakSheet by remember { mutableStateOf(false) }
        var showLogoutDialog by remember { mutableStateOf(false) }

        if (showWorkSessionSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showWorkSessionSheet = false
                    onEvent(SettingsEvent.SaveWorkSession)
                },
                containerColor = Color(0xFF1A1F28)
            ) {
                SettingsTimeField(
                    label = "Время работы (мин)",
                    value = settingsState.workSession,
                    onValueChange = { onEvent(SettingsEvent.SetWorkSession(it)) }
                )
            }
        }

        if (showShortBreakSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showShortBreakSheet = false
                    onEvent(SettingsEvent.SaveShortBreak)
                },
                containerColor = Color(0xFF1A1F28)
            ) {
                SettingsTimeField(
                    label = "Короткий отдых (мин)",
                    value = settingsState.shortBreak,
                    onValueChange = { onEvent(SettingsEvent.SetShortBreak(it)) }
                )
            }
        }

        if (showLongBreakSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showLongBreakSheet = false
                    onEvent(SettingsEvent.SaveLongBreak)
                },
                containerColor = Color(0xFF1A1F28)
            ) {
                SettingsTimeField(
                    label = "Длинный отдых (мин)",
                    value = settingsState.longBreak,
                    onValueChange = { onEvent(SettingsEvent.SetLongBreak(it)) }
                )
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Выйти из аккаунта?") },
                text = { Text("Вы уверены, что хотите выйти?") },
                confirmButton = {
                    TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                        Text("Выйти", color = Color(0xFFFF5252))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Настройки",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Bounded
                )
                if (authState.isLoggedIn) {
                    Text(
                        text = authState.username,
                        color = Color(0xFF2196F3),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Montserrat,
                        modifier = Modifier.clickable { showLogoutDialog = true }
                    )
                } else {
                    Text(
                        text = "Войти",
                        fontSize = 14.sp,
                        color = Blue,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Column(
                Modifier.clip(RoundedCornerShape(15.dp))
                    .background(color = BlockColor)
                    .padding(14.dp)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Icon(painter = painterResource(
                        id = R.drawable.pomodoro_icon),
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Помодоро",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Montserrat
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showWorkSessionSheet = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Время работы: ${settingsState.workSession} мин",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Montserrat,
                        color = Color(0xFFBDBDBD)
                    )
                    Icon(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "")
                }

                Spacer(Modifier.height(0.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showShortBreakSheet = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Короткий отдых: ${settingsState.shortBreak} мин",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Montserrat,
                        color = Color(0xFFBDBDBD)
                    )
                    Icon(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "")
                }

                Spacer(Modifier.height(0.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLongBreakSheet = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Длинный отдых: ${settingsState.longBreak} мин",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Montserrat,
                        color = Color(0xFFBDBDBD)
                    )
                    Icon(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "")
                }
            }

        }
    }
}

@Composable
private fun SettingsTimeField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 36.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontFamily = Montserrat
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Montserrat
            ),
            cursorBrush = SolidColor(Color(0xFF0088FF)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF1F252E))
                        .border(
                            width = 1.dp,
                            color = if (isFocused) Color(0xFF0088FF) else Color(0x0DFFFFFF),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "от 1 до 60",
                            color = Color.White.copy(alpha = 0.38f),
                            fontSize = 14.sp,
                            fontFamily = Montserrat
                        )
                    }
                    inner()
                }
            }
        )
    }
}
