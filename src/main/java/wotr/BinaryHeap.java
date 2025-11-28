package wotr;

import java.util.Comparator;

/* renamed from: BinaryHeap */
public class BinaryHeap {
    private static final int DEFAULT_MAX_SIZE = 1000;
    private int MAX_SIZE;
    private int backIndex;
    private Comparator<Object> cmp;
    private Object[] heap;

    /* renamed from: BinaryHeap$DefaultComparator */
    private static class DefaultComparator implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    }

    public BinaryHeap() {
        this(new DefaultComparator());
    }

    public BinaryHeap(Comparator<Object> cmp2) {
        this.MAX_SIZE = DEFAULT_MAX_SIZE;
        this.heap = new Object[this.MAX_SIZE];
        this.cmp = cmp2;
        this.backIndex = 0;
    }

    public boolean isEmpty() {
        return this.backIndex == 0;
    }

    public Object findMin() {
        if (!isEmpty()) {
            return this.heap[1];
        }
        return null;
    }

    public Object deleteMin() {
        if (isEmpty()) {
            return null;
        }
        Object temp = this.heap[1];
        if (this.backIndex <= 0) {
            return temp;
        }
        this.heap[1] = this.heap[this.backIndex];
        this.backIndex--;
        percolateDown(1);
        return temp;
    }

    public void insert(Object o) {
        if (this.backIndex == this.heap.length - 1) {
            resizeHeapArray();
        }
        this.backIndex++;
        this.heap[this.backIndex] = o;
        if (this.backIndex > 1) {
            int t = this.backIndex;
            while (t > 1 && this.cmp.compare(o, this.heap[t / 2]) < 0) {
                this.heap[t] = this.heap[t / 2];
                t /= 2;
            }
            this.heap[t] = o;
        }
    }

    public void makeEmpty() {
        this.MAX_SIZE = DEFAULT_MAX_SIZE;
        this.heap = new Object[this.MAX_SIZE];
    }

    private void resizeHeapArray() {
        this.MAX_SIZE = (this.MAX_SIZE * 2) + 1;
        Object[] temp = new Object[this.MAX_SIZE];
        for (int i = 1; i < this.heap.length; i++) {
            temp[i] = this.heap[i];
        }
        this.heap = temp;
    }

    private void percolateDown(int currentIndex) {
        Object temp = this.heap[currentIndex];
        int i = currentIndex;
        while (i * 2 <= this.backIndex) {
            int child = i * 2;
            if (child != this.backIndex && this.cmp.compare(this.heap[child + 1], this.heap[child]) < 0) {
                child++;
            }
            if (this.cmp.compare(this.heap[child], temp) >= 0) {
                break;
            }
            this.heap[i] = this.heap[child];
            i = child;
        }
        this.heap[i] = temp;
    }
}
