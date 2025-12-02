import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs';
import { PagedResponse } from '../models/PagedResponse';
import { StudentResponse } from '../models/StudentResponse';
import { StudentService } from '../services/student.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.scss']
})
export class StudentComponent implements OnInit {
  students: StudentResponse[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;
  loading = false;
  errorMessage: string | null = null;

  constructor(private studentService: StudentService,private router : Router) {}

  ngOnInit(): void {
    this.loadStudents(this.page, this.size);
  }

  loadStudents(page: number, size: number): void {
    this.loading = true;
    this.errorMessage = null;
    this.page = page;

    this.studentService.getAllStudents(page, size)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (res: PagedResponse<StudentResponse>) => {
          this.students = res.content;
          this.size = res.size;
          this.totalPages = res.totalPages;
          this.totalElements = res.totalElements;
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = "Impossible de charger les étudiants. Réessayez plus tard.";
        }
      });
  }

  goToPage(p: number): void {
    if (p < 0 || p >= this.totalPages) return;
    this.loadStudents(p, this.size);
  }

  prev(): void {
    if (this.page > 0) {
      this.loadStudents(this.page - 1, this.size);
    }
  }

  next(): void {
    if (this.page < this.totalPages - 1) {
      this.loadStudents(this.page + 1, this.size);
    }
  }

  changeSize(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const newSize = Number(target.value);
    this.size = newSize;
    this.loadStudents(0, this.size);
  }

  pagesArray(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  getVisiblePages(): number[] {
    const maxVisible = 5;
    const halfVisible = Math.floor(maxVisible / 2);

    let startPage = Math.max(0, this.page - halfVisible);
    let endPage = Math.min(this.totalPages - 1, startPage + maxVisible - 1);

    if (endPage - startPage < maxVisible - 1) {
      startPage = Math.max(0, endPage - maxVisible + 1);
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  }

  protected readonly Math = Math;

  navigateToAddStudent(): void {
    this.router.navigate(['/addStudent']);
  }
}
