package cw2;


//zad 1,2 lista 3
import java.util.Stack;

class VelosoTraversableStack<T> extends Stack<T> {
    private int cursor;

    public VelosoTraversableStack() {
        super();
        cursor = -1;
    }

    @Override
    public T push(T item) {
        T pushedItem = super.push(item);
        cursor = size() - 1;
        return pushedItem;
    }

    @Override
    public synchronized T pop() {
        T poppedItem = super.pop();
        cursor = size() - 1;
        return poppedItem;
    }

    public T peekCursor() {
        if (cursor < 0 || cursor >= size()) {
            throw new IllegalStateException("Kursor poza zakresem");
        }
        return get(cursor);
    }

    public void top() {
        cursor = size() - 1;
    }

    public boolean down() {
        if (cursor > 0) {
            cursor--;
            return true;
        } else {
            System.out.println("Osiągnięto dół stosu");
            return false;
        }
    }

    public void reverse() {
        Stack<T> tempStack1 = new Stack<>();
        Stack<T> tempStack2 = new Stack<>();

        while (!this.isEmpty()) {
            tempStack1.push(this.pop());
        }
        while (!tempStack1.isEmpty()) {
            tempStack2.push(tempStack1.pop());
        }
        while (!tempStack2.isEmpty()) {
            this.push(tempStack2.pop());
        }
    }

    public static void main(String[] args) {
        VelosoTraversableStack<Integer> vts = new VelosoTraversableStack<>();
        vts.push(10);
        vts.push(20);
        vts.push(30);

        System.out.println("Peek cursor: " + vts.peekCursor()); // 30
        vts.down();
        System.out.println("Peek cursor: " + vts.peekCursor()); // 20
        vts.down();
        System.out.println("Peek cursor: " + vts.peekCursor()); // 10
        vts.down(); // Osiągnięto dół stosu

        vts.reverse();
        System.out.println("Po odwróceniu:");
        while (!vts.isEmpty()) {
            System.out.println(vts.pop());
        }
    }
}
