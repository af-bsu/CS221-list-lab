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

        // TODO Add all references
        Node<T> newNode = new Node<>(element);
        if (currentNode != head && currentNode != tail) {
            newNode.setNext(currentNode.getNext());
            currentNode.setNext(newNode);
            newNode.setPrev(currentNode);
            newNode.getNext().setPrev(newNode);
        } else if (head == tail) {

        }
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
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

        Node<T> currentNode = head;
        while (currentNode != null && !currentNode.getElement().equals(element)) {
            currentNode = currentNode.getNext();
        }
        if (currentNode == null) {
            throw new NoSuchElementException();
        }

        T returnElement = currentNode.getElement();

        // Remove all references pointing to currentNode so it will be trashed by Java's garbage collector
        if (currentNode != head && currentNode != tail) { // Case: at least three elements and not removing head or tail
            currentNode.getPrev().setNext(currentNode.getNext());
            currentNode.getNext().setPrev(currentNode.getPrev());
        } else if (head == tail) { // Case: only one element
            head = tail = null;
        } else if (currentNode == head) { // Case: more than one element and removing head
            head = currentNode.getNext();
            currentNode.getNext().setPrev(null);
        } else { // Case: more than one element and removing tail
            tail = currentNode.getPrev();
            currentNode.getPrev().setNext(null);
        }

        return returnElement;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }

        T returnElement = currentNode.getElement();
        // Remove all references pointing to currentNode so it will be trashed by Java's garbage collector
        if (currentNode != head && currentNode != tail) { // Case: at least three elements and not removing head or tail
            currentNode.getPrev().setNext(currentNode.getNext());
            currentNode.getNext().setPrev(currentNode.getPrev());
        } else if (head == tail) { // Case: only one element
            head = tail = null;
        } else if (currentNode == head) { // Case: more than one element and removing head
            head = currentNode.getNext();
            currentNode.getNext().setPrev(null);
        } else { // Case: more than one element and removing tail
            tail = currentNode.getPrev();
            currentNode.getPrev().setNext(null);
        }

        return returnElement;
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
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);
    }

    private class DLLIterator implements ListIterator<T> {
        private int nextIndex, iterModCount;
        private Node<T> nextNode, lastReturnedNode;

        /**
         * Initialize an iterator before the first element at head.
         */
        public DLLIterator() {
            this(0);
        }

        /**
         * Initialize an iterator before a certain point in the list.
         * @param startingIndex
         */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex >= size) {
                throw new IndexOutOfBoundsException();
            }

            // TODO Fast nav to startingNode
            Node<T> startingNode;
            if (startingIndex == 0) {
                nextNode = head;
            } else {
                if (startingIndex <= ((float)size / 2)) {
                    startingNode = head;
                    
                }
            }

            nextIndex = startingIndex;
            iterModCount = modCount;
        }

        /**
         * Before any method should execute its intended program, it
         * must first check that the list that it is reporting is still
         * accurately reflected to the list the iterator promised to
         * accurately report on. If it is not, then the iterator
         * destroys itself and throws a ConcurrentModificationException.
         * 
         * @throws ConcurrentModificationException if the list is no
         *      longer accurate and was modified without the
         *      iterator's knowledge
         */
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
            // The only node without a next is tail, this prevents NullPointerException if nextNode is null
            return nextNode != tail;
        }

        @Override
        public boolean hasPrevious() {
            checkForConcurrency();
            // The only node without a previous is head, this prevents NullPointerException if nextNode is null
            return nextNode != head;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T returnElement = nextNode.getElement();
            lastReturnedNode = nextNode;
            nextNode = nextNode.getNext();
            return returnElement;
        }

        @Override
        public int nextIndex() {
            checkForConcurrency();
            return nextIndex;
        }

        @Override
        public T previous() {
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
            checkForConcurrency();
            return (nextIndex - 1);
        }

        @Override
        public void remove() {
            checkForConcurrency();
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }

            // Update all references
            if (lastReturnedNode == tail) {
                tail = tail.getPrev();
            } else {
                lastReturnedNode.getNext().setPrev(lastReturnedNode.getPrev());
            }
            if (lastReturnedNode == head) {
                head = head.getNext();
                head.setPrev(null);
            } else {
                lastReturnedNode.getPrev().setNext(lastReturnedNode.getNext());
            }

            if (lastReturnedNode == nextNode) {
                nextNode = nextNode.getNext();
            }

            size--;
            modCount++;
            iterModCount++;
            lastReturnedNode = null;
        }

        @Override
        public void set(T arg0) {
            checkForConcurrency();
            if (lastReturnedNode == null) {
                throw new IllegalStateException();
            }
            lastReturnedNode.setElement(arg0);
        }
        
    }
}
