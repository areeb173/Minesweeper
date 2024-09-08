import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {

    // Inner class representing a single tile on the board
    private class MineTile extends JButton {
        int r;  // Row index of the tile
        int c;  // Column index of the tile

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    // Constants and variables for board size and layout
    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize + 100;  // Height includes space for restart button

    // GUI components
    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton restartButton = new JButton("Restart");

    // Game state variables
    int mineCount = 10;  // Number of mines
    MineTile[][] board = new MineTile[numRows][numCols];  // 2D array of tiles
    ArrayList<MineTile> mineList;  // List to track mine positions
    Random random = new Random();  // Random number generator for mine placement

    int tilesClicked = 0;  // Tracks how many non-mine tiles have been clicked
    boolean gameOver = false;  // Flag to check if the game is over

    // Constructor for initializing the game
    Minesweeper() {
        // Set up the frame (window)
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set up the label at the top (to display game status)
        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + mineCount);  // Show initial mine count

        // Add the label and restart button to the top panel
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.NORTH);
        textPanel.add(restartButton, BorderLayout.SOUTH);  // Add restart button below label
        frame.add(textPanel, BorderLayout.NORTH);

        // Set up the board panel (grid layout for the game tiles)
        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);

        // Create the board with MineTile buttons
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;  // Add tile to the 2D board array

                // Style and add mouse listeners to handle clicks
                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;  // Ignore clicks if the game is over

                        MineTile tile = (MineTile) e.getSource();

                        // Left click to reveal the tile
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText().equals("")) {
                                if (mineList.contains(tile)) {
                                    revealMines();  // Game over if mine clicked
                                } else {
                                    checkMine(tile.r, tile.c);  // Check for adjacent mines
                                }
                            }
                        }
                        // Right click to flag or unflag the tile
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText().equals("") && tile.isEnabled()) {
                                tile.setText("❌");  // Flag tile
                            } else if (tile.getText().equals("❌")) {
                                tile.setText("");  // Remove flag
                            }
                        }
                    }
                });

                boardPanel.add(tile);  // Add tile to the board panel
            }
        }

        frame.setVisible(true);  // Make the game window visible

        // Add action listener to the restart button
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();  // Reset the game when restart is clicked
            }
        });

        setMines();  // Place mines on the board
    }

    // Randomly place mines on the board
    void setMines() {
        mineList = new ArrayList<>();
        
        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);  // Add to the list of mines
                mineLeft--;
            }
        }
    }

    // Reveal all mines on the board (triggered when a mine is clicked)
    void revealMines() {
        for (MineTile tile : mineList) {
            tile.setText("💣");  // Display bomb emoji on mines
        }

        gameOver = true;  // Set game over state
        textLabel.setText("Game Over!");  // Update status label
    }

    // Check the clicked tile and reveal surrounding tiles if no mines are adjacent
    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;  // Return if out of bounds
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;  // Skip if tile already revealed
        }
        tile.setEnabled(false);
        tilesClicked++;  // Increment clicked tiles counter

        int minesFound = 0;

        // Check for mines in surrounding tiles
        minesFound += countMine(r-1, c-1);  // top left
        minesFound += countMine(r-1, c);    // top
        minesFound += countMine(r-1, c+1);  // top right
        minesFound += countMine(r, c-1);    // left
        minesFound += countMine(r, c+1);    // right
        minesFound += countMine(r+1, c-1);  // bottom left
        minesFound += countMine(r+1, c);    // bottom 
        minesFound += countMine(r+1, c+1);  // bottom right

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));  // Show number of surrounding mines
        } else {
            tile.setText("");  // Blank tile for no adjacent mines

            // Recursively check adjacent tiles
            checkMine(r-1, c-1);  // top left
            checkMine(r-1, c);    // top 
            checkMine(r-1, c+1);  // top right
            checkMine(r, c-1);    // left
            checkMine(r, c+1);    // right
            checkMine(r+1, c-1);  // bottom left
            checkMine(r+1, c);    // bottom
            checkMine(r+1, c+1);  // bottom right
        }

        // Check if all non-mine tiles have been revealed
        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");  // Display victory message
        }
    }

    // Count how many mines surround a specific tile
    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;  // Return 0 if out of bounds
        }
        if (mineList.contains(board[r][c])) {
            return 1;  // Return 1 if there is a mine in this tile
        }
        return 0;
    }

    // Reset the game state and board for a new game
    void resetGame() {
        gameOver = false;
        tilesClicked = 0;
        textLabel.setText("Minesweeper: " + mineCount);  // Reset label text

        // Reset each tile on the board
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = board[r][c];
                tile.setText("");  // Clear text on the tile
                tile.setEnabled(true);  // Re-enable the tile
            }
        }

        setMines();  // Place new mines
    }
}
