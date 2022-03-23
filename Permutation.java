import java.util.List;
import java.util.Random;

public class Permutation
{
    // Stores the number of objects being acted upon
    private int numObjects;

    // Stores the indices that need to be swapped
    private int[] indices;

    // Stores if the permutation is even
    private boolean isEven;

    /**
     * Create a Permutation for n objects with numSwaps swaps
     * @param n        the number of objects to permute
     * @param numSwaps the number of swaps to do
     * @return a Permutation on n objects with numSwaps swaps
     * @throws IllegalArgumentException if n or numSwaps is negative
     */
    public static Permutation randomPermutation(int n, int numSwaps)
    {
        // Validate n and numSwaps
        if (n < 0)
        {
            throw new IllegalArgumentException("n must be non-negative");
        }
        if (numSwaps < 0)
        {
            throw new IllegalArgumentException("numSwaps must be non-negative");
        }

        // Generate the random list
        Random rand = new Random();
        int[] indices = new int[2*numSwaps];
        for (int i = 0; i < 2*numSwaps; i++)
        {
            indices[i] = rand.nextInt(n);
        }

        return new Permutation(n, indices);
    }

    /**
     * Create a random permutation on n objects with at least minSwaps
     * swaps and no more than maxSwaps swaps
     * @param n        the number of objects to permute
     * @param minSwaps the minimum number of swaps to generate
     * @param maxSwaps the maximum number of swaps to generate
     * @return a new Permutation on n objects with [minSwaps, maxSwaps)
     * swaps
     * @throws IllegalArgumentException if n or minSwaps is negative or if
     * maxSwaps is less than or equal to minSwaps
     */
    public static Permutation randomPermutation(int n, int minSwaps, int maxSwaps)
    {
        // Validate n, minSwaps, and maxSwaps
        if (n < 0)
        {
            throw new IllegalArgumentException("n must be non-negative");
        }
        if (minSwaps < 0)
        {
            throw new IllegalArgumentException("minSwaps must be non-negative");
        }
        if (maxSwaps <= minSwaps) {
            throw new IllegalArgumentException("maxSwaps must be greater than minSwaps");
        }

        // Calculate the number of swaps to do
        Random rand = new Random();
        int swaps = rand.nextInt(maxSwaps - minSwaps) + minSwaps;

        // Use another static method to do the heavy lifting
        return randomPermutation(n, swaps);
    }

    // indices is an even length array of ints
    // Each pair of ints reperesents a swap
    // Ex. {3, 4, 5, 6} represents swapping 3 and 4, then 5 and 6
    private Permutation(int n, int[] indices)
    {
        // Make sure indicies is even
        if (indices.length % 2 != 0)
        {
            throw new IllegalArgumentException("indices must have an even length");
        }
        
        this.indices = indices;
        this.numObjects = n;
        this.isEven = (numObjects % 2) == 0;
    }

    /**
     * Returns whether or not this permutation is even.
     * @return true if the permutation is even, false otherwise
     */
    public boolean isEven()
    {
        return isEven;
    }

    /**
     * Apply this permutation to the given list
     * @param list - a list with the same length as was given to the
     * permutation
     * @throws IllegalArgumentException if list has the wrong length
     */
    public <T> void applyToList(List<T> list)
    {
        // Validate the list of the length
        if (list.size() != numObjects)
        {
            throw new IllegalArgumentException("list must be the same length as the permutation");
        }

        // Apply the permutation
        // Iterate over every other index
        // Swap the current and next indices
        // Ex. 2,3,6,5 would swap 2 and 3, then 6 and 5
        for (int i = 0; i < indices.length; i += 2)
        {
            swap(list, indices[i], indices[i+1]);
        }
    }

    /**
     * Apply this permutation to the given array
     * @param array - an array with the same length as was given to the
     * permutation
     * @throws IllegalArgumentException if array has the wrong length
     */
    public <T> void applyToArray(T[] array)
    {
        // Validate the array length
        if (array.length != numObjects)
        {
            throw new IllegalArgumentException("array must be the same length as the permutation");
        }

        // Apply the permutation
        // Iterate over every other index
        // Swap the current and next indices
        // Ex. 2,3,6,5 would swap 2 and 3, then 6 and 5
        for (int i = 0; i < indices.length; i += 2)
        {
            swap(array, indices[i], indices[i+1]);
        }
    }

    /**
     * Apply this permutation to the given 2D array. The 1D indices are
     * converted to 2D indices such that {{1, 2}, {3, 4}} is treated as
     * {1, 2, 3, 4}.
     * @param array - a 2D array with the same number of items as was 
     * given to the permutation
     * @throws IllegalArgumentException if array has the wrong number of
     * items
     */
    public <T> void applyTo2DArray(T[][] array)
    {
        // Validate the array size
        if (array.length * array[0].length != numObjects)
        {
            throw new IllegalArgumentException("array must have the same number of items as the permutation");
        }

        // Apply the permutation
        // Convert the 1D indices into 2D indices
        for (int i = 0; i < indices.length; i += 2)
        {
            int a = indices[i];
            int b = indices[i+1];
            int len = array.length;

            swap(array, a % len, a / len, b % len, b / len);
        }
    }

    // Swap the ith and jth elements in list
    private <T> void swap(List<T> list, int i, int j)
    {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Swap the ith and jth elements in array
    private <T> void swap(T[] array, int i, int j)
    {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Swap the ith and jth elements in a 2D array
    private <T> void swap(T[][] array, int ix, int iy, int jx, int jy)
    {
        T temp = array[iy][ix];
        array[iy][ix] = array[jy][jx];
        array[jy][jx] = temp;
    }
}
