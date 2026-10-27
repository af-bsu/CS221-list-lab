import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * IUSingleLinkedList
 * @param <T>
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size, modCount;
    private final int NOT_FOUND = -1;

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

        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T removeFirst() {
        T returnElement = head.getElement();
        head = head.getNext();
        return returnElement;
    }

    @Override
    public T removeLast() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        
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

        return returnElement;
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
        int index = 0;
        Node<T> currentNode = head;
        while (index < size && currentNode != element) {
            currentNode = currentNode.getNext();
        }
        return index;
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