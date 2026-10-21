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

    private static int DEFAULT_CAPACITY = 10;
    private int rear, modCount;
    private T[] array;

    /* Public constructor using a default array capacity with no parameters */
    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Public constructor using a user-defined array capacity via integer parameter 
     * @param initialCapacity
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity) {
        rear = 0;
        modCount = 0;
        array = (T[])(new Object[initialCapacity]);
    }

    /**
     * Expands the array by double its current size if there is
     * no available space inside the array, or logically speaking,
     * if the end marker for the array exceeds its capacity.
     */
    private void expandIfNecessary() {
        if (rear >= array.length) {
            array = Arrays.copyOf(array, array.length * 2);
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
                for (int i = targetIndex; i < rear; i++) {
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

    @Override
    public void addToFront(T element) {
        expandIfNecessary();
        shiftItems("right", 0);
        array[0] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        expandIfNecessary();
        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int index = indexOf(target);
        if (index < 0) {
            throw new NoSuchElementException();
        }
        add(index + 1, element);
    }

    @Override
    public void add(int index, T element) {
        expandIfNecessary();
        if (index < 0 || index > rear) {
            throw new IndexOutOfBoundsException();
        }
        shiftItems("right", index);
        array[index] = element;
        rear++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (rear == 0) {
            throw new NoSuchElementException();
        }
        T firstElement = array[0];
        shiftItems("left", 0);
        array[rear - 1] = null;
        rear--;
        modCount++;
        return firstElement;
    }

    @Override
    public T removeLast() {
        if (rear == 0) {
            throw new NoSuchElementException();
        }
        T lastElement = array[rear - 1];
        array[rear - 1] = null;
        rear--;
        modCount++;
        return lastElement;
    }

    @Override
    public T remove(T element) {
        int index = indexOf(element);
        if (index < 0) {
            throw new NoSuchElementException();
        }
        T returnElement = array[index];
        shiftItems("left", index);
        array[rear - 1] = null;
        rear--;
        modCount++;
        return returnElement;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        T returnElement = array[index];
        shiftItems("left", index);
        array[rear - 1] = null;
        rear--;
        modCount++;
        return returnElement;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
        modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= rear) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public int indexOf(Object element) {
        int elementIndex = -1;
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
        if (rear == 0) {
            throw new NoSuchElementException();
        }
        return array[0];
    }

    @Override
    public T last() {
        if (rear == 0) {
            throw new NoSuchElementException();
        }
        return array[rear - 1];
    }

    @Override
    public boolean contains(Object target) {
        return (indexOf(target) != -1);
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
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        if (rear == 0) {
            builder.append("]");
            return builder.toString();
        }
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

		private int nextIndex, iterModCount;
		private boolean canRemove;

        /* Private-scope constructor */
        public ALIterator() {
            nextIndex = 0;
            iterModCount = modCount;
            canRemove = false;
        }

        private void fastfailCheck() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            fastfailCheck();
            return (array[nextIndex] != null);
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
            shiftItems("left", nextIndex - 1);
            array[rear - 1] = null;
            iterModCount++;
            modCount++;
            nextIndex--;
            rear--;
        }
        
    }

}