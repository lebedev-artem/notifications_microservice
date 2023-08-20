package ru.skillbox.socialnetwork.notifications.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;

	@Column(name = "authorId")
	private String authorId;

	@Column(name = "text")
	private String notifyText;

	public Notification(String authorId, String notifyText) {
		this.authorId = authorId;
		this.notifyText = notifyText;
	}
}
