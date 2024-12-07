import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Chart } from 'angular-highcharts'; // For diagrams
import { ChartModule } from 'angular-highcharts'; // For diagrams

interface OverallSummary {
  id: number,
  username: string,
  money: number,
  overall_profit: number,
  active_assets: Asset[]
}
interface Asset {
  asset: string,
  amount: number,
  current_price: number,
  current_profit: number
}

@Component({
  selector: 'app-overall-summary',
  standalone: true,
  imports: [
    FormsModule,
    HttpClientModule,
    CommonModule,
    ChartModule
  ],
  templateUrl: './overall-summary.component.html',
  styleUrl: './overall-summary.component.css'
})
export class OverallSummaryComponent implements OnInit {
  errorMessage: string = '';
  overallSummary: OverallSummary | null = null;
  assets: Asset[] = [];
  showMenu = false;
  pieChart: Chart = new Chart();

  constructor(private router: Router, private http: HttpClient) {}

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  ngOnInit(): void {
    const url = `http://localhost:5510/wallet/summary/overall`;

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    this.http.get<OverallSummary>(url, { headers, withCredentials: true }).subscribe(
      (response: OverallSummary) => {
        // Handle successful response
        this.overallSummary = response;
        this.assets = response.active_assets; // Bind active_assets to the component property
        this.drawChart();
        this.errorMessage = '';
      },
      (error: HttpErrorResponse) => {
        // Handle error
        this.errorMessage = this.getErrorsFromResponse(error.error);
      }
    );
  }

  drawChart() {
    this.pieChart = new Chart({
      chart: {
        type: 'pie',
        plotShadow: false,
      },

      credits: {
        enabled: false,
      },

      plotOptions: {
        pie: {
          innerSize: '99%',
          borderWidth: 10,
          borderColor: '',
          slicedOffset: 10,
          dataLabels: {
            connectorWidth: 0,
          },
        },
      },

      title: {
        verticalAlign: 'middle',
        floating: true,
        text: 'My active assets',
      },

      legend: {
        enabled: false,
      },

      series: [
      {
        type: 'pie',
        data: this.getData(),
      },
      ],
    })
  }

  getData() {
    return this.assets.map(asset => ({
      name: asset.asset,
      y: asset.amount,
      color: this.getRandomColor()
    }));
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  private getErrorsFromResponse(errors: any): string {
    let errorMessage = '';
    Object.keys(errors).forEach(field => {
      errorMessage += `${field}: ${errors[field]} <br>`;
    });
    return errorMessage;
  }
}
