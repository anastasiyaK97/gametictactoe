package com.example.gametictactoe.entities

sealed class WinLineState {
    data class Horizontal(val row: Int): WinLineState()
    data class Vertical(val column: Int): WinLineState()
    object MainDiagonal : WinLineState()
    object ReverseDiagonal: WinLineState()
    object None: WinLineState()
}
