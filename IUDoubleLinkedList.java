import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A double-linked-node-based implementation of the
 * IndexedUnsortedList interface.
 * 
 * @param <T>
 * @author Amira Freeman
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{
    private final int NOT_FOUND = -1;
    private Node<T> head, tail;
    private int size, modCount;

    public IUDoubleLinkedList() {
        head = tail = null;
        size = modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null) { // List is empty
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> currentNode = head;
        while (currentNode != null && !currentNode.getElement().equals(target)) {
            currentNode = currentNode.getNext();
        }
        if (currentNode == null) { // Element was not found
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<>(element);
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {

        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T element = head.getElement();
        head = head.getNext();
        head.setPrev(null);
        size--;
        modCount++;
        return element;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T element = tail.getElement();
        tail = tail.getPrev();
        tail.setNext(null);
        size--;
        modCount++;
        return element;
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        int currentIndex = 0;
        Node<T> currentNode = head;
        while (currentIndex < size) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        currentNode.setElement(element);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        int currentIndex = 0;
        Node<T> currentNode = head;
        while (currentIndex < size) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        return currentNode.getElement();
    }

    @Override
    public int indexOf(T element) {
        int foundIndex = NOT_FOUND;
        int currentIndex = 0;
        Node<T> currentNode = head;
        while (foundIndex == NOT_FOUND && currentNode.getNext() != null) {
            if (currentNode.getElement().equals(element)) {
                foundIndex = currentIndex;
            } else {
                currentNode = currentNode.getNext();
                currentIndex++;
            }
        }
        return foundIndex;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        return (indexOf(target) != NOT_FOUND);
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
        return new DLLListIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLListIterator(startingIndex);
    }

    private class DLLListIterator implements ListIterator<T> {
        private int nextIndex, iterModCount;
        private Node<T> nextNode;

        /**
         * Initialize an iterator before the first element at head.
         */
        public DLLListIterator() {
            nextNode = head;
            nextIndex = 0;
            iterModCount = modCount;
        }

        /**
         * Initialize an iterator before a certain point in the list.
         * @param startingIndex
         */
        public DLLListIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }

            if (startingIndex < ((float)size / 2)) {
                
            }

            nextIndex = startingIndex;
            iterModCount = modCount;
        }

        public void checkForConcurrency() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(T arg0) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'add'");
        }

        @Override
        public boolean hasNext() {
            checkForConcurrency();
            throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
        }

        @Override
        public boolean hasPrevious() {
            checkForConcurrency();
            // The only node without a previous is head, this prevents NullPointerException if nextNode is null
            return nextNode != head;
        }

        @Override
        public T next() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'next'");
        }

        @Override
        public int nextIndex() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'nextIndex'");
        }

        @Override
        public T previous() {
            checkForConcurrency();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (nextNode != null) {
                nextNode = nextNode.getPrev();
            } else {
                nextNode = tail;
            }
            return nextNode.getElement();
        }

        @Override
        public int previousIndex() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'previousIndex'");
        }

        @Override
        public void remove() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'remove'");
        }

        @Override
        public void set(T arg0) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'set'");
        }
        
    }
}
