package cse.java2.project.repository;

import cse.java2.project.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Evan
 */
public interface QuestionRepository extends JpaRepository<Questions, Long> {
    @Query("SELECT count(*) FROM Questions where answer_count <= :max and answer_count >= :min")
    Long findByAnswer_countRange(@Param("min") int min, @Param("max") int max);
    @Query("SELECT count(*) FROM Questions where answer_count >= :min")
    Long findByAnswer_countRange(@Param("min") int min);
    @Query("SELECT count(*) FROM Questions")
    Long findTheQuestionsSize();
    @Query("SELECT max(answer_count) FROM Questions")
    Long findByAnswer_countMax();
    @Query("SELECT avg(answer_count) FROM Questions")
    double findByAnswer_countAverage();
    @Query("SELECT count(answer_count) FROM Questions where accepted_answer_id is not null ")
    Long findByHavingAcceptedAnswers();
    @Query("SELECT count(*) FROM Questions where TIMESTAMPDIFF(DAY, accepted_date, creation_date) < :max and TIMESTAMPDIFF(DAY, accepted_date, creation_date) >= :min")
    Long findByResolutionTimeRange(@Param("min") int min, @Param("max") int max);
    @Query("SELECT count(*) FROM Questions where TIMESTAMPDIFF(DAY, accepted_date, creation_date) >= :min")
    Long findByResolutionTimeRange(@Param("min") int min);


}
