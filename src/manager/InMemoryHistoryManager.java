package manager;

import tasks.Task;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public Map<Integer, Node<Task>> historyMap = new HashMap<>();
    public Node<Task> head;
    public Node<Task> tail;

    public void linkLast(Task task) {
        Node newNode = new Node(null, task, null);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void add(Task task) {
        remove(task);
        if (task != null) {
            linkLast(task);
        }
    }

    public void removeNode(Node<Task> node) {
        if (node.prev == null && node.next == null) {
            head = null;
            tail = null;
            return;
        }
        if (node == head) {
            head = head.next;
            head.prev = null;
            return;
        }
        if (node == tail) {
            tail = tail.prev;
            tail.next = null;
            return;
        }
        final Node<Task> prev = node.prev;
        final Node<Task> next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    @Override
    public List<Task> getHistory() {
        final List<Task> list = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            list.add(node.item);
            node = node.next;
        }
        return list;
    }

    @Override
    public void remove(Task task) {
        final Node<Task> node = historyMap.remove(task.getId());
        if (node != null) {
            removeNode(node);
        }
    }
}
