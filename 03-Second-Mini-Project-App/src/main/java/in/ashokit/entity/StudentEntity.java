package in.ashokit.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
public class StudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String stuName;
	private Long phno;
	private String courseName;
	private String classMode;
	private String enqStatus;

	@CreationTimestamp
	private Date createdDate;
	@UpdateTimestamp
	private Date updatedDate;

	@ManyToOne
	@JoinColumn(name = "user_id" )	
	private UserEntity user;
}
