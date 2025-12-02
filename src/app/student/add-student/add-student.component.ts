import {Component, OnInit, Query} from '@angular/core';
import {StudentRequest} from "../../models/StudentRequest";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StudentService} from "../../services/student.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.scss']
})
export class AddStudentComponent implements OnInit{
  studentForm!: FormGroup;
  message: string = '';

  private fb: FormBuilder;

  constructor(fb: FormBuilder, private studentService: StudentService,private router : Router) {
    this.fb = fb;
  }

  ngOnInit(): void {
    this.studentForm = this.fb.group({
      username: ['', Validators.required],
      level: ['', Validators.required],
    });
  }

  saveStudent() {
    if (this.studentForm.invalid) {
      this.message = 'Veuillez remplir correctement le formulaire.';
      return;
    }

    const student: StudentRequest = this.studentForm.value;

    this.studentService.createStudent(student).subscribe(
       (res) => {
        this.message = 'Étudiant créé avec succès !';
        this.studentForm.reset();
      },
       (err) => {
        this.message = 'Erreur lors de la création : ' + (err.error || err.message);

    });
  }

  cancel() {
    this.router.navigate(['/studentList']);

  }
}
