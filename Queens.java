/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Iryna Naminat
 */
import java.util.*;
//import java.util.Random;

public class Queens {

    int hCurrent = 0;
    int restarts = 0;
    int firstQ;
    int neighbors = 0;
    int moves = 0;

    public static void main(String[] args) {
        // TODO code application logic here
        int[][] queens = new int[8][8];
        Queens q = new Queens();
        int hGoal = 0;

        //new array with random queens
        queens = q.placement();
        if (q.checkState(queens) == hGoal) {
            System.out.println("Current heuristic is the goal: " + q.checkState(queens));
        }

        q.moveQ(queens);

    }// end of main method

    //method to create the initial state of Queens
    public int[][] placement() {
        int[][] a = new int[8][8];
        System.out.println("Current State");
        Random rand = new Random();
        //lists for the random numbers of indexes within 2D array
        //where we are going to put ours Queens
        List<Integer> colNum = new ArrayList<>();
        List<Integer> rawNum = new ArrayList<>();
        // loop to create random numbers for X and Y indexes 
        for (int i = 0; i < a.length; i++) {
            int random = rand.nextInt(a.length);
            //add all random number within range 8 for index of the raws 
            rawNum.add(random);
            //take all numbers in a loop(0-7) for column and shuffle them
            //to have randomly choosen all column
            colNum.add(i);
            Collections.shuffle(colNum);
        }
        //loop to put our Queens on a board
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                //check where to put 1 according to random indexes from ArrayLists
                int randomX = rawNum.get(y);
                int randomY = colNum.get(y);
                //need to have extra condition for 0 raw 
                if (randomX == 0) {
                    a[0][randomY] = 1;
                }
                a[randomX][randomY] = 1;

                System.out.print(a[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println("Current heuristic : " + checkState(a) + " ");
        return a;
    }//the end of a method putting Queens on a board

    //method to check  state        
    public int checkState(int[][] a) {
        int hRaw = 0;
        int hRbottom = 0;
        int hLbottom = 0;
        //check if there any Queens in a same raw 
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                //assign first 1 in a raw to the firstQ variable
                if (a[x][y] == 1) {
                    firstQ = a[x][y];
                    //check all remaining numbers ia a raw if there any queens else  
                    for (int n = y + 1; n < a.length; n++) {
                        if (a[x][n] == firstQ) {
                            //calculate heuristic by adding allpossible combination for Queens in a raw 
                            hRaw++;
                        }
                    }
                }//end if

                //check bottom right diagonals
                if (a[x][y] == 1 && y != 7) {
                    firstQ = a[x][y];
                    for (int k = x + 1; k < a.length - 1; k++) {
                        if (a[k][y + 1] == firstQ) {
                            hRbottom++;
                        }
                    }
                }//end if 

                //check bottom left diagonals
                if (a[x][y] == 1 && y != 0) {
                    firstQ = a[x][y];
                    for (int k = x + 1; k < a.length - 1; k++) {
                        if (a[k][y - 1] == firstQ) {
                            hLbottom++;
                        }
                    }
                }//end if                     
            }  //end inner loop                   
        }//end outter loop
        hCurrent = hRaw + hLbottom + hRbottom;

        return hCurrent;
    }//the end of a checkState method      

    //method to find 1 within given column
    public static int findQ(int[][] a, int colNum) {
        int rawNum = 0;
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                //find coordintes for 1
                if (a[x][colNum] == 1) {
                    rawNum = x;
                }
            }
        }
        return rawNum;
    }//the end findQ method 

    //method to remove Queen within a column
    public int[][] removeQ(int[][] a, int col) {
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                //set current one to zero
                if (x == findQ(a, col)) {
                    a[x][y] = 0;
                }// end if statement 
            }
        }
        return a;
    }

    //method to find number for column with minimum neighbor state
    public int numCol(int[][] a) {
        int numCol = 0;
        int min = 0;
        int count = 0;

        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a.length; y++) {
                if (a[x][y] < a.length) {
                    min = a[x][y];
                    numCol = y;
                }
                if (a[x][y] < hCurrent) {
                    count++;
                }
            }
        }
        neighbors = count;
        return numCol;
    }
    //method to find number for raw with minimum neighbor state  

    public int numRaw(int[][] a) {
        int minRaw = 0;
        int minVal = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (a[x][y] < a.length) {
                    minVal = a[x][y];
                    minRaw = x;
                }
            }
        }
        return minRaw;
    }

    //method to move Queen within a column
    public void moveQ(int[][] a) {
        //int [][] a = new int[8][8];
        int[][] temp = new int[8][8];
        int[][] countState = new int[8][8];
        int qFound = 0;
        int minRaw = 0;
        int minCol = 0;
        int heuristic = 0;
        int col;

        while (true) {
            col = 0;
            temp = a.clone();

            while (col < a.length) {
                for (int x = 0; x < a.length; x++) {
                    removeQ(temp, col);
                }

                for (int x = 0; x < 8; x++) {
                    if (a[x][col] == 1) {
                        qFound = x;
                    }
                    temp[x][col] = 1;
                    countState[x][col] = checkState(temp);
                    temp[x][col] = 0;
                }
                temp[qFound][col] = 1;
                col++;
            }//end while loop

            if (restart(countState) == true) {
                restarts++;
            }// if restart statement

            minCol = numCol(countState);
            minRaw = numRaw(countState);

            for (int i = 0; i < 8; i++) {
                a[i][minCol] = 0;
            }

            a[minRaw][minCol] = 1;
            moves++;
            heuristic = checkState(a);

            if (checkState(a) == 0) {
                System.out.println("\nCurrent State");
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        System.out.print(a[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println("Solution Found!");
                System.out.println("State changes: " + moves);
                System.out.println("Restarts: " + restarts);
                break;
            }

            System.out.println("\n");
            System.out.println("Current h: " + heuristic);
            System.out.println("Current State");
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(a[i][j] + " ");
                }
                System.out.print("\n");
            }

            System.out.println("Neighbors found with lower h: " + neighbors);
            System.out.println("Setting new current State");

        }//end outter  while  loop

    }// the end of moveQ method

    public boolean restart(int[][] a) {
        int minState = a.length;
        boolean restart = false;

        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a.length; y++) {
                if (a[y][x] < minState) {
                    minState = a[x][y];
                }
            }
        }
        if (neighbors == 0) {
            restart = true;
            System.out.println("RESTART");
            placement();
        }
        return restart;
    } //end restart method

}
