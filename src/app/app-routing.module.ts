import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {StudentComponent} from "./student/student.component";
import {RegisterComponent} from "./register/register.component";
import {AddStudentComponent} from "./student/add-student/add-student.component";
import {AuthGuard} from "./services/auth.guard";

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'studentList', component: StudentComponent , canActivate : [AuthGuard] },
  { path: 'register', component: RegisterComponent },
  { path: 'addStudent', component: AddStudentComponent ,canActivate : [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
