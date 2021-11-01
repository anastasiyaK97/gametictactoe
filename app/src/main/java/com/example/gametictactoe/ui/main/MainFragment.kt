package com.example.gametictactoe.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.example.gametictactoe.databinding.MainFragmentBinding
import com.example.gametictactoe.entities.CellState
import com.example.gametictactoe.entities.GameStatus
import com.example.gametictactoe.entities.WinLineState

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reloadBtn.setOnClickListener {
            viewModel.onReloadGame()
            binding.grid.drawWinLine(WinLineState.None)
        }

        binding.grid.forEachIndexed { index, view ->
            view.setOnClickListener { viewModel.onCellClick(index) }
        }
        viewModel.currentMove.observe(viewLifecycleOwner) {
            binding.arrow.animate()
                .rotation(if (it == CellState.Cross) 0f else 180f)
                .scaleY(if (it == CellState.Cross) 1f else -1f)
        }
        viewModel.cellStateByIndex.observe(viewLifecycleOwner) {
            val (index, state) = it
            with(binding.grid.getChildAt(index) as ImageView) {
                isEnabled = state.isClickable
                setImageResource(state.icon)
            }
        }
        viewModel.gameState.observe(viewLifecycleOwner) {
            val (status, matrix) = it
            matrix.forEachIndexed { index, cellState ->
                with(binding.grid.getChildAt(index) as ImageView) {
                    setImageResource(cellState.icon)
                    isEnabled = cellState.isClickable && status == GameStatus.Started
                }
            }
            if (status == GameStatus.Started) binding.reloadBtn.visibility = View.GONE
            else binding.reloadBtn.visibility = View.VISIBLE
        }
        viewModel.winState.observe(viewLifecycleOwner) {
            binding.grid.drawWinLine(it)}
    }

}