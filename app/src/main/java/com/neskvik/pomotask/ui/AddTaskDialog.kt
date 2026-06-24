package com.neskvik.pomotask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.neskvik.pomotask.task.TaskEvent
import com.neskvik.pomotask.task.TaskState
import com.neskvik.pomotask.ui.theme.Bounded
import com.neskvik.pomotask.ui.theme.Montserrat

private val SheetBg   = Color(0xFF1A1F28)
private val FieldBg   = Color(0xFF1F252E)
private val AddBlue   = Color(0xFF0088FF)
private val TextFaded = Color.White.copy(alpha = 0.38f)
private val FieldBorderDefault = Color(0x0DFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskdialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(TaskEvent.HideDialog) },
        containerColor = SheetBg,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Новая задача",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontFamily = Bounded
            )

            AddTaskField(
                value = state.name,
                onValueChange = { onEvent(TaskEvent.SetName(it)) },
                placeholder = "Название"
            )

            AddTaskField(
                value = state.description,
                onValueChange = { onEvent(TaskEvent.SetDescription(it)) },
                placeholder = "Описание (необязательно)"
            )

            AddTaskField(
                value = state.deadline,
                onValueChange = { onEvent(TaskEvent.SetDeadline(it)) },
                placeholder = "Дедлайн  дд.мм.гггг",
                keyboardType = KeyboardType.Number
            )

            if (state.categories.isNotEmpty()) {
                Text(
                    text = "Категория",
                    fontSize = 11.sp,
                    color = TextFaded,
                    fontFamily = Montserrat
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    state.categories.forEach { category ->
                        val selected = state.category?.cid == category.cid
                        val bgColor = Color(category.color.toColorInt())
                        val textColor = if (bgColor.luminance() > 0.5f) Color.Black else Color.White
                        Text(
                            text = category.name,
                            fontSize = 12.sp,
                            color = if (selected) textColor else Color.White.copy(alpha = 0.7f),
                            fontFamily = Montserrat,
                            modifier = Modifier
                                .clip(RoundedCornerShape(200.dp))
                                .background(
                                    if (selected) bgColor.copy(alpha = 0.85f) else Color(0xFF2D3340)
                                )
                                .clickable { onEvent(TaskEvent.SetCategory(category)) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(2.dp))

            Button(
                onClick = { onEvent(TaskEvent.SaveTask) },
                colors = ButtonDefaults.buttonColors(containerColor = AddBlue),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Сохранить",
                    color = Color.White,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun AddTaskField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(color = Color.White, fontSize = 14.sp, fontFamily = Montserrat),
        cursorBrush = SolidColor(AddBlue),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        interactionSource = interactionSource,
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(FieldBg)
                    .border(
                        width = 1.dp,
                        color = if (isFocused) AddBlue else FieldBorderDefault,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, color = TextFaded, fontSize = 14.sp, fontFamily = Montserrat)
                }
                inner()
            }
        }
    )
}
