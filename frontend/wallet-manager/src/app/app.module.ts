import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { ChartModule } from 'angular-highcharts'; // For diagrams

import { UserComponent } from './user/user.component';
import { SellComponent } from './sell/sell.component';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { WalletComponent } from './wallet/wallet.component'
import { BuyComponent } from './buy/buy.component';
import { DepositMoneyComponent } from './deposit-money/deposit-money.component';
import { HistoryComponent } from './history/history.component';
import { HistoryPeriodComponent } from './history-period/history-period.component';
import { SummaryComponent } from './summary/summary.component';
import { OverallSummaryComponent } from './overall-summary/overall-summary.component';

@NgModule({
declarations: [
  AppComponent,
  LoginComponent,
  RegisterComponent,
  WalletComponent,
  BuyComponent,
  DepositMoneyComponent,
  SellComponent,
  UserComponent,
  HistoryComponent,
  HistoryPeriodComponent,
  SummaryComponent,
  OverallSummaryComponent
],
imports: [
  BrowserModule,
  AppRoutingModule,
  FormsModule,
  HttpClientModule,
  RouterModule,
  ChartModule,
  HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN',
  })
],
providers: [],
bootstrap: [AppComponent]
})

export class AppModule { }
