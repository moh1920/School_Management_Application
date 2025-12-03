import { Injectable } from '@angular/core';
import { Router, UrlTree, CanActivate } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    if (localStorage.getItem('authorized') === 'true')
    {
      console.log("authorized");
      return true;
    } else {
      console.log("not authorized");
      return this.router.parseUrl('/login');   // better than navigate()
    }
  }
}
