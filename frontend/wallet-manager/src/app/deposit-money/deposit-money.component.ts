import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

export interface UserCryptoDTO {
  // Define the properties of UserCryptoDTO here
  id: number;
  cryptoName: string;
  amount: number;
  value: number;
}
export interface UserDTOOutput {
  id: number;
  userName: string;
  money: number;
  cryptocurrenciesDTO: Set<UserCryptoDTO>;
  overallTransactionsProfit: number;
}

@Component({
  selector: 'app-deposit-money',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './deposit-money.component.html',
  styleUrl: './deposit-money.component.css'
})
export class DepositMoneyComponent {
  amount: number | null = null;
  errorMessage: string = '';
  successMessage: string = '';
  showMenu = false;

  constructor(private router: Router, private http: HttpClient) {}

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  onSubmit() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const depositUrl = 'http://localhost:5510/wallet/money/deposit';

    if(this.amount === null) {
      this.amount = 0;
    }
    const requestBody = { money: this.amount };

    this.http.patch<UserDTOOutput>(depositUrl, requestBody, { headers, withCredentials: true }).subscribe(
      (response: UserDTOOutput) => {
        this.errorMessage = ''; // Clear any previous error messages
        this.successMessage = `Current balance is: ${response.money}`;
      },
      error => {
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
