package net.thumbtack.school.notes.model;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class Note {
    private Integer id;
    private String subject;
    private User author;
    private ZonedDateTime created;
    private List<Revision> revisions;
    private List<Rating> ratings;
    private Section section;
    
    
    public Note() {
    }
    
    
    public Note(Integer id, String subject, User author, ZonedDateTime created,
                List<Revision> revisions, List<Rating> ratings, Section section) {
        setId(id);
        setSubject(subject);
        setAuthor(author);
        setCreated(created);
        setRevisions(revisions);
        setRatings(ratings);
        setSection(section);
    }
    
    
    public Note(String subject, User author, ZonedDateTime created,
                List<Revision> revisions, List<Rating> ratings, Section section) {
        this(null, subject, author, created, revisions, ratings, section);
    }
    
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    
    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }
    
    
    public void setRevisions(List<Revision> revisions) {
        this.revisions = revisions;
    }
    
    
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    
    
    public void setSection(Section section) {
        this.section = section;
    }
    
    
    public Integer getId() {
        return id;
    }
    
    
    public String getSubject() {
        return subject;
    }
    
    
    public User getAuthor() {
        return author;
    }
    
    
    public ZonedDateTime getCreated() {
        return created;
    }
    
    
    public List<Revision> getRevisions() {
        return revisions;
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
        return Objects.equals(getId(), note.getId()) &&
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
