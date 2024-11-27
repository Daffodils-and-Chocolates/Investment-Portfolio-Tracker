import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Stock } from '../../models/stock.interface';

@Component({
  selector: 'app-watchlist-dialog',
  templateUrl: './watchlist-dialog.component.html',
  styleUrl: './watchlist-dialog.component.css'
})
export class WatchlistDialogComponent {
  isModalVisible: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<WatchlistDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      groupNames: string[];
      loading: boolean;
      error: string;
      newGroupName: string;
      stock: Stock;
      groupsWithStock:string[];
    }
  ) {
    this.isModalVisible = true;
    console.log("in watchlist Dialog groupsWithStock : "+this.data.groupsWithStock);
  }

  onClose(): void {
    this.dialogRef.close();
    this.toggleModal();  
  }

  onAddToGroup(groupName: string): void {
    this.dialogRef.close({ action: 'add', groupName });
  }

  onRemoveFromGroup(groupName: string) : void{
    this.dialogRef.close({ action: 'remove',groupName })
  }

  onCreateGroup(): void {
    if (this.data.newGroupName.trim()) {
      this.dialogRef.close({
        action: 'create',
        groupName: this.data.newGroupName.trim()
      });
    }
  }

  toggleModal(): void {
    this.isModalVisible = !this.isModalVisible;
  }
}