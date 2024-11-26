import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginData = { email: '', password: '' };
  errorMessage: string | null = null;
  passwordVisible = false;        

  constructor(private authService: AuthService, private router: Router,private toastr  : ToastrService) {}

  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }

  onSubmit(): void {
    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        /* console.log('Token received:', response.token); // Log the token */
        localStorage.setItem('token', response.token); // Store JWT token
        /* console.log('Redirecting to home...'); */
        this.loginSuccess();
        this.router.navigate(['/home']);   // Redirect to home page after login
      },
      error: (error) => {
        this.loginFailure();
        this.errorMessage = 'Invalid email or password';
        console.error('Login error:', error);
      },
    });
  }

  loginSuccess() {
    this.toastr.success('Login successful!', 'Welcome');
  }
      

  loginFailure() {
    this.toastr.error('Login failed. Please try again.', 'Error');
  }
}