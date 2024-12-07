import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface Asset {
  name: string;
  price: number;
}
interface Transaction {
  id: number;
  sellingProfit?: number;
  dateOfCommit: Date; // was string
  crypto: Asset;
  amount: number;
  type: string;
}

@Component({
  selector: 'app-history-period',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './history-period.component.html',
  styleUrl: './history-period.component.css'
})
export class HistoryPeriodComponent {
  displayForConcreteAsset: boolean = false;
  errorMessage: string = '';
  transactions: Transaction[] = [];
  showMenu = false;
  beforeDateTime: Date | null = null; // Default initialisation
  afterDateTime: Date | null = null; // Default initialisation

  constructor(private router: Router, private http: HttpClient) {}

  private getErrorsFromResponse(errors: any): string {
    let errorMessage = '';
    Object.keys(errors).forEach(field => {
      errorMessage += `${field}: ${errors[field]} <br>`;
    });
    return errorMessage;
  }

  submitForm() {
    let url = 'http://localhost:5510/wallet/history/period';
    url = this.processTransactionsIntervals(url);

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.get<Transaction[]>(url, { headers, withCredentials: true }).subscribe(
      (response: Transaction[]) => {
        // Handle successful response
        this.transactions = response;
        this.errorMessage = '';
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage = this.getErrorsFromResponse(error.error);
      }
    );
  }

  processTransactionsIntervals(url: string): string {
    if(this.beforeDateTime != null) {
      url += `?before=${this.beforeDateTime}&`;
    }

    if(this.afterDateTime != null) {
      if(url.charAt(url.length - 1) === '&') { // Case when before date time was choosen
          url += `after=${this.afterDateTime}`;
      } else {
          url += `?after=${this.afterDateTime}`;
      }
    }

    return url;
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }
}
