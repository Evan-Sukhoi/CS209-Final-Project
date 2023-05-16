package cse.java2.project.repository;

import cse.java2.project.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Evan
 */
public interface Repositories extends JpaRepository<Questions, Long> {

  @Query("SELECT count(*) FROM Questions where answer_count <= :max and answer_count >= :min")
  Long findByAnswer_countRange(@Param("min") int min, @Param("max") int max);

  @Query("SELECT answer_count FROM Questions")
  List<Integer> findAllAnswer_count();

  @Query("SELECT count(*) FROM Questions where answer_count >= :min")
  Long findByAnswer_countRange(@Param("min") int min);

  @Query("SELECT count(*) FROM Questions")
  Long findTheQuestionsSize();

  @Query("SELECT max(answer_count) FROM Questions")
  Long findByAnswer_countMax();

  @Query("SELECT avg(answer_count) FROM Questions")
  double findByAnswer_countAverage();

  @Query("SELECT count(answer_count) FROM Questions where accepted_answer_id is not null")
  Long findByHavingAcceptedAnswers();

  @Query("SELECT count(*) FROM Questions where TIMESTAMPDIFF(HOUR, creation_date , accepted_date) < :max and TIMESTAMPDIFF(HOUR, creation_date , accepted_date) >= :min")
  Long findByResolutionTimeRange(@Param("min") int min, @Param("max") int max);

  @Query("SELECT count(*) FROM Questions where TIMESTAMPDIFF(HOUR, creation_date , accepted_date) >= :min")
  Long findByResolutionTimeRange(@Param("min") int min);

  @Query("SELECT count(*) FROM Questions where not_public_will = 1")
  Long findByNot_public_will();

  @Query("SELECT t FROM TagsJavaRelated t ORDER BY t.count DESC ")
  List<TagsJavaRelated> findByCount();

  @Query("SELECT t FROM Tags t ORDER BY t.score DESC ")
  List<Tags> findByUpvotes();

  @Query("SELECT t FROM Tags t ORDER BY t.view_count DESC ")
  List<Tags> findByViews();

  @Query("SELECT count(*) FROM Questions where user_count <= :max and user_count >= :min")
  Long findByQuestionUsersCount(@Param("min") int min, @Param("max") int max);

  @Query("SELECT count(*) FROM Questions where user_count >= :min")
  Long findByQuestionUsersCount(@Param("min") int min);

  @Query("SELECT count(*) FROM Questions where answer_user_count <= :max and answer_user_count >= :min")
  Long findByQuestionAnswerUsersCount(@Param("min") int min, @Param("max") int max);

  @Query("SELECT count(*) FROM Questions where answer_user_count >= :min")
  Long findByQuestionAnswerUsersCount(@Param("min") int min);

  @Query("SELECT count(*) FROM Questions where comment_user_count <= :max and comment_user_count >= :min")
  Long findByQuestionCommentUsersCount(@Param("min") int min, @Param("max") int max);

  @Query("SELECT count(*) FROM Questions where comment_user_count >= :min")
  Long findByQuestionCommentUsersCount(@Param("min") int min);

  @Query("SELECT t FROM HotApi t ORDER BY t.count DESC ")
  List<HotApi> findByApiCount();

  @Query("SELECT t FROM Users t ORDER BY t.count DESC ")
  List<Users> findByUsersCount();

  /**
   * The following queries are for RESTful API
   */

  // query all answers
  @Query("SELECT t FROM Answers t")
  List<Answers> findAllAnswers();

  // query answers through question_id
  @Query("SELECT t FROM Answers t where t.question_id = :question_id")
  List<Answers> findAnswersByQuestion_id(@Param("question_id") Integer question_id);

  // query all accepted answers
  @Query("SELECT t FROM Answers t WHERE t.answer_id IN (SELECT q.accepted_answer_id FROM Questions q)")
  List<Answers> findAllAcceptedAnswers();

  // query accepted answers through question_id
  @Query("SELECT t FROM Answers t WHERE t.answer_id = (SELECT q.accepted_answer_id FROM Questions q WHERE q.question_id = :question_id)")
  List<Answers> findAcceptedAnswersByQuestionId(@Param("question_id") Integer question_id);

  // query all comments
  @Query("SELECT t FROM Comments t")
  List<Comments> findAllComments();

  // query comments through question_id
  @Query("SELECT t FROM Comments t where t.question_id = :question_id")
  List<Comments> findCommentsByQuestion_id(@Param("question_id") Integer question_id);

  // query all questions
  @Query("SELECT t FROM Questions t")
  List<Questions> findAllQuestions();

  // query questions through question_id
  @Query("SELECT t FROM Questions t where t.question_id = :question_id")
  List<Questions> findQuestionsByQuestion_id(@Param("question_id") Integer question_id);

  // query all tagsJavaRelated
  @Query("SELECT t FROM TagsJavaRelated t")
  List<TagsJavaRelated> findAllTagsJavaRelated();

}