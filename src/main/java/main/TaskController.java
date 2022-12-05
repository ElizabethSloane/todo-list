package main;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
public class TaskController {


    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @RequestMapping("/")
    public String index(Model model) {
        Iterable<Task> taskIterable = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskIterable) {
            tasks.add(task);
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("tasksCount", tasks.size());
        return LocalDateTime.now().toString();
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> list() {
        Iterable<Task> taskIterable = taskRepository.findAll();
        return new ResponseEntity<>(taskIterable, HttpStatus.OK);
    }

    @PostMapping(value = "/tasks", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> add(@RequestBody Map<String, String> data) {
        Task newTask = new Task();
        newTask.setTitle(data.get("title"));
        newTask.setDescription(data.get("description"));
        newTask.setCreationTime(LocalDateTime.now());
        newTask.setDone(false);
        taskRepository.save(newTask);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (!optionalTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(optionalTask.get(), HttpStatus.OK);
    }

    @PatchMapping(value = "/tasks/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity<?> patch(@RequestBody Map<String, String> data,
                                                 @PathVariable int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Task task = optionalTask.get();

        if (!optionalTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (data.containsKey("isDone") && !(data.get("isDone").equals(task.isDone()))) {
            task.setDone(Boolean.parseBoolean(data.get("isDone")));
        }
        if (data.containsKey("title") && !data.get("isDone").equals(task.getTitle())) {
            task.setTitle(data.get("title"));
        }
        if (data.containsKey("description") && !data.get("description").equals(task.getDescription())) {
            task.setDescription(data.get("description"));
        }
        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (!optionalTask.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        taskRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
