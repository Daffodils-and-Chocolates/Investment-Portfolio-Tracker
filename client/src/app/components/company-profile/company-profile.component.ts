import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CompanyService } from '../../services/company.service';

@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrl: './company-profile.component.css',
})
export class CompanyProfileComponent implements OnInit {
  company: any = null;
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private companyService: CompanyService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      const symbol = params['symbol'];
      if (symbol) {
        this.loading = true;
        this.getCompanyProfile(symbol);
      }
    });
  }

  getCompanyProfile(symbol : any) {
    this.companyService.getCompanyProfile(symbol).subscribe({
      next: (data) => {
        this.company = data;
        this.loading = false;
      },
      error: (error) => {
        this.error =
          'An error occurred while fetching company profile. Please try again.';
        this.loading = false;
      },
    });
  }
}
