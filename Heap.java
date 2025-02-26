import java.util.ArrayList;

public class Heap {
    private ArrayList<Node> heap;

    // Constructor to initialize the heap
    public Heap() {
        heap = new ArrayList<>();
    }

    // Add an element to the priority queue
    public void add(Node value) {
        heap.add(value);
        heapifyUp(heap.size() - 1);
    }


    public Node poll() {
        if (heap.isEmpty()) {
            return null;
        }
        Node root = heap.get(0); // Save the root to return it later
        Node last = heap.remove(heap.size() - 1); // Remove the last element
        if (!heap.isEmpty()) {
            heap.set(0, last); // Replace the root with the last element
            heapifyDown(0); // Restore the heap property
        }
        return root;
    }

    // Peek the highest-priority element without removing it
    public Node peek() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Priority Queue is empty");
        }
        return heap.get(0);
    }

    // Check if the priority queue is empty
    public boolean isEmpty() {
        return heap.isEmpty();
    }


    // Maintain the heap property after adding an element
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex))>=0) {
                break;
            }
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    // Maintain the heap property after removing the root
    private void heapifyDown(int index) {
        int size = heap.size();
        while (true) {
            int smallest   = index;
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;

            if (leftChild < size && heap.get(leftChild).compareTo(heap.get(smallest))<0) {
                smallest  = leftChild;
            }
            if (rightChild < size && heap.get(rightChild).compareTo(heap.get(smallest))<   0){
                smallest  = rightChild;
            }
            if (smallest  == index) {
                break;
            }
            swap(index, smallest );
            index = smallest ;
        }
    }

    // swapping two elements of heap
    private void swap(int i, int j) {
        Node temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }


}
