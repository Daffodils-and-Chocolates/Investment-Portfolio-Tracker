import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { WatchlistService } from '../../services/watchlist.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(private router: Router, private watchlistService: WatchlistService) {}

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token'); 
    this.watchlistService.refreshWatchlist();
    this.router.navigate(['/']); 
  }
}