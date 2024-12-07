import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface LogoutOutput {
  message: string;
}
interface UserOutputDTO {
  userName: string;
  password: string;
}

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent {
  userName: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  showMenu = false;

  constructor(private router: Router, private http: HttpClient) {}

  getCurrentUserName() {
    return localStorage.getItem('userName');
  }

  updateUserForm() {
    let user: any = {};

    if(this.userName != '') {
      user.userName = this.userName;
    }
    if(this.password != '') {
      user.password = this.password;
    }

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.patch<UserOutputDTO>('http://localhost:5510/user', user,
      { headers, withCredentials: true }).subscribe(
      (response: UserOutputDTO) => {
        localStorage.setItem('userName', response.userName);
        this.errorMessage = '';
        this.successMessage = "User credentials were update successfuly";
      },
      (error: HttpErrorResponse) => {
        if (error.status != 200) {
          // Handle validation errors
          this.successMessage = '';
          this.errorMessage = this.getErrorsFromResponse(error.error);
        }
      }
    );
  }

  logout() {

    localStorage.setItem('isLogged', 'false');
    localStorage.setItem('userName', 'anonymousUser');
    document.cookie = 'JSESSIONID' + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT; path=/;';

    this.http.post('http://localhost:5510/logout', {}, { withCredentials: true }).subscribe({
      next: () => {
        this.router.navigate(['login']);
      }
    });

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
