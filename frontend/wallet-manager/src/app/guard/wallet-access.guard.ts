import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common'; // Import isPlatformBrowser

@Injectable({
providedIn: 'root'
})
export class WalletAccessGuard implements CanActivate {

  constructor(private router: Router, @Inject(PLATFORM_ID) private platformId: Object) {}

  canActivate(): Observable<boolean> {
    if(isPlatformBrowser(this.platformId)) {
      const isLogged = localStorage.getItem('isLogged');

      if (isLogged === 'true') {
        return of(true);
      } else {
        this.router.navigate(['login']); // Redirect to login page if not logged in
        return of(false);
      }
    } else {
        return of(false); // Return observable of false
    }
  }
}
