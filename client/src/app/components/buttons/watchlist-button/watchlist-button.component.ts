import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { catchError, forkJoin, of, Subscription, switchMap, tap } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Stock, WatchlistRequestDto } from '../../../models/stock.interface';
import { WatchlistService } from '../../../services/watchlist.service';
import { WatchlistDialogComponent } from '../watchlist-dialog/watchlist-dialog.component';

@Component({
  selector: 'app-watchlist-button',
  templateUrl: './watchlist-button.component.html',
  styleUrl: './watchlist-button.component.css',
})
export class WatchlistButtonComponent implements OnInit, OnDestroy {
  @Input() stock!: Stock;

  groupNames: string[] = [];
  newGroupName = '';
  isInWatchlist = false;
  loading = false;
  error = '';
  currentGroupName = '';
  groupsWithStock: string[] = [] ;
  private subscription: Subscription = new Subscription();

  constructor(
    private watchlistService: WatchlistService,
    private dialog: MatDialog,
    private toastr : ToastrService
  ) {}

  ngOnInit() {
    this.checkWatchlistStatus();
    this.subscription.add(
      this.watchlistService.watchlistStocks$.subscribe(() => {
        this.checkWatchlistStatus();
      })
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  checkWatchlistStatus() {
    this.isInWatchlist = this.watchlistService.isStockInWatchlist(
      this.stock.symbol
    );
  }

  openWatchlistDialog() {
    this.loading = true;
    this.error = '';
  
    // Fetch user's watchlist group names (always needed)
    const groupNames$ = this.watchlistService.getGroupNames().pipe(
      tap((names) => (this.groupNames = names))
    );
  
    // Handle isInWatchlist logic
    let stockRequests$;
    if (this.isInWatchlist) {
      // Fetch stockId if missing
      const stockId$ = !this.stock.stockId
        ? this.watchlistService.getStockBySymbol(this.stock.symbol).pipe(
            tap((response) => (this.stock.stockId = response.stockId)),
            catchError(() => {
              this.error = 'Error finding the stock ID';
              return of(null); // Return an empty observable to avoid breaking the flow
            })
          )
        : of(null); // If stockId exists, no need to fetch it
  
      // Fetch groups containing the stock
      const groupsWithStock$ = stockId$.pipe(
        switchMap(() => {
          if (this.stock.stockId) {
            return this.watchlistService.getGroupNamesByStock(this.stock.stockId).pipe(
              tap((response) => (this.groupsWithStock = response)),
              catchError((err) => {
                this.error += '\nCould not get groups associated with the stock.\n' + err;
                return of(null); // Return an empty observable
              })
            );
          }
          return of(null);
        })
      );
  
      // Combine stock-related requests
      stockRequests$ = forkJoin([stockId$, groupsWithStock$]);
    } else {
      stockRequests$ = of(null); // If not in watchlist, skip stock-related requests
    }
  
    // Combine all requests
    forkJoin([groupNames$, stockRequests$]).subscribe({
      next: () => {
        this.loading = false;
        this.openDialog(); // Open dialog after all requests are handled
      },
      error: (err) => {
        this.error = 'An error occurred while loading watchlist information.\n' + err.error.message;
        console.log("simran threw this error\n"+ JSON.stringify(err));
        this.loading = false;
        this.openDialog();
      },
    });
  }
  

  private openDialog() {
    const dialogRef = this.dialog.open(WatchlistDialogComponent, {
      width: '400px',
      data: {
        groupNames: this.groupNames,
        loading: this.loading,
        error: this.error,
        newGroupName: '',
        stock: this.stock,
        groupsWithStock: this.groupsWithStock,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!result) return;

      if (result.action === 'add') {
        this.addToWatchlist(result.groupName);
      } else if (result.action === 'create') {
        const request: WatchlistRequestDto = {
          stock: this.stock,
          group: { groupName: result.groupName },
        };

        this.watchlistService.createWatchlistEntry(request).subscribe({
          next: (response) => {
            console.log('Created new watchlist entry:', response);
            this.watchlistService.refreshWatchlist();
            this.toastr.success("Group created successfully and stock added to \""+result.groupName+"\"");
          },
          error: (err) => {
            console.error('Error creating watchlist entry:', err);
          },
        });
      } else if (result.action === 'remove') {
        this.currentGroupName = result.groupName;
        this.removeFromCurrentGroup();
        this.watchlistService.refreshWatchlist();
      }
    });
  }

  addToWatchlist(groupName: string) {
    this.loading = true;
    this.error = '';

    this.watchlistService.addStocksToGroup(groupName, [this.stock]).subscribe({
      next: () => {
        this.loading = false;
        this.watchlistService.refreshWatchlist();
        this.currentGroupName = groupName;
        this.toastr.success("Added successfully to \""+this.currentGroupName+"\"");
      },
      error: (err) => {
        this.error = 'Failed to add to watchlist';
        this.loading = false;
        console.error('Error adding to watchlist:', err);
        this.toastr.error("Error adding to \""+this.currentGroupName+"\".Please try again later.");
      },
    });
  }

  removeFromCurrentGroup() {
    this.watchlistService
      .removeStocksFromGroup(this.currentGroupName, [this.stock])
      .subscribe({
        next: () => {
          this.watchlistService.refreshWatchlist();
          this.groupsWithStock = null;
          this.stock.stockId = null;
          this.toastr.success("Removed successfully from \""+this.currentGroupName+"\"");
        },
        error: (err) => {
          console.error('Error removing from watchlist:', err);
          this.toastr.error("Error removing from \""+this.currentGroupName+"\".Please try again later.");
        },
      });
  }
}
