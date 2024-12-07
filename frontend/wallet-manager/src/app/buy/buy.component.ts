import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface Offering {
  assetID: string;
  assetName: string;
  isCrypto: number;
  price: number;
}
interface Asset {
  assetID: string;
  amount: number;
}
interface BoughtCryptoOutput {
  user: string;
  cryptoID: string;
  message: string;
  amount: number;
  price: number;
}

@Component({
  selector: 'app-buy',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule
  ],
  templateUrl: './buy.component.html',
  styleUrl: './buy.component.css'
})
export class BuyComponent {
  selectedType: string = 'all'; // Default way offerings to be listed = 'all'
  displayConcreteAsset: boolean = false;
  assetId: string = '';
  errorMessage2: string = '';
  errorMessage1: string = '';
  successMessage: string = '';
  offerings: Offering[] = [];
  asset: Asset = {assetID: '', amount: 0};
  showMenu = false;

  constructor(private router: Router, private http: HttpClient) {}

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  buyForm() {
    const url = `http://localhost:5510/wallet/buy`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.post<BoughtCryptoOutput>(url, this.asset, { headers, withCredentials: true }).subscribe(
      (response: BoughtCryptoOutput) => {
        // Handle successful response
        this.errorMessage1 = '';
        this.successMessage = response.message;
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.successMessage = '';
        this.errorMessage1 = this.getErrorsFromResponse(error.error);
      }
    );
  }

  submitForm() {
    if (this.displayConcreteAsset) {
      this.fetchOfferingsByAssetId();
    } else {
      this.fetchOfferingsByType();
    }
  }

  fetchOfferingsByAssetId() {
    if (!this.assetId) {
      this.errorMessage2 = 'Asset ID is required when displaying concrete asset.';
      return;
    }

    const url = `http://localhost:5510/wallet/offerings/${this.assetId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.get<Offering>(url, { headers, withCredentials: true }).subscribe(
      (response: Offering) => {
        // Handle successful response
        this.offerings = [response];
        this.errorMessage2 = '';
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage2 = this.getErrorsFromResponse(error.error);
      }
    );
  }

  fetchOfferingsByType() {
    let url;

    if(this.selectedType === "all") {
      url = "http://localhost:5510/wallet/offerings";
    } else {
      url = `http://localhost:5510/wallet/offerings?asset_type=${this.selectedType}`;
    }

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.get<Offering[]>(url, { headers, withCredentials: true }).subscribe(
      (response: Offering[]) => {
        // Handle successful response
        this.offerings = response;
        this.errorMessage2 = '';
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage2 = this.getErrorsFromResponse(error.error);
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
