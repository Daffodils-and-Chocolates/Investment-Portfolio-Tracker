import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GetUserDto, UpdateUserRequestDto } from '../dto/dtoAll';

@Injectable({
  providedIn: 'root'
})
export class ManageAccountService {
  private apiUrl = 'http://localhost:5000/api/users/me';

  constructor(private http: HttpClient) {}

  getUserData(): Observable<GetUserDto> {
    return this.http.get<GetUserDto>(`${this.apiUrl}`);
  }

  updateUserData(userUpdateRequestDto: UpdateUserRequestDto): Observable<GetUserDto> {
    return this.http.put<GetUserDto>(`${this.apiUrl}`, userUpdateRequestDto);
  }
}
