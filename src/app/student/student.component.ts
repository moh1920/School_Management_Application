import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { PagedResponse } from '../models/PagedResponse';
import { StudentResponse } from '../models/StudentResponse';
import { StudentService } from '../services/student.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StudentRequest } from '../models/StudentRequest';
import {AuthService} from "../services/auth.service";

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

  studentForm!: FormGroup;
  showModal = false;
  currentStudentId!: number;

  searchValue: string = "";
  isSearching = false;

  isFiltering = false;


  constructor(
    private studentService: StudentService,
    private router: Router,
    private fb: FormBuilder,
    private authService : AuthService,
  ) {}

  ngOnInit(): void {
    this.studentForm = this.fb.group({
      username: ['', Validators.required],
      level: ['', Validators.required]
    });

    this.loadStudents(this.page, this.size);
  }

  loadStudents(page: number, size: number): void {
    this.loading = true;
    this.errorMessage = null;
    this.page = page;

    this.studentService.getAllStudents(page, size)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (res: PagedResponse<StudentResponse>) => {
          this.students = res.content || [];
          this.size = res.size ?? this.size;
          this.totalPages = res.totalPages ?? 0;
          this.totalElements = res.totalElements ?? 0;
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

  // suppression: on recharge la page actuelle après succès
  deleteStudent(id: number) {
    if (!confirm('Voulez-vous vraiment supprimer cet étudiant ?')) {
      return;
    }

    this.studentService.deleteStudent(id).subscribe({
      next: () => {
        const willHaveNoItems = (this.totalElements - 1) <= this.page * this.size && this.page > 0;
        const newPage = willHaveNoItems ? this.page - 1 : this.page;
        this.totalElements = Math.max(0, this.totalElements - 1);
        this.loadStudents(newPage, this.size);
      },
      error: (err) => {
        console.error('Erreur lors de la suppression :', err);
        this.errorMessage = "Impossible de supprimer l'étudiant. Réessayez.";
      }
    });
  }

  editStudent(student: StudentResponse) {
    if (!student) return;

    this.currentStudentId = student.id;
    this.studentForm.patchValue({
      username: student.username,
      level: student.level
    });
    this.showModal = true;

    // focus sur modal pour capture du keydown.escape (optionnel selon DOM)
    setTimeout(() => {
      const backdrop = document.querySelector('.modal-backdrop') as HTMLElement | null;
      if (backdrop) backdrop.focus();
    }, 0);
  }

  closeModal() {
    this.showModal = false;
    this.studentForm.reset();
  }

  updateStudent() {
    if (this.studentForm.valid) {
      const student: StudentRequest = this.studentForm.value;
      this.studentService.updateStudent(this.currentStudentId, student).subscribe({
        next: () => {
          this.loadStudents(this.page, this.size);
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = "Impossible de mettre à jour l'étudiant. Réessayez.";
        }
      });
    } else {
      this.studentForm.markAllAsTouched();
    }
  }

  trackById(index: number, item: StudentResponse) {
    return item.id;
  }


  performSearch() {
    if (!this.searchValue.trim()) return;

    this.isSearching = true;
    this.isFiltering = false;

    const value = this.searchValue.trim();

    const id = Number(value);
    const isNumeric = !isNaN(id);

    this.studentService.searchStudent(isNumeric ? id : undefined, !isNumeric ? value : undefined)
      .subscribe({
        next: (student) => {
          this.students = [student];
          this.totalElements = 1;
          this.totalPages = 1;
        },
        error: () => {
          this.students = [];
          this.errorMessage = "Aucun étudiant trouvé.";
        }
      });
  }


  resetSearch() {
    this.searchValue = "";
    this.isSearching = false;
    this.loadStudents(0, this.size);
  }


  filterByLevel(event: Event) {
    const level = (event.target as HTMLSelectElement).value;

    if (!level) {
      this.resetFilter();
      return;
    }

    this.isFiltering = true;
    this.isSearching = false;

    this.studentService.filterByLevel(level)
      .subscribe(
         (student) => {
           console.log(student);
          this.students = [student];
          this.totalElements = 1;
          this.totalPages = 1;
        },
         () => {
          this.students = [];
          this.errorMessage = "Aucun étudiant trouvé avec ce niveau.";

      });
  }
  resetFilter() {
    this.isFiltering = false;
    this.loadStudents(0, this.size);
  }

  logout() {
    this.authService.logout()
    setTimeout(() => this.router.navigate(['/login']), 1500);

  }
}
