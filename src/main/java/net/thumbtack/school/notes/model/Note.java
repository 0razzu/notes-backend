package net.thumbtack.school.notes.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class Note {
    private int id;
    private String subject;
    private User author;
    private LocalDateTime created;
    private List<NoteRevision> noteRevisions;
    private List<Rating> ratings;
    private Section section;
    
    
    public Note() {
    }
    
    
    public Note(int id, String subject, User author, LocalDateTime created,
                List<NoteRevision> noteRevisions, List<Rating> ratings, Section section) {
        setId(id);
        setSubject(subject);
        setAuthor(author);
        setCreated(created);
        setRevisions(noteRevisions);
        setRatings(ratings);
        setSection(section);
    }
    
    
    public Note(String subject, User author, LocalDateTime created,
                List<NoteRevision> noteRevisions, List<Rating> ratings, Section section) {
        this(0, subject, author, created, noteRevisions, ratings, section);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    
    public void setRevisions(List<NoteRevision> noteRevisions) {
        this.noteRevisions = noteRevisions;
    }
    
    
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    
    
    public void setSection(Section section) {
        this.section = section;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public String getSubject() {
        return subject;
    }
    
    
    public User getAuthor() {
        return author;
    }
    
    
    public LocalDateTime getCreated() {
        return created;
    }
    
    
    public List<NoteRevision> getRevisions() {
        return noteRevisions;
    }
    
    
    public List<Rating> getRatings() {
        return ratings;
    }
    
    
    public Section getSection() {
        return section;
    }
    
    
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", author=" + author +
                ", created=" + created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")) +
                ", section=" + section +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return getId() == note.getId() &&
                Objects.equals(getSubject(), note.getSubject()) &&
                Objects.equals(getAuthor(), note.getAuthor()) &&
                Objects.equals(getCreated(), note.getCreated()) &&
                Objects.equals(getRevisions(), note.getRevisions()) &&
                Objects.equals(getRatings(), note.getRatings()) &&
                Objects.equals(getSection(), note.getSection());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSubject(), getAuthor(), getCreated(), getRevisions(), getRatings(), getSection());
    }
}
