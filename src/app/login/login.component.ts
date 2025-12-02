import { Component } from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    console.log(this.username , this.password);


    this.authService.login({ username: this.username, password: this.password })
      .subscribe(data =>{
        this.authService.saveToken(data.token);
        this.router.navigate(['/studentList']);
      });
  }
}
