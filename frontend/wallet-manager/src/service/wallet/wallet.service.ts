import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class WalletService {

  private apiURL = 'http://localhost:5510/is_logged';

  constructor(private http: HttpClient) { }

  // TODO: was Observable<any>
  fetchData(): Observable<boolean> {
    return this.http.get<boolean>(this.apiURL, { withCredentials: true });
  }
}
