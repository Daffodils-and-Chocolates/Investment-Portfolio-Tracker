import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { WatchlistComponent } from './components/watchlist/watchlist.component';
import { AuthGuard } from './guards/auth.guard'  // Import the AuthGuard
import { HomeComponent } from './pages/home/home.component';
import { ManageAccountComponent } from './components/manage-account/manage-account.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'watchlist', component: WatchlistComponent, canActivate: [AuthGuard] },
  { path: 'manage-account', component: ManageAccountComponent, canActivate: [AuthGuard] },
  // { path: 'portfolio', component: PortfolioComponent, canActivate: [AuthGuard] }, 
  { path: '**', redirectTo: '/home' }  // Redirect any unknown paths to home
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
