package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Rating;
import net.thumbtack.school.notes.model.User;


public interface RatingDao {
    void insert(Rating rating, Note note) throws ServerException;
    Rating get(Note note, User author) throws ServerException;
}
