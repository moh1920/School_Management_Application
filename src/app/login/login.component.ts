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
    this.authService.login({ username: this.username, password: this.password })
      .subscribe(
        (data) => {

          this.authService.saveToken(data);
          localStorage.setItem('authorized', 'true');
          this.router.navigate(['/studentList']);
        },
         (err) => {
          if (err.status === 401) {
            this.errorMessage = "Identifiants invalides ";
          }
          else if (err.status === 429) {
            this.errorMessage = "Trop de tentatives, veuillez réessayer plus tard ";
          }
          else {
            this.errorMessage = "Erreur inconnue. Réessayez plus tard.";
          }

      });

  }

}
