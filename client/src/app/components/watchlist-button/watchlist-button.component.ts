import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Stock, WatchlistRequestDto } from '../../models/stock.interface';
import { MatDialog } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { WatchlistService } from '../../services/watchlist.service';
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
    private dialog: MatDialog
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

    //get group names associated with the user
    this.watchlistService.getGroupNames().subscribe({
      next: (names) => {
        this.groupNames = names;
        this.loading = false;
        this.openDialog();
      },
      error: (err) => {
        this.error = 'Failed to load watchlist groups';
        this.loading = false;
        console.error('Error loading groups:', err);
        this.openDialog();
      },
    });

    // only do this step if isInWatchlist
    if (this.isInWatchlist) {
      //if stock doesnt have stockId i.e. from the search not the watchlist ; then get stock id first
      if (!this.stock.stockId) {
        this.watchlistService.getStockBySymbol(this.stock.symbol).subscribe({
          next: (response) => {
            console.log("response recieved in getStockBySymbol in watchlist-button is " + response);
            this.stock.stockId = response.stockId;
          },
          error: () => {
            console.log('Error in finidng the stock in a group');
            this.error = 'Error in finidng the stock in a group';
          },
        });
      }
      //get groups that contain this stock
      this.watchlistService.getGroupNamesByStock(this.stock.stockId).subscribe({
        next: (response) =>{
          this.groupsWithStock = response;
        },
        error: (err) =>{
          this.error = this.error + "\nCould not get groups associated with a stock.\n"+ err;
        }
      })
    }
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
      },
      error: (err) => {
        this.error = 'Failed to add to watchlist';
        this.loading = false;
        console.error('Error adding to watchlist:', err);
      },
    });
  }

  removeFromCurrentGroup() {
    this.watchlistService
      .removeStocksFromGroup(this.currentGroupName, [this.stock])
      .subscribe({
        next: () => {
          this.watchlistService.refreshWatchlist();
        },
        error: (err) => {
          console.error('Error removing from watchlist:', err);
        },
      });
  }
}
