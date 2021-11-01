package com.example.gametictactoe.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gametictactoe.entities.CellState
import com.example.gametictactoe.entities.GameStatus
import com.example.gametictactoe.entities.WinLineState

class MainViewModel : ViewModel() {

    private lateinit var cellStateMatrix: Array<CellState>
    private lateinit var currentCellState: CellState

    private val mCurrentMove = MutableLiveData<CellState>()
    val currentMove: LiveData<CellState> = mCurrentMove

    private val mCellStateByIndex : MutableLiveData<Pair<Int, CellState>> = SingleLiveEvent()
    val cellStateByIndex: LiveData<Pair<Int, CellState>> = mCellStateByIndex

    private val mGameState = MutableLiveData<Pair<GameStatus, Array<CellState>>>()
    val gameState: LiveData<Pair<GameStatus, Array<CellState>>> = mGameState

    private val mWinState = MutableLiveData<WinLineState>()
    val winState: LiveData<WinLineState> = mWinState

    init {
        initGame()
        mWinState.value = WinLineState.None
    }

    private fun initGame() {
        cellStateMatrix = Array<CellState>(9) { CellState.None }
        mGameState.value = Pair(GameStatus.Started, cellStateMatrix)
        currentCellState = CellState.Cross
        mCurrentMove.value = currentCellState
    }

    fun onReloadGame() = initGame()

    fun onCellClick(index: Int) {
        cellStateMatrix[index] = currentCellState
        mCellStateByIndex.value = Pair(index,currentCellState)

        val row = index / 3
        val column = index % 3
        val state = checkWin(row, column)
        if (state != WinLineState.None) {
            mGameState.value = Pair(GameStatus.Finished, cellStateMatrix)
            mWinState.value = state
            return
        }

        if (checkFinished()) mGameState.value = Pair(GameStatus.Finished, cellStateMatrix)

        currentCellState =
            if (currentCellState == CellState.Cross) CellState.Circle else CellState.Cross
        mCurrentMove.value = currentCellState

    }

    private fun checkFinished(): Boolean {
        return cellStateMatrix.all { state -> state != CellState.None}
    }

    private fun checkWin(row: Int, column: Int): WinLineState {
        //check row
        if (checkLine { cellStateMatrix[getIndex(row, it)] == currentCellState }) return WinLineState.Horizontal(row)
        // check column
        if (checkLine { cellStateMatrix[getIndex(it, column)] == currentCellState }) return WinLineState.Vertical(column)
        if (row == column) {
            // check main diagonal
            if (checkLine { cellStateMatrix[getIndex(it, it)] == currentCellState }) return WinLineState.MainDiagonal
        }
        if (row + column == 2) {
            // check reverse diagonal
            if (checkLine { cellStateMatrix[getIndex(it, 2 - it)] == currentCellState }) return WinLineState.ReverseDiagonal
        }
        return WinLineState.None
    }

    private fun getIndex(row: Int, column: Int): Int = row * 3 + column

    private fun checkLine(function: (Int) -> Boolean): Boolean {
        for (i in 0..2) {
            if (!function(i)) return false
        }
        return true
    }

}