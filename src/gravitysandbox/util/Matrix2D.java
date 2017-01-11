package gravitysandbox.util;

import java.util.ArrayList;
// TODO: Documentation
public class Matrix2D<T> {

    public ArrayList<ArrayList<T>> matrix;

    public Matrix2D() {
        matrix = new ArrayList<>();
    }

    public Matrix2D(T[][] array) {
        this();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if ((array[i][j] != null))
                    add(array[i][j], i, j);
            }
        }
    }

    public void add(T elem, int index1, int index2) {
        if (matrix.get(index1) == null) {
            matrix.add(index1, new ArrayList<>());
        }

        matrix.get(index1).add(index2, elem);
    }

    // TODO: When last element of ArrayList is removed also remove the ArrayList?
    public void remove(int index1, int index2) {
        matrix.get(index1).set(index2, null);

        if (matrix.get(index1).size() == 0) {
            matrix.set(index1, null);
        }
    }

    public T get(int index1, int index2) {
        return matrix.get(index1).get(index2);
    }

    public int[] find(T elem) {
        int i , j = i = 0 ;

        for (ArrayList al : matrix) {
            for (Object e : al) {
                if (e.equals(elem)) return new int[]{i,j};
                j++;
            }
            i++;
        }

        return null;
    }

}