import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {StudentComponent} from "./student/student.component";
import {RegisterComponent} from "./register/register.component";
import {AddStudentComponent} from "./student/add-student/add-student.component";

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'studentList', component: StudentComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'addStudent', component: AddStudentComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
