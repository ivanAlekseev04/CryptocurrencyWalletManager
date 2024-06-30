import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiURL = 'http://localhost:5510/is_logged';

  constructor(private http: HttpClient) { }

  checkWalletAccess(): Observable<boolean> {
    return this.http.get<boolean>(this.apiURL, { withCredentials: true });
  }

  /*logout() {
    localStorage.removeItem('isLogged');
    this.router.navigate(['login']);
  }*/

  /*isAuthenticated(): boolean {
    return localStorage.getItem('isLoggedIn') === 'true';
    //if (this.isBrowser()) {
      //return localStorage.getItem('isLoggedIn') === 'true';
    //}
    //return false;
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }*/
}
