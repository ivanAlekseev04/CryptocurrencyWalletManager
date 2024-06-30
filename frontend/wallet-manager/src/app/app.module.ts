import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router';  // Import RouterModule
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClientXsrfModule } from '@angular/common/http';

import { WalletService } from '../service/wallet/wallet.service';
import { AuthService } from '../service/auth/auth.service';

import { UserComponent } from './user/user.component';
import { SellComponent } from './sell/sell.component';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { WalletComponent } from './wallet/wallet.component'
import { BuyComponent } from './buy/buy.component';
import { DepositMoneyComponent } from './deposit-money/deposit-money.component';

@NgModule({
declarations: [
  AppComponent,
  LoginComponent,
  RegisterComponent,
  WalletComponent,
  BuyComponent,
  DepositMoneyComponent,
  SellComponent,
  UserComponent
],
imports: [
  BrowserModule,
  AppRoutingModule,
  FormsModule,
  HttpClientModule,
  RouterModule,
  HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN',
  })
],
providers: [
  WalletService, AuthService
],
bootstrap: [AppComponent]
})

export class AppModule { }
