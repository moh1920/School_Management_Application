import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  username = '';
  password = '';
  confirmPassword = '';

  successMessage = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  register(form: any) {
    this.successMessage = '';
    this.errorMessage = '';

    if (form.invalid) {
      this.errorMessage = 'Veuillez remplir correctement tous les champs.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Les mots de passe ne correspondent pas.';
      return;
    }

    const payload = {
      username: this.username,
      password: this.password
    };

    this.authService.register(payload).subscribe(
      (data) => {
        this.successMessage = 'Compte créé avec succès !';
        console.log(data);
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      (error) => {
        this.errorMessage = 'Une erreur est survenue.';
        console.error(error);
      }
    );

  }
}
