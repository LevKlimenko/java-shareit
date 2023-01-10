package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String text;
    @ManyToOne
    @Column(name = "item_id")
    Item item;
    @ManyToOne
    @Column(name = "author_id")
    User author;
    @Column
    LocalDateTime created;
}
