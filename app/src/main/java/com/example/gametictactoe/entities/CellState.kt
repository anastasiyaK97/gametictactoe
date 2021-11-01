package com.example.gametictactoe.entities

import androidx.annotation.DrawableRes
import com.example.gametictactoe.R

enum class CellState (@DrawableRes val icon: Int, val isClickable: Boolean) {
    None(0, true),
    Cross(R.drawable.ic_cross, false),
    Circle(R.drawable.ic_circle, false)
}