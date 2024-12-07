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
  dateOfCommit: Date;
  crypto: Asset;
  amount: number;
  type: string;
}

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent {
  selectedType: string = 'ALL'; // Default way offerings to be listed = 'ALL'
  displayForConcreteAsset: boolean = false;
  assetId: string = '';
  errorMessage: string = '';
  transactions: Transaction[] = [];
  showMenu = false;
  formWasSubmitted: boolean = false;

  constructor(private router: Router, private http: HttpClient) {}

  private getErrorsFromResponse(errors: any): string {
    let errorMessage = '';
    Object.keys(errors).forEach(field => {
      errorMessage += `${field}: ${errors[field]} <br>`;
    });
    return errorMessage;
  }

  submitForm() {
    let url = 'http://localhost:5510/wallet/history';
    url = this.processTransactionsType(url);
    url = this.processTransactionsAssetId(url);

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.get<Transaction[]>(url, { headers, withCredentials: true }).subscribe(
      (response: Transaction[]) => {
        // Handle successful response
        this.transactions = response;
        this.errorMessage = '';

        this.formWasSubmitted = true; // show table in case of successful request
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage = this.getErrorsFromResponse(error.error);

        this.formWasSubmitted = false; // Hide table in case of bad request
      }
    );
  }

  processTransactionsType(url: string): string {
    if(this.selectedType === 'ALL') {
      return url;
    } else {
      return (url + `?type=${this.selectedType}&`);
    }
  }

  processTransactionsAssetId(url: string): string {
    if(this.displayForConcreteAsset) {
      if(url.charAt(url.length - 1) === '&') { // Case when specific type of transactions was choosen
          return (url + `asset_id=${this.assetId}`);
      } else {
          return (url + `?asset_id=${this.assetId}`);
      }
    }

    return url;
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }
}
