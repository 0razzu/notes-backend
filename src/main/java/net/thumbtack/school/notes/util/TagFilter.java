package net.thumbtack.school.notes.util;


import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class TagFilter {
    private static final Pattern PATTERN = Pattern.compile("^([-]?[A-Za-zА-ЯЁа-яё0-9_])*$");
    
    
    public static List<String> filter(List<String> tags) {
        return tags.stream().filter(tag -> PATTERN.matcher(tag).find()).collect(Collectors.toList());
    }
}
