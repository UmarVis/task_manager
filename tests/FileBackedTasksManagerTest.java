import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    void setUp() {
        file = new File("tests.csv");
        taskManager = new FileBackedTasksManager(file);
        taskManagerSetUp();
    }

    @Disabled
    @Test
    void save() {
    }

    @Test
    void loadFromFile() throws Exception {
        taskManager.getTask(1);
        taskManager.getEpic(epic.getId());
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        List<Task> taskList = taskManager.getAllTasks();
        assertNotNull(taskList);
        assertEquals(fileBackedTasksManager.getAllTasks().size(), taskList.size());
        assertEquals(fileBackedTasksManager.getAllTasks().get(0), taskList.get(0));
    }
}