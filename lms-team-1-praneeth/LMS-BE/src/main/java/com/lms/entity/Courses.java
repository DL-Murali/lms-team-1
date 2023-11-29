package com.lms.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "courses")
public class Courses {

	@Id
	@JsonProperty(access = Access.WRITE_ONLY)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cid;

	private String coursename;

	private String coursetrainer;

	private LocalDateTime courseinsertdate;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_cid", referencedColumnName = "cid")
	private List<CourseModules> cmodule;

	@ManyToMany(mappedBy = "listcourses", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("courses")
	private List<UserCourse> uc;
}
