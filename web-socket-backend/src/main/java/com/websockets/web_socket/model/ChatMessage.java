package com.websockets.web_socket.model;

import com.websockets.web_socket.abstractclass.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_messages")
public class ChatMessage extends AbstractEntity<Long> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_messages_SEQ_GEN")
    @SequenceGenerator(name = "chat_messages_SEQ_GEN", sequenceName = "chat_messages_seq", initialValue = 1, allocationSize = 1)
    @Basic(optional = false)
    private Long id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "content")
    private String content;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "is_saved")
    private boolean saved;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;


}
