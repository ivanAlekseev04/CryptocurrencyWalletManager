import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true, // was true
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user = { userName: '', password: '' };
  errorMessage = '';

  constructor(private router: Router, private http: HttpClient) {}

  onSubmit() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post('http://localhost:5510/login', this.user,
      { headers, withCredentials: true, observe: 'response' }).subscribe(
      (response: HttpResponse<any>) => {
        localStorage.setItem('isLogged', 'true');
        localStorage.setItem('userName', this.user.userName);

        this.router.navigate(['wallet']);
      },
      (error: HttpErrorResponse) => {
        if (error.status != 200) {
          // Handle validation errors
          this.errorMessage = this.getErrorsFromResponse(error.error);
        }
      }
    );
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  private getErrorsFromResponse(errors: any): string {
    let errorMessage = '';
    Object.keys(errors).forEach(field => {
      errorMessage += `${field}: ${errors[field]} <br>`;
    });
    return errorMessage;
  }
}
