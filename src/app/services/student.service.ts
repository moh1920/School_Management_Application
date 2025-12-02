import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {PagedResponse} from "../models/PagedResponse";
import {StudentResponse} from "../models/StudentResponse";
import {Level, StudentRequest} from "../models/StudentRequest";
import {FormGroup} from "@angular/forms";
const API_BASE_URL = 'http://localhost:8020/api/v1/student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private base = API_BASE_URL;


  constructor(private http: HttpClient) {}

  getAllStudents(page: number, size: number): Observable<PagedResponse<StudentResponse>> {
    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    const params = new HttpParams()
      .set('page', String(page))
      .set('size', String(size));

    return this.http.get<PagedResponse<StudentResponse>>(
      `${this.base}/getAllStudents`,
      { headers, params }
    );
  }


  createStudent(student: StudentRequest): Observable<StudentResponse> {
    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    return this.http.post<StudentResponse>(`${this.base}/createStudent`, student,{headers});
  }



  updateStudent(id: number, student: StudentRequest): Observable<any> {
    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    return this.http.put(`${this.base}/updateStudent/${id}`, student,{headers});
  }



  deleteStudent(id: number): Observable<any> {
    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    return this.http.delete(`${this.base}/deleteStudent/${id}`,{headers});
  }


  searchStudent(id?: number, username?: string): Observable<StudentResponse> {
    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    let params = new HttpParams();

    if (id !== undefined) params = params.set('id', id);
    if (username !== undefined && username.trim() !== '') params = params.set('username', username);

    return this.http.get<StudentResponse>(`${this.base}/search`, { params ,headers});
  }


  filterByLevel(level: string): Observable<StudentResponse> {

    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    const params = new HttpParams().set('level', level);
    return this.http.get<StudentResponse>(`${this.base}/filter`, { params ,headers});
  }

  getStudentById(id: number): Observable<StudentResponse> {
    return this.http.get<StudentResponse>(`${this.base}/getStudentById/${id}`)
      .pipe(catchError(this.handleError));
  }









  // Gestion d'erreur simple centralisée
  private handleError(error: any) {
    // vous pouvez ajouter logger, transformation d'erreur, messages user-friendly, etc.
    const err = (error && error.error) ? error.error : error;
    return throwError(() => err);
  }
}
