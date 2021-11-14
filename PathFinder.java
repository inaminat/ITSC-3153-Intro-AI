
/**
 *
 * @author Iryna Naminat
 * Program creates array 15x15 with random put blocked point.
 * The block point represented by zeros
 */
import java.util.*;

public class PathFinder {

    public ArrayList<Node> openList = new ArrayList<Node>();
    public HashMap<Node, Integer> closedList = new HashMap<>();
    public ArrayList<Node> parentList = new ArrayList<Node>();

    public static void main(String[] args) {

        PathFinder q = new PathFinder();
        q.aStar();

    }//end main method

    // A* method
    public void aStar() {
        Node currentNode = null, finishNode = null;
        int start = 0, finish = 0;
        int a[][] = null;
        //setUp array, start point and endpoint
        a = grid();
        start = startPoint();
        finish = endPoint();

        //check if start point in Array not blocked (0)
        boolean b = elementInArray(a, start);
        boolean c = elementInArray(a, finish);
        if (b == false) {
            System.out.println("\n" + "You can't start at the blocked point");
            start = startPoint();
            b = true;
        }

        // set Start point as a Start node
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                if (a[x][y] == start) {
                    currentNode = new Node(x, y);
                    //add start point to the OpenList
                    openList.add(currentNode);
                    break;
                }
            }
        }
        //check if finish point in Array not blocked (0)
        if (c == false) {
            System.out.println("\n" + "You can't finished at the blocked point");
            finish = endPoint();
            c = true;
        }
        // set end Poind as a Finish Node
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                if (a[x][y] == finish) {
                    finishNode = new Node(x, y);
                    break;
                }
            }
        }

        if (!openList.isEmpty()) {
            for (int i = 0; i < openList.size(); i++) {

                //add all avaiable neighbours to the OpenList
                openList = neighboursNodes(a, currentNode.getRow(), currentNode.getCol(), finish);
                //put current Node to closed List     
                closedList.put(currentNode, start);
                //remove current(Start) Node from Open List 
                openList.remove(currentNode);
                //set Node with min F as current
                currentNode = minF();
                parentList.add(currentNode);

                if (currentNode.equals(finishNode)) {
                    closedList.put(currentNode, i);
                    System.out.println("\n" + "Generated path: " + parentList); 
                    System.out.println();
                    break;
                }

            }
        }

        //System.out.println("Nodes in the OpenList: " + openList);

    }// end aStar method

    //method to create array 
    public int[][] grid() {
        int[][] a = new int[15][15];
        int b = 1;
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                a[x][y] = b;
                int k = obstacle();
                if (k <= 10) {
                    a[x][y] = 0;
                }
                System.out.printf("%-5s", a[x][y]);
                b++;
            }
            System.out.println();
        }
        return a;
    }// end grid method

    //method to set rundom probability for blicked point
    public int obstacle() {
        Random r = new Random();
        int randomInt = r.nextInt(100) + 1;
        return randomInt;
    }//end obstacle method

    //method to ask user create Sart point
    public int startPoint() {
        Scanner scnr = new Scanner(System.in);
        int s = 0;
        try {
            System.out.println("\n" + "Enter Starting Point within range 1-225");
            s = scnr.nextInt();
            IllegatArgumentExeption(s);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            s = scnr.nextInt();
        }
        return s;
    }
    
     //method to ask user create End point
    public int endPoint() {
        Scanner scnr = new Scanner(System.in);
        int s = 0;
        try {
            System.out.println("\n" + "Enter End Point within range 1-225");
            s = scnr.nextInt();
            IllegatArgumentExeption(s);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            s = scnr.nextInt();
        }
        return s;
    }
    //method to create exeption 
    public static void IllegatArgumentExeption(int s) {
        while (s <= 0 || s > 225) {
            throw new IllegalArgumentException("Choose, please, number within 1-225 range");
        }
    }
    
    //methid to count heuristic
    public int checkH(int[][] a, int finish, int curent) {
        int hCurrent = 0;
        int indexX = 0, indexY = 0;
        int indexX1 = 0, indexY1 = 0;
        int length = 0, width = 0;

        //check where start and finish point
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                if (a[x][y] == finish) {
                    indexX = x;
                    indexY = y;
                }
                if (a[x][y] == curent) {
                    indexX1 = x;
                    indexY1 = y;
                }
                length = Math.abs(indexY - indexY1);
                width = Math.abs(indexX - indexX1);
            }  //end inner loop                   
        }//end outter loop
        hCurrent = Math.abs(length + width) * 10;
        return hCurrent;
    }//the end of a checkH method      
    
    //method creates list of all close available neighbours
    public ArrayList neighboursNodes(int[][] a, int row, int col, int finish) {
        int current;
        int start = 0;

        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                if (row == x && col == y) {
                    start = a[x][y];
                }
            }
        }

        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                //upper boundaries
                if (a[x][y] == start && (x - 1) >= 0 && (y - 1) >= 0 && (y + 1) < a.length) {
                    //upper boundaries not a block
                    if (a[x - 1][y - 1] != 0) {
                        current = a[x - 1][y - 1];
                        //add this child to openList if all conditions are True
                        neighbourDiag(a, x - 1, y - 1, finish, current);
                    }
                    if (a[x - 1][y + 1] != 0) {
                        current = a[x - 1][y + 1];
                        //add this child to openList if all conditions are True
                        neighbourDiag(a, x - 1, y + 1, finish, current);
                    }
                    if (a[x - 1][y] != 0) {
                        current = a[x - 1][y];
                        //add this child to openList if all conditions are True
                        neighbourLine(a, x - 1, y, finish, current);
                    }
                }
                //bottom boundaries
                if (a[x][y] == start && (x + 1) >= 0 && (y - 1) >= 0 && (y + 1) < a.length) {
                    //bottom boundaries not a block
                    if (a[x + 1][y - 1] != 0) {
                        current = a[x + 1][y - 1];
                        //add this child to openList if all conditions are True 
                        neighbourDiag(a, x + 1, y - 1, finish, current);
                    }
                    if (a[x + 1][y + 1] != 0) {
                        current = a[x + 1][y + 1];
                        //add this child to openList if all conditions are True
                        neighbourDiag(a, x + 1, y + 1, finish, current);
                    }
                    if (a[x + 1][y] != 0) {
                        current = a[x + 1][y];
                        //add this child to openList if all conditions are True
                        neighbourLine(a, x + 1, y, finish, current);
                    }
                }
                //the side blocks
                if (a[x][y] == start && (y - 1) >= 0 && (y + 1) < a.length) {
                    //upper boundaries not a block
                    if (a[x][y - 1] != 0) {
                        current = a[x][y - 1];
                        //add this child to openList if all conditions are True
                        neighbourLine(a, x, y - 1, finish, current);
                    }
                    if (a[x][y + 1] != 0) {
                        current = a[x][y + 1];
                        //add this child to openList if all conditions are True
                        neighbourLine(a, x, y + 1, finish, current);
                    }
                }
            }
        }

        return openList;
    }//the end of a childsNodes method      

    //method set F to diagonal childs
    public ArrayList neighbourDiag(int a[][], int x, int y, int finish, int current) {
        Node child = new Node(x, y);
        child.setH(checkH(a, finish, current));
        child.setG(14);
        child.setF();
        openList.add(child);
        return openList;
    }//end neihgbourDiag  

    //method set F to line childs
    public ArrayList neighbourLine(int a[][], int x, int y, int finish, int current) {
        Node child = new Node(x, y);
        child.setH(checkH(a, finish, current));
        child.setG(10);
        child.setF();
        openList.add(child);
        return openList;

    }//end neighbourLine        
    
    //method to check is start and end point exist in array
    public static boolean elementInArray(int a[][], int element) {
        boolean exist = false;
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[x].length; y++) {
                if (a[x][y] == element) {
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

    //method to find child with smallest F
    public Node minF() {
        Node fminNode = null;
        int fmin = openList.get(0).getF();
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getF() <= fmin) {
                fmin = openList.get(i).getF();
                fminNode = openList.get(i);
            }
        }
        return fminNode;
    }

} //end PathFinder class

