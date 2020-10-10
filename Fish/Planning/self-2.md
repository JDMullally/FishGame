## Self-Evaluation Form for Milestone 2

A fundamental guideline of Fundamentals I, II, and OOD is to design
methods and functions systematically, starting with a signature, a
clear purpose statement (possibly illustrated with examples), and
unit tests.

Under each of the following elements below, indicate below where your
TAs can find:

- the data description of tiles, including an interpretation:

<https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/src/model/Tile.java#L1-L52>

**A Tile contains information about where it is on a GameBoard, the number of fish it has, and it's visual representation.**

- the data description of boards, include an interpretation:

<https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/src/model/IGameBoard.java#L1-L79>

**An IGameBoard contains information about all the tiles on the game board, the board dimensions & layout, and contains operations to manipulate the gameboard.**


- the functionality for removing a tile:

  - purpose:
  
  <https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/src/model/GameBoard.java#L294-L304>
  
  **Replaces a Tile or the Tile at a specified Point with an EmptyTile**
  
  - signature:
  
  <https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/src/model/IGameBoard.java#L66-L79>
  
  **Takes in a Tile or a Point and returns the Tile being removed**
  
  - unit tests:
  
  <https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/test/model/GameBoardTest.java#L325-L344>
  
  

- the functionality for reaching other tiles on the board:

  - purpose:
  
  <https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/src/model/GameBoard.java#L170-L279>

  **Returns a List of Tile's that can be moved to from a current Tile or Point**
  
  - signature:
  
  <https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/src/model/IGameBoard.java#L52-L64>
  
  **Takes in a Tile or a Point and returns a List of Tile's that can be moved to**
  
  - unit tests:
  
  <https://github.ccs.neu.edu/CS4500-F20/texline/blob/2d8042af3504dcd2cf01913f7a817dfec0af0667/Fish/Common/test/model/GameBoardTest.java#L201-L323>

The ideal feedback is a GitHub perma-link to the range of lines in specific
file or a collection of files for each of the above bullet points.

  WARNING: all such links must point to your commit "1da05aa73f83065cc519f21cd41dc671f60f0802".
  Any bad links will result in a zero score for this self-evaluation.
  Here is an example link:
    <https://github.ccs.neu.edu/CS4500-F20/texline/tree/1da05aa73f83065cc519f21cd41dc671f60f0802/Fish>

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

In either case you may wish to, beneath each snippet of code you
indicate, add a line or two of commentary that explains how you think
the specified code snippets answers the request.
