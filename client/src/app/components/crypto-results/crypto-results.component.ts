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
  filteredResults: any[] = [];
  searchTerm: string = ''; 
  error: string | null = null;
  currentPage = 1;
  itemsPerPage = 18;
  pageRange = 5;

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
            this.filteredResults = this.results;
            this.loading = false;
          },
          error: () => {
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
    return this.filteredResults.slice(start, start + this.itemsPerPage); // Use filteredResults here
  }

  get totalPages() {
    return Math.ceil(this.filteredResults.length / this.itemsPerPage); // Use filteredResults for total pages
  }

  get pages() {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  setPage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  filterResults() {
    if (this.searchTerm.trim() === '') {
      this.filteredResults = this.results; // Show all if no search term
    } else {
      this.filteredResults = this.results.filter((result) =>
        result.displaySymbol.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        result.description.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        result.symbol.toLowerCase().includes(this.searchTerm.toLowerCase())

      );
    }
    this.currentPage = 1; // Reset to first page after search
  }
}
