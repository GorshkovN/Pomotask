package com.neskvik.pomotask.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.neskvik.pomotask.pomodoro.PomodoroViewModel
import com.neskvik.pomotask.pomodoro.TimerMode
import com.neskvik.pomotask.ui.theme.BackgroundColor
import com.neskvik.pomotask.ui.theme.BlockColor
import com.neskvik.pomotask.ui.theme.Bounded

private val BgColor = Color(0xFF1C1C2E)
private val RingInactive = Color(0xFF2D2D40)
private val RingActive = Color(0xFF2196F3)
private val ButtonBg = Color(0xFF2D2D40)

@Composable
fun PomodoroTimerScreen(
    viewModel: PomodoroViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    val progress = if (state.sessionTotalTime > 0L) {
        (state.currentTime.toFloat() / state.sessionTotalTime.toFloat()).coerceIn(0f, 1f)
    } else 1f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(300),
        label = "timerProgress"
    )

    val minutes = state.currentTime / 1000L / 60
    val seconds = state.currentTime / 1000L % 60

    val modeLabel = when (state.timerMode) {
        TimerMode.WORK -> "Время работы"
        TimerMode.SHORT_BREAK -> "Коротний отдых"
        TimerMode.LONG_BREAK -> "Длинный отдых"
    }

    val filledDots = when {
        state.timerMode == TimerMode.LONG_BREAK -> 4
        else -> state.completedSessions % 4
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (index < filledDots) RingActive
                            else Color.White.copy(alpha = 0.2f)
                        )
                )
            }
        }

        Spacer(Modifier.height(48.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(300.dp)
        ) {
            Canvas(modifier = Modifier.size(300.dp).background(color = BlockColor, shape = CircleShape)
                .padding(6.dp)) {
                val strokePx = 12.dp.toPx()

                drawArc(
                    color = RingActive,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(strokePx, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%02d".format(minutes),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Bounded,
                    color = Color.White,
                    lineHeight = 56.sp
                )
                Text(
                    text = "%02d".format(seconds),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Bounded,
                    color = Color.White,
                    lineHeight = 56.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = modeLabel,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 16.sp
        )

        Spacer(Modifier.height(48.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = viewModel::resetSession,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(ButtonBg)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart session",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }

            IconButton(
                onClick = viewModel::togglePlayPause,
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(RingActive)
            ) {
                Icon(
                    imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isRunning) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(43.dp)
                )
            }

            IconButton(
                onClick = viewModel::stop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(ButtonBg)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}
