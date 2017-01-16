package gravitysandbox.util;

import java.util.ArrayList;

/**
 * A two dimensional matrix with dynamic size allocation.
 * @param <T> The type of elements in this Matrix.
 * @author Christoph Bruckner
 * @version 1.0
 * @since 0.1
 */
public class Matrix2D<T> {

    /**
     * The stored data.
     */
    private ArrayList<ArrayList<T>> matrix;

    /**
     * Creates a new empty Matrix2D object.
     */
    public Matrix2D() {
        matrix = new ArrayList<>();
    }

    /**
     * Creates a new Matrix2D object with the given initial values.
     * @param array The initial values. First dimension is the column, second dimension is the row.
     */
    public Matrix2D(T[][] array) {
        this();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if ((array[i][j] != null))
                    set(array[i][j], i, j);
            }
        }
    }

    /**
     * Adds a new or replaces an element at the given position.
     * @param elem The new element.
     * @param column The column index.
     * @param row The row index.
     */
    public void set(T elem, int column, int row) {
        if (matrix.get(column) == null) {
            matrix.add(column, new ArrayList<>());
        }

        if (matrix.get(column).get(row) == null) {
            matrix.get(column).add(row, elem);
        } else {
            matrix.get(column).set(row, elem);
        }
    }

    /**
     * Removes the element at the specified location.
     * @param column The column index.
     * @param row The row index.
     */
    public void remove(int column, int row) {
        matrix.get(column).remove(row);

        // TODO: When last element of ArrayList is removed also remove the ArrayList?
        if (matrix.get(column).size() == 0) {
            matrix.remove(column);
        }
    }

    /**
     * Return the element at the given location.
     * @param column The column index of the element.
     * @param row The row index of the element.
     * @return The element stored in the given matrix cell.
     */
    public T get(int column, int row) {
        return matrix.get(column).get(row);
    }

    /**
     * Finds an element in the matrix and returns its column and row id as an int array.
     * @param elem The element to search for.
     * @return The column and row id as an int array. Index 0 represents the column and index 1 the row.
     */
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