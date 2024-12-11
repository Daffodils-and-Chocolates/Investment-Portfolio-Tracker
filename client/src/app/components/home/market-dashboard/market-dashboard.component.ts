import { Component, OnInit, ViewChild } from '@angular/core';
import { HomeService } from '../../../services/home.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

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

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  newsDataSource = new MatTableDataSource<any>();
  
  constructor(private homeService: HomeService) {}

  ngOnInit(): void {
    this.loadMarketStatus();
    this.loadMarketHolidays();
    this.loadMarketNews();
  }

  ngAfterViewInit(): void {
    if (this.paginator) {
      this.newsDataSource.paginator = this.paginator;
    } else {
      console.error('Paginator is undefined');
      console.log('Paginator is undefined in ngAfterViewINit');
    }
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
        this.newsDataSource.data = this.marketNews;
        if (this.paginator) {
          this.newsDataSource.paginator = this.paginator;
          this.paginator.firstPage();
        }
      },
      (err) => {
        console.error('Error fetching market news', err);
      }
    );
  }

  changeNewsGroup(group: string): void {
    this.activeGroup = group;
    this.loadMarketNews();
    if (this.paginator) {
      this.paginator.firstPage();
    }
  }
}
