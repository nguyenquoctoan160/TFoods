import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AvatarUploadServiceService {
  private baseUrl = environment.baseUrl;

  constructor(private http: HttpClient) { }

  uploadAvatar(file: Blob): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, 'avatar.png'); // Name the file 'file' to match backend API

    return this.http.post(`${this.baseUrl}/users/avatar`, formData, { withCredentials: true, responseType: 'text' });
  }
}
