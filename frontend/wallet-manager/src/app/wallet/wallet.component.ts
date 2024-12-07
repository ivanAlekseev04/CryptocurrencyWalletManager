import { NgModule } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule, Routes } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { Chart } from 'angular-highcharts'; // For diagrams
import { ChartModule } from 'angular-highcharts'; // For diagrams

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
  selector: 'app-wallet',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule,
    RouterModule,
    ChartModule
  ],
  templateUrl: './wallet.component.html',
  styleUrl: './wallet.component.css'
})
export class WalletComponent implements OnInit {
  errorMessage: string = '';
  showMenu = false;
  transactions: Transaction[] = [];
  pieChart: Chart = new Chart();
  curUser: string | null = '';

  constructor(private router: Router, private http: HttpClient, private datePipe: DatePipe) {}

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

  ngOnInit(): void {
    this.curUser = localStorage.getItem('userName');
    const today = new Date();

    // Set 'after' to the end of the current day (23:59:59)
    let afterDate = new Date(today.getFullYear(), today.getMonth(), today.getDate(), 23, 59, 59);
    afterDate.setDate(afterDate.getDate() - 1);
    const afterDateFormatted = this.datePipe.transform(afterDate, 'yyyy-MM-ddTHH:mm:ss');

    // Set 'before' to the start of the current day (00:00:00)
    let beforeDate = new Date(today.getFullYear(), today.getMonth(), today.getDate(), 0, 0, 0);
    beforeDate.setDate(beforeDate.getDate() + 1);
    const beforeDateFormatted = this.datePipe.transform(beforeDate, 'yyyy-MM-ddTHH:mm:ss');

    const url = `http://localhost:5510/wallet/history/period?before=${beforeDateFormatted}&after=${afterDateFormatted}`;

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.get<Transaction[]>(url, { headers, withCredentials: true }).subscribe(
      (response: Transaction[]) => {
        // Handle successful response
        this.transactions = response;
        this.errorMessage = '';

        const hourlyData = this.processTransactions(response);
        this.initChart(hourlyData);
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage = this.getErrorsFromResponse(error.error);
      }
    );
  }

  processTransactions(transactions: Transaction[]): number[] {
    const hours = new Array(24).fill(0);

    transactions.forEach(transaction => {
      const date = new Date(transaction.dateOfCommit);
      const hour = date.getHours();
      hours[hour]++;
    });

    return hours;
  }

  initChart(hourlyData: number[]) {
    this.pieChart = new Chart({
      chart: {
        type: 'column'
      },
      title: {
        text: 'Transactions Per Hour Today'
      },
      xAxis: {
        categories: Array.from({ length: 24 }, (_, i) => `${i}:00 - ${i + 1}:00`),
        crosshair: true
      },
      yAxis: {
        min: 0,
        title: {
          text: 'Number of Transactions'
        }
      },
      series: [
        {
          name: 'Transactions',
          type: 'column',
          data: hourlyData
        }
      ]
    });
  }
}
