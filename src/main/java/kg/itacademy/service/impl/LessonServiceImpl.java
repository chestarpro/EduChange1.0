package kg.itacademy.service.impl;

import kg.itacademy.entity.Lesson;
import kg.itacademy.repository.LessonRepository;
import kg.itacademy.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public Lesson create(Lesson lesson) {
        if (lesson.getLessonInfo() == null)
            throw new IllegalArgumentException("Нет описания урока");
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.getById(id);
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson update(Lesson lesson) {
        if (lesson.getId() == null)
            throw new IllegalArgumentException("Не указан id урока");
        return create(lesson);
    }

    @Override
    public Lesson deleteLessonById(Long id) {
        Lesson lesson = getById(id);
        lessonRepository.delete(lesson);
        return lesson;
    }
}