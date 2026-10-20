import java.util.Iterator;
import java.util.ListIterator;

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

    @Override
    public void addToFront(T element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addToRear(T element) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T removeLast() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T first() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T last() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(Object target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int size() {
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

    // private class ListIterator<T> implements Iterator {

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