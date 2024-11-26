import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})
export class SearchBarComponent {
  searchType: string = 'symbol';
  searchQuery: string = '';

  constructor(private router: Router) {}

  getPlaceholder(): string {
    switch(this.searchType) {
      case 'symbol':
        return 'Search by symbol, security\'s name, ISIN or Cusip';
      case 'company':
        return 'Search by company\'s symbol';
      case 'crypto':
        return 'Search by supported crypto exchanges';
      default:
        return 'Search...';
    }
  }

  search() {
    if (!this.searchQuery.trim()) return;
    
    switch(this.searchType) {
      case 'symbol':
        this.router.navigate(['/symbol-results'], { 
          queryParams: { query: this.searchQuery } 
        });
        break;
      case 'company':
        this.router.navigate(['/company-profile'], { 
          queryParams: { symbol: this.searchQuery } 
        });
        break;
      case 'crypto':
        this.router.navigate(['/crypto-results'], { 
          queryParams: { exchange: this.searchQuery } 
        });
        break;
    }
  }
}