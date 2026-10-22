import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size;

    public IUSingleLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.setNext(head);
        head = newNode;
        size++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<>(element);
        if (!isEmpty()) {
            tail.setNext(newNode);
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> targetNode = head;
        // Short-circuiting loop, logical order prevents NullPointerException
        while (targetNode != null && !targetNode.getElement().equals(target)) {
            targetNode = targetNode.getNext();
        }

        if (targetNode == null) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<>(element);
        newNode.setNext(targetNode.getNext());
        targetNode.setNext(newNode);
        newNode.setElement(element);

        if (targetNode == tail) {
            tail = newNode;
        }
    }

    @Override
    public void add(int index, T element) {
        if (index < size || index >= size) {
            throw new IndexOutOfBoundsException();
        }
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int indexOf(T element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T first() {
        return head.getElement();
    }

    @Override
    public T last() {
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
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

}