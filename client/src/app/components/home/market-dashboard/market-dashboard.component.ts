import { Component, OnInit } from '@angular/core';
import { HomeService } from '../../../services/home.service';

@Component({
  selector: 'app-market-dashboard',
  templateUrl: './market-dashboard.component.html',
  styleUrl: './market-dashboard.component.css'
})
export class MarketDashboardComponent implements OnInit {
  marketStatus: any = null;
  marketHolidays: any[] = [];
  marketNews: any[] = [];
  selectedHoliday: string = '';
  newsPerPage: number = 5;
  currentPage: number = 1;
  newsGroups = ['general', 'forex', 'crypto', 'merger'];
  activeGroup = 'general';

  newsCategories = [3,5,8]; // Options for items per page

  constructor(private homeService: HomeService) {}

  ngOnInit(): void {
    this.loadMarketStatus();
    this.loadMarketHolidays();
    this.loadMarketNews();
  }

  // Fetch market status
  loadMarketStatus(): void {
    this.homeService.getMarketStatus().subscribe(
      (data) => {
        this.marketStatus = data;
      },
      (err) => {
        console.error('Error fetching market status', err);
      }
    );
  }

  // Fetch market holidays (limit to 5 by default)
  loadMarketHolidays(): void {
    this.homeService.getMarketHolidays().subscribe(
      (data) => {
        this.marketHolidays = data.data.slice(0, 5);
      },
      (err) => {
        console.error('Error fetching market holidays', err);
      }
    );
  }

  // Fetch market news
  loadMarketNews(): void {
    this.homeService.getNews(this.activeGroup).subscribe(
      (data) => {
        this.marketNews = data;
      },
      (err) => {
        console.error('Error fetching market news', err);
      }
    );
  }


  changeNewsGroup(group: string): void {
    this.activeGroup = group;
    this.loadMarketNews();
  }

  // Get paginated news
  get paginatedNews(): any[] {
    const start = (this.currentPage - 1) * this.newsPerPage;
    return this.marketNews.slice(start, start + this.newsPerPage);
  }

  // Calculate the number of pages
  get totalPages(): number[] {
    if (!this.marketNews.length) {
      return [];
    }
    const total = Math.floor(this.marketNews.length / this.newsPerPage) + (this.marketNews.length % this.newsPerPage ? 1 : 0);
    return Array.from({ length: total }, (_, i) => i + 1);
  }

  // Handle pagination change
  changePage(page: number): void {
    this.currentPage = page;
  }

  // Handle change in number of news items per page
  changeNewsPerPage(count: number): void {
    this.newsPerPage = count;
    this.currentPage = 1; // Reset to the first page
  }
}
