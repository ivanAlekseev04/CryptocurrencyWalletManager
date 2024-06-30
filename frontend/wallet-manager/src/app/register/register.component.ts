import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  user = { userName: '', password: '' };
  errorMessage = '';

  constructor(private router: Router, private http: HttpClient) {}

  onSubmit() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post('http://localhost:5510/register', this.user,
      { headers, withCredentials: true, observe: 'response' }).subscribe(
      (response: HttpResponse<any>) => {
        this.router.navigate(['login']);
      },
      (error: HttpErrorResponse) => {
        if (error.status != 200) {
          // Handle validation errors
          this.errorMessage = this.getErrorsFromResponse(error.error);
        }
      }
    );
  }

  private getErrorsFromResponse(errors: any): string {
    let errorMessage = '';
    Object.keys(errors).forEach(field => {
      errorMessage += `${field}: ${errors[field]} <br>`;
    });
    return errorMessage;
  }
}
