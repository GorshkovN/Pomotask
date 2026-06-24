package com.neskvik.pomotask.ui

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neskvik.pomotask.auth.AuthViewModel

private val Bg          = Color(0xFF161A20)
private val FieldBg     = Color(0xFF1F252E)
private val Blue        = Color(0xFF0088FF)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecond  = Color(0xFFBDBDBD)
private val TextMuted   = Color(0xFF78808A)
private val TextGray    = Color(0xFF6B7280)
private val FieldBorder = Color(0x0DFFFFFF)

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onDismiss()
    }

    var isLogin by rememberSaveable { mutableStateOf(true) }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isLogin) {
        name = ""; email = ""; password = ""; passwordVisible = false
        viewModel.clearError()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = TextSecond)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp)
                .padding(top = 56.dp, bottom = 30.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "POMOTASK",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 5.sp
                )
                Spacer(Modifier.size(6.dp))
                Box(
                    modifier = Modifier
                        .offset(y = 4.dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Blue)
                )
            }

            Spacer(Modifier.height(46.dp))

            Text(
                text = if (isLogin) "ВХОД" else "РЕГИСТРАЦИЯ",
                color = TextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = if (isLogin) "С возвращением" else "Создать аккаунт",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 29.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (isLogin) "Войдите, чтобы продолжить"
                       else "Зарегистрируйтесь, чтобы начать",
                color = TextSecond,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(30.dp))

            if (!isLogin) {
                DesignField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Имя пользователя",
                    placeholder = "Ваше имя"
                )
                Spacer(Modifier.height(16.dp))
            }

            DesignField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "example@mail.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(16.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FieldLabel("Пароль")

                }

                Spacer(Modifier.height(8.dp))

                PasswordDesignField(
                    value = password,
                    onValueChange = { password = it },
                    visible = passwordVisible,
                    onToggleVisible = { passwordVisible = !passwordVisible }
                )

                if (!isLogin) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Не менее 8 символов",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (state.error != null) {
                Spacer(Modifier.height(14.dp))
                Text(
                    text = state.error ?: "",
                    color = Color(0xFFFF5252),
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isLogin) viewModel.login(email, password)
                    else viewModel.register(name, email, password)
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (isLogin) "Войти" else "Зарегистрироваться",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            if (!isLogin) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Регистрируясь, вы соглашаетесь с условиями использования сервиса",
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLogin) "Нет аккаунта?  " else "Уже есть аккаунт?  ",
                    color = TextSecond,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (isLogin) "Зарегистрироваться" else "Войти",
                    color = Blue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { isLogin = !isLogin }
                )
            }
        }
    }
}


@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}


@Composable
private fun DesignField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    Column {
        FieldLabel(label)
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium),
            cursorBrush = SolidColor(Blue),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(FieldBg)
                        .border(
                            width = 1.dp,
                            color = if (focused) Blue else FieldBorder,
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    inner()
                }
            }
        )
    }
}


@Composable
private fun PasswordDesignField(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisible: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = TextStyle(color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium),
        cursorBrush = SolidColor(Blue),
        decorationBox = { inner ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(FieldBg)
                    .border(
                        width = 1.dp,
                        color = if (focused) Blue else FieldBorder,
                        shape = RoundedCornerShape(22.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 18.dp, end = 50.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text("••••••••", color = TextMuted, fontSize = 13.sp)
                    }
                    inner()
                }
                IconButton(
                    onClick = onToggleVisible,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = TextSecond,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    )
}
