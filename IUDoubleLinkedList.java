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
    private static final int NOT_FOUND = -1;
    private Node<T> head, tail;
    private int size, modCount;

    /** Public constructor to create a new linked list */
    public IUDoubleLinkedList() {
        head = tail = null;
        size = modCount = 0;
    }

    /**
     * Many public methods require a non-empty list to properly
     * conduct their function. If a method finds that a list
     * is empty when it requires a non-empty list, it will throw
     * a NoSuchElementException.
     * 
     * @throws NoSuchElementException when the list is empty
     */
    public void checkForEmptyList() {
        if (isEmpty()) { throw new NoSuchElementException(); }
    }

    /**
     * Many public methods require an index parameter to properly
     * conduct their function. If a method finds that the given
     * index is outside of the list's bounds, as defined between
     * 0 and size - 1, then it will throw an IndexOutOfBoundsException.
     * 
     * Note that this method is for checking a given index if it is
     * less than zero, or greater than OR EQUAL TO size. The bound
     * check for the add(index, element) method therefore cannot use
     * this check in case a user is adding an element after the tail
     * element, which this would specifically prevent.
     * 
     * @param index to ensure is not less than zero, or greater than
     *      or equal to size
     * @throws IndexOutOfBoundsException if the given index is not valid
     *      because it is not usable
     */
    public void checkForValidIndexBounds(int index) {
        if (index < 0 || index >= size) { throw new IndexOutOfBoundsException(); }
    }

    /**
     * Some methods are required to find the index of an element,
     * but if the element is not found using the indexOf() method,
     * then it will return -1, or NOT_FOUND as represented in this
     * implementation of IndexedUnsortedList. If that is the case,
     * then the method which called indexOf() must detect this and
     * throw a NoSuchElementException to indicate this error.
     * 
     * @param element of which to find the index in the list
     * @throws NoSuchElementException if the element is not found
     *      (indexOf returns NOT_FOUND == -1)
     * @return index number in list if the element is found (indexOf
     *      returns any value other than NOT_FOUND)
     */
    public int checkForValidFoundIndex(T element) {
        int index = indexOf(element);
        if (index == NOT_FOUND) { throw new NoSuchElementException(); }
        return index;
    }

    @Override
    public void addToFront(T element) {
        add(0, element);
    }

    @Override
    public void addToRear(T element) {
        add(size, element);
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int index = checkForValidFoundIndex(target);
        DLLIterator list = new DLLIterator(index);
        list.next();
        list.add(element);
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) { throw new IndexOutOfBoundsException(); }
        DLLIterator list = new DLLIterator(index);
        list.add(element);
    }

    @Override
    public T removeFirst() {
        checkForEmptyList();
        return remove(0);
    }

    @Override
    public T removeLast() {
        checkForEmptyList();
        return remove(size - 1);
    }

    @Override
    public T remove(T element) {
        checkForEmptyList();
        int index = checkForValidFoundIndex(element);
        return remove(index);
    }

    @Override
    public T remove(int index) {
        checkForValidIndexBounds(index);
        DLLIterator list = new DLLIterator(index);
        T element = list.next();
        list.remove();
        return element;
    }

    @Override
    public void set(int index, T element) {
        checkForValidIndexBounds(index);
        DLLIterator list = new DLLIterator(index);
        list.next();
        list.set(element);
    }

    @Override
    public T get(int index) {
        checkForValidIndexBounds(index);
        DLLIterator list = new DLLIterator(index);
        return list.next();
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

    /**
     * A class for creating a ListIterator specially designed to
     * handle double-linked lists.
     * 
     * @author Amira Freeman
     */
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
         * @param startingIndex by which to begin iterating
         */
        public DLLIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size) {
                throw new IndexOutOfBoundsException();
            }

            if (startingIndex == 0) {
                nextNode = head; // Quick conditional exit for head
            } else if (startingIndex == size - 1) {
                nextNode = tail; // Quick conditional exit for tail
            } else if (startingIndex == size) {
                nextNode = null; // Quick conditional exit for after tail
            } else { // All other cases
                int currentIndex;
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
            if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
        }

        /**
         * Before adding a node or setting a node's element, the respective
         * method needs to ensure that there was a last returned node, usually
         * done by the navigation methods next() and previous(). If such node
         * does not exist, or it is null, the iterator must throw an
         * IllegalStateException and destroy itself.
         * 
         * @throws IllegalStateException if a navigation call was not done and
         *      therefore did not return a node
         */
        public void checkForValidReturnedNode() {
            if (lastReturnedNode == null) { throw new IllegalStateException(); }
        }

        @Override
        public void add(T arg0) {
            checkForConcurrency();
            Node<T> newNode = new Node<>(arg0);

            if (isEmpty()) { // Case: empty list
                head = tail = newNode;
                nextNode = newNode;
            } else if (nextNode == null) { // Case: adding after tail
                tail.setNext(newNode);
                newNode.setPrev(tail);
                tail = newNode;
                nextNode = newNode;
            } else if (nextNode == head) { // Case: adding at head
                head.setPrev(newNode);
                newNode.setNext(head);
                head = newNode;
                nextNode = newNode;
            } else { // All other cases
                Node<T> prev = nextNode.getPrev(); // For readability of calls
                newNode.setPrev(prev);
                newNode.setNext(nextNode);
                prev.setNext(newNode);
                nextNode.setPrev(newNode);
            }

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
            if (!hasNext()) { throw new NoSuchElementException(); }
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
            if (!hasPrevious()) { throw new NoSuchElementException(); }

            if (nextNode != null) { nextNode = nextNode.getPrev(); }
            else { nextNode = tail; }

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
            checkForValidReturnedNode();

            // Update all references
            if (lastReturnedNode == head && head == tail) { // Quick exit for one-element list
                head = tail = null;
                lastReturnedNode = null;
            } else { // Necessary else statement? ListIterator fails some tests otherwise
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

                if (lastReturnedNode == nextNode) { nextNode = nextNode.getNext(); }
            }

            size--;
            modCount++;
            iterModCount++;
            lastReturnedNode = null;
        }

        @Override
        public void set(T arg0) {
            checkForConcurrency();
            checkForValidReturnedNode();
            
            lastReturnedNode.setElement(arg0);
            modCount++;
            iterModCount++;
        }
        
    }
}
