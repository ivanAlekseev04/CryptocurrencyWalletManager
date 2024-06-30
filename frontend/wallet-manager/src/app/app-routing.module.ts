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

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full'}, // Redirect root to /login
  { path: 'login', component: LoginComponent, data: {title: 'Login'}},
  { path: 'register', component: RegisterComponent, data: {title: 'Register'}},
  { path: 'wallet', component: WalletComponent, data: {title: 'CryptocurrencyWalletManager'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/buy', component: BuyComponent, data: {title: 'Buy asset'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/deposit_money', component: DepositMoneyComponent, data: {title: 'Deposit money'}, canActivate: [WalletAccessGuard] },
  { path: 'wallet/sell', component: SellComponent, data: {title: 'Sell asset'}, canActivate: [WalletAccessGuard] },
  { path: 'user', component: UserComponent, data: {title: 'User profile'}, canActivate: [WalletAccessGuard] },
  { path: '**', component: NotfoundComponent, data: {title: '404'} }  // Wildcard route
];

@NgModule({
imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
