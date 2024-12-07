import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface Asset {
  assetID: string;
  amount: number;
}
interface SoldCryptoOutput {
  user: string;
  cryptoID: string;
  message: string;
  amount: number;
  profit: number;
}

@Component({
  selector: 'app-sell',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './sell.component.html',
  styleUrl: './sell.component.css'
})
export class SellComponent {
  errorMessage: string = '';
  successMessage: string = '';
  asset: Asset = {assetID: '', amount: 0};
  showMenu = false;

  constructor(private router: Router, private http: HttpClient) {}

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  sellForm() {
    const url = `http://localhost:5510/wallet/sell`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post<SoldCryptoOutput>(url, this.asset, { headers, withCredentials: true }).subscribe(
      (response: SoldCryptoOutput) => {
        // Handle successful response
        this.errorMessage = '';
        this.successMessage = response.message;
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.successMessage = '';
        this.errorMessage = this.getErrorsFromResponse(error.error);
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
