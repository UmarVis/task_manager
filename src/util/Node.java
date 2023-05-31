package util;

public class Node<E> {
    public Node<E> prev;
    public E item;
    public Node<E> next;


    public Node(Node<E> prev, E item, Node<E> next) {
        this.item = item;
        this.next = next;
        this.prev = prev;
    }
}
