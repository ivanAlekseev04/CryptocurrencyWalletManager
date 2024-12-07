import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WalletAccessGuard } from './guard/wallet-access.guard'

import { UserComponent } from './user/user.component';
import { SellComponent } from './sell/sell.component';
import { DepositMoneyComponent } from './deposit-money/deposit-money.component';
import { BuyComponent } from './buy/buy.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { WalletComponent } from './wallet/wallet.component';
import { NotfoundComponent } from './notfound/notfound.component';
import { HistoryComponent } from './history/history.component';
import { HistoryPeriodComponent } from './history-period/history-period.component';
import { SummaryComponent } from './summary/summary.component';
import { OverallSummaryComponent } from './overall-summary/overall-summary.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full'}, // Redirect root to /login
  { path: 'login', component: LoginComponent, data: {title: 'Login'}},
  { path: 'register', component: RegisterComponent, data: {title: 'Register'}},
  { path: 'wallet', component: WalletComponent, data: {title: 'CryptocurrencyWalletManager'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/buy', component: BuyComponent, data: {title: 'Buy asset'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/money/deposit', component: DepositMoneyComponent, data: {title: 'Deposit money'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/sell', component: SellComponent, data: {title: 'Sell asset'}, canActivate: [WalletAccessGuard] },
  { path: 'user', component: UserComponent, data: {title: 'User profile'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/history', component: HistoryComponent, data: {title: 'Transaction history'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/history/period', component: HistoryPeriodComponent, data: {title: 'Transaction history by period'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/summary', component: SummaryComponent, data: {title: 'Wallet summary'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/summary/overall', component: OverallSummaryComponent, data: {title: 'Wallet overall summary'}, canActivate: [WalletAccessGuard] },
  { path: '**', component: NotfoundComponent, data: {title: '404'} }  // Routing for not defined routes
];

@NgModule({
imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
