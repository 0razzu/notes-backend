package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Rating;


public interface RatingDao {
    void insert(Rating rating, Note note) throws ServerException;
}
