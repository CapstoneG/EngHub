package ptit.com.enghub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.com.enghub.dto.request.CompleteLessonRequest;
import ptit.com.enghub.dto.request.LessonCreationRequest;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.entity.*;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.mapper.LessonMapper;
import ptit.com.enghub.repository.LessonRepository;
import ptit.com.enghub.repository.UnitRepository;
import ptit.com.enghub.repository.UserProgressRepository;
import ptit.com.enghub.service.IService.LessonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final UserProgressRepository userProgressRepository;
    private final LessonMapper lessonMapper;
    private final UnitRepository unitRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LessonResponse getLesson(Long lessonId, Long userId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        LessonResponse response = lessonMapper.toResponse(lesson);

        boolean completed = userProgressRepository
                .findByUserIdAndLessonId(userId, lessonId)
                .map(UserProgress::isCompleted)
                .orElse(false);

        response.setCompleted(completed);
        return response;
    }

    @Override
    public List<LessonResponse> getLessonsByUnitId(Long unitId) {
        return lessonRepository.findByUnit_Id(unitId).stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void completeLesson(Long lessonId, CompleteLessonRequest request, Long userId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        UserProgress progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElse(new UserProgress());

        progress.setUserId(userId);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setScore(request.getScore());
        progress.setLastUpdated(LocalDateTime.now());

        userProgressRepository.save(progress);

        // Mở khóa bài học tiếp theo
        unlockNextLesson(lesson, userId);
    }

    private boolean isLessonUnlocked(Lesson lesson, Long userId) {
        // Bài học đầu tiên của unit luôn mở khóa
        if (lesson.getOrderIndex() == 0) {
            return true;
        }

        // Kiểm tra bài học trước đó
        Lesson previousLesson = lessonRepository.findByUnit_Id(lesson.getUnit().getId()).stream()
                .filter(l -> l.getOrderIndex() == lesson.getOrderIndex() - 1)
                .findFirst()
                .orElse(null);

        if (previousLesson == null) {
            return true;
        }

        return userProgressRepository.findByUserIdAndLessonId(userId, previousLesson.getId())
                .map(UserProgress::isCompleted)
                .orElse(false);
    }

    private void unlockNextLesson(Lesson currentLesson, Long userId) {
        Lesson nextLesson = lessonRepository.findByUnit_Id(currentLesson.getUnit().getId()).stream()
                .filter(l -> l.getOrderIndex() == currentLesson.getOrderIndex() + 1)
                .findFirst()
                .orElse(null);

        if (nextLesson != null) {
            UserProgress nextProgress = userProgressRepository.findByUserIdAndLessonId(userId, nextLesson.getId())
                    .orElse(new UserProgress());
            nextProgress.setUserId(userId);
            nextProgress.setLesson(nextLesson);
            nextProgress.setCompleted(false);
            nextProgress.setScore(0);
            nextProgress.setLastUpdated(LocalDateTime.now());
            userProgressRepository.save(nextProgress);
        }
    }

    @Transactional
    public Lesson createLesson(LessonCreationRequest request) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setDuration(request.getDuration());
        lesson.setUnit(unit);

        if (request.getVideo() != null) {
            Video video = new Video();
            video.setUrl(request.getVideo().getUrl());
            video.setDescription(request.getVideo().getDescription());
            video.setDuration(request.getVideo().getDuration());
            video.setLesson(lesson);
            lesson.setVideo(video);
        }

        if (request.getVocabularies() != null) {
            List<Vocabulary> vocabList = request.getVocabularies().stream()
                    .map(v -> {
                        Vocabulary vo = new Vocabulary();
                        vo.setWord(v.getWord());
                        vo.setMeaning(v.getMeaning());
                        vo.setExample(v.getExample());
                        vo.setImageUrl(v.getImageUrl());
                        vo.setLesson(lesson);
                        return vo;
                    }).toList();
            lesson.setVocabularies(vocabList);
        }

        if (request.getDialogues() != null) {
            List<Dialogue> dList = request.getDialogues().stream()
                    .map(d -> {
                        Dialogue di = new Dialogue();
                        di.setSpeaker(d.getSpeaker());
                        di.setText(d.getText());
                        di.setLesson(lesson);
                        return di;
                    }).toList();
            lesson.setDialogue(dList);
        }

        if (request.getGrammar() != null) {
            Grammar grammar = new Grammar();
            grammar.setLesson(lesson);
            grammar.setTopic(request.getGrammar().getTopic());
            grammar.setExplanation(request.getGrammar().getExplanation());
            grammar.setSignalWord(request.getGrammar().getSignalWord());

            List<GrammarFormula> formulas = request.getGrammar().getFormulas()
                    .stream().map(f -> {
                        GrammarFormula gf = new GrammarFormula();
                        gf.setGrammar(grammar);
                        gf.setType(f.getType());
                        gf.setFormula(f.getFormula());
                        gf.setDescription(f.getDescription());
                        gf.setVerbType(f.getVerbType());

                        List<GrammarExample> examples = f.getExamples().stream()
                                .map(e -> {
                                    GrammarExample ex = new GrammarExample();
                                    ex.setFormula(gf);
                                    ex.setSentence(e.getSentence());
                                    ex.setTranslation(e.getTranslation());
                                    ex.setHighlight(e.getHighlight());
                                    return ex;
                                }).toList();

                        gf.setExamples(examples);
                        return gf;
                    }).toList();

            grammar.setFormulas(formulas);
            lesson.setGrammar(grammar);
        }

        if (request.getExercises() != null) {
            List<Exercise> exList = request.getExercises().stream()
                    .map(e -> {
                        Exercise ex = new Exercise();
                        ex.setQuestion(e.getQuestion());
                        ex.setType(e.getType());
                        ex.setLesson(lesson);


                        try {
                            JsonNode metadataNode = e.getMetadata();
                            if (metadataNode != null && !metadataNode.isNull()) {
                                ex.setMetadata(objectMapper.writeValueAsString(metadataNode));
                            } else {
                                ex.setMetadata(null);
                            }
                        } catch (Exception ex1) {
                            ex.setMetadata(null);
                        }


                        return ex;
                    }).toList();
            lesson.setExercises(exList);
        }

        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Long id) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

}
