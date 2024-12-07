import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface Asset {
  asset: string,
  amount: number,
  current_price: number,
  current_profit: number
}

@Component({
  selector: 'app-summary',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './summary.component.html',
  styleUrl: './summary.component.css'
})
export class SummaryComponent {
  displayForConcreteAsset: boolean = false;
  assetId: string = '';
  errorMessage: string = '';
  assets: Asset[] = [];
  showMenu = false;
  formWasSubmitted: boolean = false;

  constructor(private router: Router, private http: HttpClient) {}

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

  submitForm() {
    let url = `http://localhost:5510/wallet/summary`;
    url = this.processTransactionsAssetId(url);

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.get<Asset[]>(url, { headers, withCredentials: true }).subscribe(
      (response: Asset[]) => {
        // Handle successful response
        this.assets = response;
        this.errorMessage = '';

        this.formWasSubmitted = true; // Show table in case of successful request
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage = this.getErrorsFromResponse(error.error);

        this.formWasSubmitted = false; // Hide table in case of bad request
      }
    );
  }

  processTransactionsAssetId(url: string): string {
    if(this.displayForConcreteAsset) {
      url += `?asset_id=${this.assetId}`;
    }

    return url;
  }
}
