import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A fully-functional implementation of the IndexedUnsortedList
 * interface as guided during CS221 lectures at Boise State
 * University in Fall 2026. ListIterator is unsupported.
 * 
 * @author Amira Freeman
 * @param <T> as an over-arching alias for the Object-type objects stored in the array
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {

    // XXX Array list instance variables
    private static final int DEFAULT_CAPACITY = 10;
    private static final int RESIZE_FACTOR = 2;
    private static final int NOT_FOUND = -1;
    private int rear, modCount;
    private T[] array;


    /**
     * Public constructor using a default array capacity with no parameters.
     */
    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Public constructor using a user-defined array capacity via integer parameter.
     * 
     * @param initialCapacity starting length of the array
     * @throws IllegalArgumentException if the provided initial capacity will create an unusable array
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException();
        }

        rear = 0;
        modCount = 0;
        array = (T[])(new Object[initialCapacity]);
    }


    // XXX Private helper methods for array list

    /**
     * Expands the array by double its current size if there is
     * no available space inside the array, or logically speaking,
     * if the end marker for the array equals or exceeds the
     * array's current capacity.
     */
    private void expandIfNecessary() {
        // rear > array.length should never technically be possible but it remains here as a safety net
        if (rear >= array.length) {
            array = Arrays.copyOf(array, array.length * RESIZE_FACTOR);
        }
    }

    /**
     * When adding or removing elements from the array, it is
     * necessary that all the items in the array are shifted to
     * their respective places before or after the modification
     * is made. This method, when given a direction and target
     * index, will shift the remainder of the array from a given
     * index to the left or right once an element is removed or
     * when an element is being added, respectively. The result
     * of this shifting will leave holes or duplicates in the
     * array if they are not handled appropriately in the
     * following statements after this method is called.
     * 
     * @param direction to which the items should be shifted
     * @param targetIndex to which the shifting should begin or end depending on the direction
     */
    private void shiftItems(String direction, int targetIndex) {
        switch (direction) {
            case "left" -> {
                for (int i = targetIndex; i < rear - 1; i++) {
                    array[i] = array[i + 1];
                }
            }
            case "right" -> {
                for (int i = rear; i > targetIndex; i--) {
                    array[i] = array[i - 1];
                }
            }
            default -> {
                throw new UnsupportedOperationException("See stack trace for incorrect call.");
            }
        }
    }

    /**
     * In order to remove an element from the array, it need not
     * be nulled before the remainder of the array is shifted
     * accordingly, so the array can simply be shifted. Once
     * this is done, there is a duplicate value remaining at the
     * end of the array, so this is nulled. The rear variable is
     * decremented to reflect the removed index and the modCount
     * variable is incremented to reflect the changed array.
     * 
     * @param index of which to shift values over
     */
    private void removeAt(int index) {
        shiftItems("left", index);
        array[rear - 1] = null;
        rear--;
        modCount++;
    }

    /**
     * Many public methods require a non-empty array to function
     * correctly, otherwise a NoSuchElementException is thrown.
     * This is verified by ensuring the marker which denotes the
     * end of array objects is also not the size of the list.
     * 
     * @throws NoSuchElementException if the array size is 0
     */
    private void requireNonEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Many public methods check if a given index is within the
     * scope of valid objects, which is at least 0 but before
     * the marker denoting the end of valid objects, rear.
     * 
     * @param index to verify is within the scope of valid objects in the array
     * @throws IndexOutOfBoundsException if the given index is less than zero or exceeds the scope of valid objects
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * After calling indexOf(), there remains a possibility
     * that the integer the method returned indicates that
     * the target element is not present in the array. As
     * such, before other methods can modify the array, this
     * return value must be compared to its known false value,
     * and throw an exception if they match.
     * 
     * @param index to verify was found by an indexOf() call
     * @throws NoSuchElementException if the element was not found by indexOf()
     */
    private void ensureFound(int index) {
        if (index == NOT_FOUND) {
            throw new NoSuchElementException();
        }
    }


    // XXX IndexedUnsortedList interface methods

    @Override
    public void addToFront(T element) {
        add(0, element);
    }

    @Override
    public void addToRear(T element) {
        add(rear, element);
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int index = indexOf(target);
        ensureFound(index);
        // avoid code duplication BUT also accommodate for the intended result of add()
        add(index + 1, element);
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > rear) {
            throw new IndexOutOfBoundsException();
        }
        expandIfNecessary();
        shiftItems("right", index);
        array[index] = element;
        rear++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        requireNonEmpty();
        return remove(0);
    }

    @Override
    public T removeLast() {
        requireNonEmpty();
        return remove(rear - 1);
    }

    @Override
    public T remove(T element) {
        int index = indexOf(element);
        ensureFound(index);
        return remove(index);
    }

    @Override
    public T remove(int index) {
        checkElementIndex(index);
        T returnElement = array[index];
        removeAt(index);
        return returnElement;
    }

    @Override
    public void set(int index, T element) {
        checkElementIndex(index);
        array[index] = element;
        modCount++;
    }

    @Override
    public T get(int index) {
        checkElementIndex(index);
        return array[index];
    }

    @Override
    public int indexOf(Object element) {
        int elementIndex = NOT_FOUND;
        int searchIndex = 0;
        while (elementIndex < 0 && searchIndex < rear) {
            if (array[searchIndex].equals(element)) {
                elementIndex = searchIndex;
            }
            searchIndex++;
        }
        return elementIndex;
    }

    @Override
    public T first() {
        requireNonEmpty();
        return array[0];
    }

    @Override
    public T last() {
        requireNonEmpty();
        return array[rear - 1];
    }

    @Override
    public boolean contains(Object target) {
        return (indexOf(target) != NOT_FOUND);
    }

    @Override
    public boolean isEmpty() {
        return (rear == 0);
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public String toString() {
        // No need to build a string for an empty array
        if (rear == 0) {
            return "[ ]";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for (int i = 0; i < rear; i++) {
            builder.append(array[i]);
            builder.append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        builder.append(" ]");
        return builder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new ALIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * A fully-functional private iterator class for an
     * instance of IUArrayList.
     * 
     * @author Amira Freeman
     */
    private class ALIterator implements Iterator<T> {

        // XXX Iterator instance variables

        private int nextIndex, iterModCount;
        private boolean canRemove;


        /**
         * Private-scope constructor for iterating through an array list
         */
        private ALIterator() {
            nextIndex = 0;
            iterModCount = modCount;
            canRemove = false;
        }


        // XXX Private helper methods for array list iterator

        /**
         * If the array is modified in any way, even by a simple set() call,
         * then the promise the iterator makes at its creation is no longer
         * valid, and cannot be trusted to relay valid information about the
         * array, and thus will destroy itself and throw an exception.
         * 
         * @throws ConcurrentModificationException if the array has been modified via any non-iterator method and can no longer be trustworthy
         */
        private void fastfailCheck() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }


        // XXX Iterator interface methods

        @Override
        public boolean hasNext() {
            fastfailCheck();
            return (nextIndex < rear);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = array[nextIndex];
            nextIndex++;
            canRemove = true;
            return element;
        }

        @Override 
        public void remove() {
            fastfailCheck();
            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;
            removeAt(nextIndex - 1);
            iterModCount = modCount;
            nextIndex--;
        }
        
    }

}