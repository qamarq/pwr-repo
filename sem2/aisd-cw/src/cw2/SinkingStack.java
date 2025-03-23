package cw2;

public class SinkingStack<T> {
    private T[] stack;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public SinkingStack(int capacity) {
        this.capacity = capacity;
        this.stack = (T[]) new Object[capacity];
        this.size = 0;
    }

    public void push(T item) {
        if (size == capacity) {
            System.arraycopy(stack, 1, stack, 0, capacity - 1);
            stack[capacity - 1] = item;
        } else {
            stack[size++] = item;
        }
    }

    public T pop() {
        if (size == 0) {
            throw new IllegalStateException("Stos jest pusty");
        }
        return stack[--size];
    }

    public T peek() {
        if (size == 0) {
            throw new IllegalStateException("Stos jest pusty");
        }
        return stack[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        SinkingStack<Integer> stack = new SinkingStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.peek()); // 3
        stack.push(4); // 1 zostaje usunięte
        System.out.println(stack.pop()); // 4
        System.out.println(stack.pop()); // 3
        System.out.println(stack.pop()); // 2
    }
}
