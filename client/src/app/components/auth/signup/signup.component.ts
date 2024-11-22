import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  signupData = { email: '', password: '', name: '' };
  errorMessage: string | null = null;
  passwordVisible = false;

  constructor(private authService: AuthService, private router: Router) {}

  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }
  onSubmit(): void {
    this.authService.signup(this.signupData).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/home']);  // Redirect to login after signup
      },
      error: (error) => {
        this.errorMessage = 'Error creating account. Please try again.';
      }
    });
  }
}
