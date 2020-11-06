package net.thumbtack.school.notes.database.provider;


import net.thumbtack.school.notes.model.UserType;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;


public class UserProvider {
    public String getAllByRelationToUser(Map<String, Object> params) {
        return new SQL() {
            {
                int userId = ((User) params.get("user")).getId();
                String relation = (String) params.get("relation");
                String sortByRating = (String) params.get("sortByRating");
                Integer from = (Integer) params.get("from");
                Integer count = (Integer) params.get("count");
                
                SELECT("user.id AS id, login, password");
                SELECT("first_name, patronymic, last_name, type, avg(rating.value) AS rating");
                SELECT("(session.token IS NOT NULL) AS online");
                
                if (relation.equals("following")) {
                    FROM("user_followed");
                    JOIN("user ON user.id = user_followed.followed_id");
                }
                else if (relation.equals("followers")) {
                    FROM("user_followed");
                    JOIN("user ON user.id = user_followed.user_id");
                }
                else if (relation.equals("ignore")) {
                    FROM("user_ignored");
                    JOIN("user ON user.id = user_ignored.ignored_id");
                }
                else {
                    FROM("user_ignored");
                    JOIN("user ON user.id = user_ignored.user_id");
                }
                
                LEFT_OUTER_JOIN("note ON user.id = note.author_id");
                LEFT_OUTER_JOIN("rating ON note.id = rating.note_id");
                LEFT_OUTER_JOIN("session ON user.id = session.user_id");
                
                if (relation.equals("following"))
                    WHERE("user_followed.user_id = " + userId);
                else if (relation.equals("followers"))
                    WHERE("user_followed.followed_id = " + userId);
                else if (relation.equals("ignore"))
                    WHERE("user_ignored.user_id = " + userId);
                else
                    WHERE("user_ignored.ignored_id = " + userId);
                
                GROUP_BY("user.id");
                
                if ("asc".equals(sortByRating))
                    ORDER_BY("rating ASC");
                else if ("desc".equals(sortByRating))
                    ORDER_BY("rating DESC");
                
                LIMIT(count == null? Integer.MAX_VALUE : count);
                if (from != null)
                    OFFSET(from);
            }
        }.toString();
    }
}
