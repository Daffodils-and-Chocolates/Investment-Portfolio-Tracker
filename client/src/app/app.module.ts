import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component'; 
import { SignupComponent } from './components/auth/signup/signup.component';  
import { FormsModule } from '@angular/forms';
import { WatchlistComponent } from './components/watchlist/watchlist.component';
import { HomeComponent } from './pages/home/home.component';
import { HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './components/navbar/navbar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { ManageAccountComponent } from './components/manage-account/manage-account.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SearchBarComponent } from './components/search-bar/search-bar.component';
import { SymbolResultsComponent } from './components/symbol-results/symbol-results.component';
import { CompanyProfileComponent } from './components/company-profile/company-profile.component';
import { CryptoResultsComponent } from './components/crypto-results/crypto-results.component';
import { WatchlistButtonComponent } from './components/watchlist-button/watchlist-button.component';
import { WatchlistDialogComponent } from './components/watchlist-dialog/watchlist-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    WatchlistComponent,
    HomeComponent,
    NavbarComponent,
    ManageAccountComponent,
    SearchBarComponent,
    SymbolResultsComponent,
    CompanyProfileComponent,
    CryptoResultsComponent,
    WatchlistButtonComponent,
    WatchlistDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot({
      timeOut: 3000,  // Duration for each toast
      positionClass: 'toast-top-right',
      preventDuplicates: true, 
      closeButton: true,    
      progressBar: true
    }),
    MatDialogModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }