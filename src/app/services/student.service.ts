import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {PagedResponse} from "../models/PagedResponse";
import {StudentResponse} from "../models/StudentResponse";
import {Level, StudentRequest} from "../models/StudentRequest";
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

  getStudentById(id: number): Observable<StudentResponse> {
    return this.http.get<StudentResponse>(`${this.base}/getStudentById/${id}`)
      .pipe(catchError(this.handleError));
  }




  deleteStudent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/deleteStudent/${id}`)
      .pipe(catchError(this.handleError));
  }

  searchStudent(id?: number, username?: string): Observable<StudentResponse> {
    const token = localStorage.getItem('jwt');
    const headers = token ? new HttpHeaders({ 'Authorization': `Bearer ${token}` }) : undefined;

    let params = new HttpParams();
    if (id != null) { params = params.set('id', String(id)); }
    if (username) { params = params.set('username', username); }
    return this.http.get<StudentResponse>(`${this.base}/search`, { params,headers })
      .pipe(catchError(this.handleError));
  }

  // Filtrer par level (enum)
  filterByLevel(level: Level): Observable<StudentResponse> {
    const params = new HttpParams().set('level', level);
    return this.http.get<StudentResponse>(`${this.base}/filter`, { params })
      .pipe(catchError(this.handleError));
  }

  /**
   * Upload CSV de students.
   * Retourne HttpEvent pour permettre suivi progression (upload progress).
   * On peut l'utiliser dans le composant avec observe:'events' et reportProgress:true.
   */
  uploadStudents(file: File): Observable<HttpEvent<any>> {
    const form = new FormData();
    form.append('file', file, file.name);

    const req = new HttpRequest('POST', `${this.base}/upload-students`, form, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req).pipe(catchError(this.handleError));
  }

  // Gestion d'erreur simple centralisée
  private handleError(error: any) {
    // vous pouvez ajouter logger, transformation d'erreur, messages user-friendly, etc.
    const err = (error && error.error) ? error.error : error;
    return throwError(() => err);
  }
}
