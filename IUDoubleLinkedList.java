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

    /** Public constructor to create a new linked list */
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
        if (currentNode == tail) {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else {
            newNode.setNext(currentNode.getNext());
            currentNode.setNext(newNode);
            newNode.setPrev(currentNode);
            newNode.getNext().setPrev(newNode);
        }

        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<>(element);

        if (isEmpty()) { // Case: empty list
            head = tail = newNode;
        } else if (index == size) { // Case: adding after tail
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        } else if (index == 0) { // Case: adding at head
            head.setPrev(newNode);
            newNode.setNext(head);
            head = newNode;
        } else {
            Node<T> prevNode = head;
            for (int i = 0; i < index - 1; i++) {
                prevNode = prevNode.getNext();
            }
            newNode.setNext(prevNode.getNext());
            newNode.setPrev(prevNode);
            prevNode.setNext(newNode);
            newNode.getNext().setPrev(newNode);
        }

        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T element = head.getElement();
        if (size == 1) { // Early exit for one-element lists
            head = tail = null;
            return element;
        }
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
        if (size == 1) { // Early exit for one-element lists
            head = tail = null;
            return element;
        }
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
            head.setPrev(null);
        } else { // Case: more than one element and removing tail
            tail = currentNode.getPrev();
            tail.setNext(null);
        }

        size--;
        modCount++;
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
            head.setPrev(null);
        } else { // Case: more than one element and removing tail
            tail = currentNode.getPrev();
            tail.setNext(null);
        }

        size--;
        modCount++;
        return returnElement;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }
        currentNode.setElement(element);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }
        return currentNode.getElement();
    }

    @Override
    public int indexOf(T element) {
        int foundIndex = NOT_FOUND;
        int currentIndex = 0;
        Node<T> currentNode = head;
        while (currentNode != null && foundIndex == NOT_FOUND) {
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
    public String toString() {
        if (isEmpty()) { // No need to build a string for an empty list
            return "[ ]";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        Node<T> currentNode = head;
        while (currentNode != null) {
            builder.append(currentNode.getElement());
            builder.append(", ");
            currentNode = currentNode.getNext();
        }
        builder.delete(builder.length() - 2, builder.length() - 1); // Remove trailing comma
        builder.append("]");

        return builder.toString();
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
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }

            if (startingIndex == 0) {
                nextNode = head; // Quick conditional exit for head
            } else if (startingIndex == size - 1) {
                nextNode = tail; // Quick conditional exit for tail
            } else { // All other cases
                int currentIndex = 0;
                if (startingIndex < (size / 2)) {
                    nextNode = head;
                    currentIndex = 0;
                    while (currentIndex < startingIndex) {
                        nextNode = nextNode.getNext();
                        currentIndex++;
                    }
                } else {
                    nextNode = tail;
                    currentIndex = size - 1;
                    while (currentIndex > startingIndex) {
                        nextNode = nextNode.getPrev();
                        currentIndex--;
                    }
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
            

            size++;
            modCount++;
            iterModCount++;
        }

        @Override
        public boolean hasNext() {
            checkForConcurrency();
            return nextNode != null;
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

            lastReturnedNode = nextNode;
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
            if (lastReturnedNode == head && head == tail) { // Quick exit for one-element list
                head = tail = null;
                lastReturnedNode = null;
            }

            if (lastReturnedNode == tail) {
                tail = tail.getPrev();
                tail.setNext(null);
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
            modCount++;
            iterModCount++;
        }
        
    }
}
