import java.util.Iterator;
import java.util.NoSuchElementException;

class NodeList<T> {
    T data;
    NodeList<T> next;

    NodeList(T data) {
        this.data = data;
        this.next = null;
    }
}

public class MyList<T> implements Iterable<T> {
    NodeList<T> head;
    NodeList<T> tail;

    public MyList() {
        this.head = null;
        this.tail = null;
    }

    public void add(T item) {
        NodeList<T> newNode = new NodeList<>(item);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    @Override
    public Iterator<T> iterator() {
        return new SimpleListIterator();
    }

    private class SimpleListIterator implements Iterator<T> {
        private NodeList<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}
