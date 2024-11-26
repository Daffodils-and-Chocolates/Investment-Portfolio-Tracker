import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SearchService } from '../../services/search.service';

@Component({
  selector: 'app-crypto-results',
  templateUrl: './crypto-results.component.html',
  styleUrl: './crypto-results.component.css',
})
export class CryptoResultsComponent implements OnInit {
  results: any[] = [];
  loading = true;
  error: string | null = null;
  currentPage = 1;
  itemsPerPage = 10;

  constructor(
    private route: ActivatedRoute,
    private searchService: SearchService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const exchange = params['exchange'];
      if (exchange) {
        this.loading = true;
        this.searchService.getCryptoSymbols(exchange).subscribe({
          next: (data) => {
            this.results = data || [];
            this.loading = false;
          },
          error: (error) => {
            this.error =
              'An error occurred while fetching crypto symbols. Please try again.';
            this.loading = false;
          },
        });
      }
    });
  }

  get paginatedResults() {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.results.slice(start, start + this.itemsPerPage);
  }

  get totalPages() {
    return Math.ceil(this.results.length / this.itemsPerPage);
  }

  get pages() {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  setPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
}
