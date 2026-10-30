import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A single-linked-node-based implementation of the
 * IndexedUnsortedList interface. Does not support
 * ListIterator.
 * @param <T>
 * @author Amira Freeman
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size, modCount;
    private final int NOT_FOUND = -1;

    /**
     * Public constructor for creating a single-linked list.
     */
    public IUSingleLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    /**
     * Many of the public methods require the list to have
     * at least one element. If the list is empty, the
     * method cannot continue, and throws a NoSuchElementException.
     * 
     * @throws NoSuchElementException if the list is empty
     */
    private void checkForEmptyList() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Many of the public methods require their given parameters
     * to fit a certain range, and when given an index, if the
     * index is less than zero or is greater than or equal to
     * the numerical size of the list, then the method cannot
     * continue and throws an IndexOutOfBoundsException.
     * 
     * @param index to ensure is within the bounds of the list
     * @throws IndexOutOfBoundsException if the given index is not usable
     */
    private void checkGivenBounds(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.setNext(head);
        if (head == null) { // Special case: empty list
            tail = newNode;
        }
        head = newNode;
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<>(element);
        if (!isEmpty() && tail != null) {
            tail.setNext(newNode);
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
        modCount++;
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

        // Case: target not found
        if (targetNode == null) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<>(element);
        newNode.setNext(targetNode.getNext());
        targetNode.setNext(newNode);
        newNode.setElement(element);

        // Special case: adding after tail
        if (targetNode == tail) {
            tail = newNode;
        }

        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        // This method is NOT suitable for using checkGivenBounds()
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<>(element);
        // Case: adding after an existent tail
        if (index == size && tail != null) {
            tail.setNext(newNode);
            tail = newNode;
        } else { // All other cases
            int currentIndex = 0;
            Node<T> prevNode = head;
            // Case: adding at head
            if (index == 0) {
                newNode.setNext(head);
                head = newNode;
                if (size == 0) { // Special case: adding to an empty list
                    tail = newNode;
                }
            } else {
                while (currentIndex < index - 1) {
                    prevNode = prevNode.getNext();
                    currentIndex++;
                }
            }
        }

        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        checkForEmptyList();
        T returnElement = head.getElement();
        head = head.getNext();
        size--;
        modCount++;
        return returnElement;
    }

    @Override
    public T removeLast() {
        checkForEmptyList();

        T returnElement = tail.getElement();
        if (size > 2) {
            Node<T> prevPrevNode = head;
            while (prevPrevNode.getNext().getNext() != tail) {
                prevPrevNode = prevPrevNode.getNext();
            }
            tail = prevPrevNode.getNext();
            prevPrevNode.getNext().setNext(null);
        } else if (size == 2) {
            tail = head;
            head.setNext(null);
        } else {
            head = null;
            tail = null;
        }

        size--;
        modCount++;
        return returnElement;
    }

    @Override
    public T remove(T element) {
        checkForEmptyList();
        
        T returnElement;
        if (head.getElement().equals(element)) {
            returnElement = head.getElement();
            head = head.getNext();

            // Special case: one element list
            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> prevNode = head;
            while (prevNode.getNext() != null && !prevNode.getNext().getElement().equals(element)) {
                prevNode = prevNode.getNext();
            }

            // Case: element not found
            if (prevNode.getNext() == null) {
                throw new NoSuchElementException();
            }

            returnElement = prevNode.getNext().getElement();
            prevNode.setNext(prevNode.getNext().getNext());

            // Special case: tail was removed
            if (prevNode.getNext() == null) {
                tail = prevNode;
            }
        }

        size--;
        modCount++;
        return returnElement;
    }

    @Override
    public T remove(int index) {
        checkGivenBounds(index);

        T returnElement;

        // Case: removing head
        if (index == 0) {
            returnElement = head.getElement();
            head = head.getNext();
            if (head == null) { // List is now empty
                tail = null;
            }
            size--;
            modCount++;
            return returnElement;
        }

        Node<T> prevNode = head;
        for (int i = 0; i < index - 1; i++) {
            prevNode = prevNode.getNext();
        }
        Node<T> currentNode = prevNode.getNext();
        returnElement = currentNode.getElement();

        prevNode.setNext(currentNode.getNext());

        // Case: removing tail
        if (currentNode == tail) {
            tail = prevNode;
        }

        size--;
        modCount++;
        return returnElement;
    }

    @Override
    public void set(int index, T element) {
        checkGivenBounds(index);

        int currentIndex = 0;
        Node<T> currentNode = head;
        while (currentIndex < index) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }

        modCount++;
        currentNode.setElement(element);
    }

    @Override
    public T get(int index) {
        checkGivenBounds(index);

        int currentIndex = 0;
        Node<T> currentNode = head;
        while (currentIndex < index) {
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
        while (currentIndex < size && foundIndex == NOT_FOUND && currentNode != null) {
            if (currentNode.getElement().equals(element)) {
                foundIndex = currentIndex;
            } else {
                currentNode = currentNode.getNext();
            }
            currentIndex++;
        }
        return foundIndex;
    }

    @Override
    public T first() {
        checkForEmptyList();
        return head.getElement();
    }

    @Override
    public T last() {
        checkForEmptyList();
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
        return new SLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        if (isEmpty()) { // No need to build a string if the list is empty
            return "[ ]";
        }

        StringBuilder builder = new StringBuilder("[ ");
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

    private class SLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private boolean canRemove;

        public SLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            canRemove = false;
        }

        private void concurrencyCheck() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            concurrencyCheck();
            return (nextNode != null);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T returnElement = nextNode.getElement();
            nextNode = nextNode.getNext();
            canRemove = true;
            return returnElement;
        }

        @Override
        public void remove() {
            concurrencyCheck();
            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;

            // Special case: head was removed
            if (head.getNext() == nextNode) {
                head = head.getNext();

                // Special-er case: only one element
                if (head == null) {
                    tail = null;
                }
            } else {
                Node<T> prevPrevNode = head;
                while (prevPrevNode.getNext().getNext() != nextNode) {
                    prevPrevNode = prevPrevNode.getNext();
                }
                prevPrevNode.setNext(nextNode);

                // Special case: tail was removed
                if (nextNode == null) {
                    tail = prevPrevNode;
                }
            }

            size--;
            modCount++;
            iterModCount++;
        }
    }
}