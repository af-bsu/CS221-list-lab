import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private static int DEFAULT_CAPACITY = 10;
    private int rear, modCount;
    private T[] array;

    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public IUArrayList(int initialCapacity) {
        rear = 0;
        modCount = 0;
        array = (T[])(new Object[initialCapacity]);
    }

    private void expandIfNecessary() {
        if (rear >= array.length) {
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    private void shiftItems(String direction, int startingIndex) {
        switch (direction) {
            case "left" -> {
                for (int i = startingIndex; i < rear; i++) {
                    array[i] = array[i + 1];
                }
            }
            case "right" -> {
                for (int i = rear; i > startingIndex; i--) {
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addAfter(T element, T target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // private class ALIterator<T> implements Iterator {

    //     @Override
    //     public boolean hasNext() {
    //         throw new UnsupportedOperationException("Not supported yet.");
    //     }

    //     @Override
    //     public T next() {
    //         throw new UnsupportedOperationException("Not supported yet.");
    //     }
        
    // }

}