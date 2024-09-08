# Minesweeper

## Description
This project is an implementation of the classic Minesweeper game written in Java. The game uses a grid-based board where the player must uncover cells without triggering mines. It features a recursive algorithm to efficiently reveal cells with no adjacent mines, mimicking the behavior of classic Minesweeper games.

## Features
- **Customizable Grid:** Play on different grid sizes.
- **Random Mine Placement:** Mines are randomly placed on the board for each game.
- **Recursive Cell Reveal Algorithm:** Efficiently reveals connected cells with zero adjacent mines, automatically uncovering large areas of the board.
- **Win/Lose Conditions:** The game ends when all non-mine cells are revealed or when a mine is triggered.

## Recursive Algorithm Overview
The project uses a recursive algorithm to handle revealing cells with no adjacent mines. When the player uncovers an empty cell, the algorithm automatically reveals all neighboring cells. If one of these neighboring cells is also empty, the recursion continues, uncovering all adjacent cells until no further empty cells are found. This mirrors the classic Minesweeper gameplay mechanic where large sections of the grid are uncovered at once.

## Random Mine Placement
Mines are randomly placed on the grid using an algorithm that ensures no two mines occupy the same cell. The number of mines can be adjusted based on the grid size or difficulty level.

## Gameplay Instructions
1. The player is presented with a grid of covered cells.
2. The player can select a cell to uncover.
   - If the cell contains a mine, the game ends.
   - If the cell does not contain a mine, it will reveal a number representing how many mines are adjacent.
   - If the cell has no adjacent mines, the recursive algorithm will automatically reveal surrounding cells.
3. The objective is to uncover all non-mine cells without triggering any mines.

## Commands
- **Reveal a cell:** Enter the row and column number to reveal a specific cell.
- **Flag a cell (optional):** Mark a cell as containing a suspected mine.
