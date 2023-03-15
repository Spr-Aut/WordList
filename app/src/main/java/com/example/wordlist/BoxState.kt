package com.example.wordlist

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

public sealed class BoxState(val height: Dp, val width: Dp) {
    // 默认状态
    object NormalState : BoxState(30.dp, 100.dp)

    // 充电状态
    object ChargeState : BoxState(30.dp, 170.dp)

    // 支付状态
    object PayState : BoxState(100.dp, 100.dp)

    // 音乐状态
    object MusicState : BoxState(170.dp, 340.dp)

    // 多个状态
    object MoreState : BoxState(30.dp, 100.dp)
}