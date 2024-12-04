import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { HomeService } from '../../../services/home.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-ipo-calender',
  templateUrl: './ipo-calender.component.html',
  styleUrl: './ipo-calender.component.css'
})
export class IpoCalendarComponent implements OnInit {
  @Input() onHome: boolean = false;
  ipoCalendar: any[] = [];
  error: string = '';
  form: FormGroup;

  constructor(private homeService: HomeService, private fb: FormBuilder, private router: Router, private route: ActivatedRoute) {
    this.form = this.fb.group({
      from: [this.getFirstDayOfMonth()],
      to: [this.getLastDayOfMonth()]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const from = params['from'] || this.getFirstDayOfMonth();
      const to = params['to'] || this.getLastDayOfMonth();

      this.form.setValue({ from, to });

      this.fetchIpoData();
    });  }

  getFirstDayOfMonth(): string {
    const date = new Date();
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-01`;
  }

  getLastDayOfMonth(): string {
    const date = new Date();
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    return lastDay.toISOString().split('T')[0];
  }

  fetchIpoData(): void {
    const { from, to } = this.form.value;
    this.homeService.getIpoCalendar(from, to).subscribe(
      (data: any) => {
        this.ipoCalendar = data?.ipoCalendar || [];
        this.error = this.ipoCalendar.length === 0 ? 'No IPO data available for the selected dates.' : '';
      },
      (err) => {
        this.error = 'Failed to fetch IPO data. Please try again later.';
        console.error(err);
      }
    );
  }

  onSubmit(): void {
    this.fetchIpoData();
  }

  displayedIpoCalendar(): any[] {
    return this.onHome ? this.ipoCalendar.slice(0, 4) : this.ipoCalendar;
  }

  showMore(): void {
    const { from, to } = this.form.value;
    this.router.navigate(['/ipo-calender'], {
      queryParams: { from, to },
    });
  }  
}
