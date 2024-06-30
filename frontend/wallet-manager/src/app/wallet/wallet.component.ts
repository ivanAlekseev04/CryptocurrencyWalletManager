import { NgModule } from '@angular/core';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule, Routes } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { WalletService } from '../../service/wallet/wallet.service';

@Component({
  selector: 'app-wallet',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule,
    RouterModule
  ],
  templateUrl: './wallet.component.html',
  styleUrl: './wallet.component.css'
})
export class WalletComponent {
  isLogged: boolean = false;
  requested: boolean = false;
  errorMessage: string = '';

  constructor(private walletService: WalletService, private router: Router) {}

  // TODO: maybe there is a problem
  fetchData() {
    this.walletService.fetchData().subscribe(
      (response: boolean) => {
        // Assuming data is a string received from the backend
        this.requested = true;
        this.isLogged = response;
      },
      (error) => {
        this.errorMessage = 'Error fetching data'; // Handle error
        console.error('Error fetching data:', error);
      }
    );
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }
}
