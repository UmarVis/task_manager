import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import util.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, 25,
                LocalDateTime.of(2022, 9, 21, 13, 30));
        epic = new Epic(2, "Эпик 1", "Описание эпика 1", Status.NEW, 0,
                LocalDateTime.of(2000, 1, 1, 0, 0));
        subtask = new Subtask(3, "Подзадача 1 эпика 1", "Описание подзадачи 1",
                Status.DONE, 35, LocalDateTime.of(2022, 9, 22, 12, 0), 2);
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "Задача не добавлена");
    }

    @Test
    void getHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertTrue(history.isEmpty(), "История пустая");
    }

    @Test
    void addTwice() {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "Присутствует дублирование задач");
    }

    @Test
    void removeFirst() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(3, history.size(), "История не верна");

        historyManager.remove(task);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "Не верный размер истории");
        assertEquals(epic, history.get(0), "Задачи не соответсвуют");
        assertEquals(subtask, history.get(1), "Задачи не соответсвуют");
    }

    @Test
    void removeMiddle() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(3, history.size(), "История не верна");

        historyManager.remove(epic);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "Не верный размер истории");
        assertEquals(task, history.get(0), "Задачи не соответсвуют");
        assertEquals(subtask, history.get(1), "Задачи не соответсвуют");
    }

    @Test
    void removeLast() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(3, history.size(), "История не верна");

        historyManager.remove(subtask);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "Не верный размер истории");
        assertEquals(task, history.get(0), "Задачи не соответсвуют");
        assertEquals(epic, history.get(1), "Задачи не соответсвуют");
    }

    @Test
    void removeSingle() {
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "История не верна");

        historyManager.remove(task);
        history = historyManager.getHistory();
        assertEquals(history.isEmpty(), true, "История не пустая");
    }
}