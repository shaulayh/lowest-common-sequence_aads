import java.util.*;

import static java.lang.Integer.max;

/**
 * @Author Azeez G. Shola
 */

public class Main {

    public static void main(String[] args) {
//        String firstString = "AGTGATG";
//        String secondString = "GTTAG";

//        String firstString = "XMJYAUZ ";
//        String secondString = "MZJAWXU";

        String firstString = "ABCBDAB";
        String secondString = "BDCABA";
        System.out.println(lcsLength(secondString, firstString));

        lcs(secondString, firstString);


        String firstEdit = "SUNDAY";
        String secondEdit = "SATURDAY";
        System.out.println(editDistance(firstEdit, secondEdit));
        System.out.println(callRecursion("SUNDAY", "SATURDAY"));
    }

    private static int lcsLength(String A, String B) {
        if ((A.length() == 0) && (B.length() == 0)) {
            return 0;
        } else if ((A.length() > 0) && (B.length() > 0)) {
            if (A.charAt(A.length() - 1) != B.charAt(B.length() - 1)) {
                return Math.max(lcsLength(A, B.substring(0, B.length() - 1)),
                        lcsLength(A.substring(0, A.length() - 1), B));
            }
            return 1 + lcsLength(A.substring(0, A.length() - 1),
                    B.substring(0, B.length() - 1));
        }
        return 0;
    }


    private static void lcs(String X, String Y) {
        int m = X.length();
        int n = Y.length();
        int[][] dynamicMatrix = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0)
                    dynamicMatrix[i][j] = 0;
                else if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    dynamicMatrix[i][j] = dynamicMatrix[i - 1][j - 1] + 1;
                } else
                    dynamicMatrix[i][j] = max(dynamicMatrix[i - 1][j], dynamicMatrix[i][j - 1]);
            }
        }

        int index = dynamicMatrix[m][n];
        char[] lcs = new char[index];
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                lcs[index - 1] = X.charAt(i - 1); // copy to result
                i--;
                j--;
                index--;     // move to next matching character
            } else if (dynamicMatrix[i - 1][j] > dynamicMatrix[i][j - 1])
                i--;
            else
                j--;
        }
        print2D(dynamicMatrix, m, n);
        System.out.println("LCS of " + Y + " and " + X + " is " + Arrays.toString(lcs));
    }

    private static void print2D(int[][] dynamicMatrix, int m, int n) {
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.print(dynamicMatrix[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    private static int editDistance(String A, String B) {
        int[][] distance = new int[A.length() + 1][B.length() + 1];
        int distanceIn;
        int m = A.length();
        int n = B.length();
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    distance[i][j] = j;
                } else if (j == 0) {
                    distance[i][j] = i;
                } else {
                    if (A.charAt(i - 1) == B.charAt(j - 1)) {
                        distance[i][j] = distance[i - 1][j - 1];
                    } else {
                        int sub = distance[i - 1][j - 1];
                        int del = distance[i][j - 1];
                        int ins = distance[i - 1][j];
                        distance[i][j] = minOfThree(sub, del, ins) + 1;
                    }
                }
            }
        }
        print2D(distance, m, n);
        distanceIn = distance[m - 1][n - 1];

        print(A, B, distance);

//        for (int i = 1; i < A.length(); i++) {
//            for (int j = 1; j < B.length(); j++) {
//                int del = distance[i - 1][j] + 1;
//                int ins = distance[i][j - 1] + 1;
//                int sub = distance[i - 1][i - 1] + (A.charAt(i) == B.charAt(j) ? 0 : 1);
//
//                distanceIn = minOfThree(del, sub, ins);
//                if (distance[i][j] == del) {
//                    solution[i][j] = "UP";
//                } else if (distance[i][j] == ins) {
//                    solution[i][j] = "left";
//                } else {
//                    solution[i][j] = "up-left";
//                }
//
//            }
//        }
        return distanceIn;
    }


    private static int minOfThree(int num1, int num2, int num3) {
        return Math.min(num1,
                Math.min(num2, num3));

    }

    private static int callRecursion(String A, String B) {
        int[][] dynamicTable = new int[A.length()][B.length()];
        for (int[] row : dynamicTable) {
            Arrays.fill(row, -1);
        }
        return editDistanceRecursion(A, B, A.length() - 1,
                B.length() - 1, dynamicTable);
    }

    private static int editDistanceRecursion(String A, String B, int m, int n,
                                             int[][] dynamicTable) {
        if (m < 0) {
            return n + 1;
        } else if (n < 0) {
            return m + 1;
        }

        if (dynamicTable[m][n] == -1) {
            if (A.charAt(m) == B.charAt(n)) {
                dynamicTable[m][n] = editDistanceRecursion(A, B,
                        m - 1,
                        n - 1, dynamicTable);
            } else {
                int sub = editDistanceRecursion(A, B, m - 1, n - 1,
                        dynamicTable);
                int ins = editDistanceRecursion(A, B, m, n - 1,
                        dynamicTable);
                int del = editDistanceRecursion(A, B, m - 1, n,
                        dynamicTable);

                dynamicTable[m][n] = 1 + Math.min(sub,
                        Math.min(ins, del));
                dynamicTable[m][n] = 1 + minOfThree(sub,
                        ins,
                        del);
            }
        }
        return dynamicTable[m][n];
    }


    private static void print(String A, String B, int[][] distance) {
        List<String> list = new ArrayList<>();
        int i = A.length();
        int j = B.length();

        StringBuilder stringBuilder = new StringBuilder();
        while (i != 0 && j != 0) {

            if (A.charAt(i - 1) == B.charAt(j - 1)) {
                stringBuilder.append(A.charAt(i - 1));
                list.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.capacity());
                i--;
                j--;
            } else if (distance[i][j] == distance[i - 1][j - 1] + 1) {
                stringBuilder.append("Sub( ")
                        .append(A.charAt(i - 1))
                        .append(" , ").
                        append(B.charAt(j - 1)).
                        append(")");
                list.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.capacity());
                i--;
                j--;
            } else if (distance[i][j] == distance[i - 1][j] + 1) {
                stringBuilder.append("del( ").append(A.charAt(i - 1)).append(")");
                list.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.capacity());
                i--;
            } else if (distance[i][j] == distance[i][j - 1] + 1) {
                stringBuilder.append("ins( ").append(B.charAt(j - 1)).append(")");
                list.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.capacity());
                j--;
            }
        }


        Collections.reverse(list);
        list.forEach(x -> System.out.print(x + "\t"));
        System.out.println();

    }

}