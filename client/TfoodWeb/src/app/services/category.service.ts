import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from 'src/app/interfaces/page';
import { Category } from 'src/app/models/category';
import { environment } from 'src/app/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private baseUrl = environment.baseUrl + "/categories";
  constructor(private http: HttpClient) { }

  getCategories(page: number, searchTerm?: string): Observable<Page<Category>> {
    let params = new HttpParams()
      .set('page', page);
    if (searchTerm) {
      params = params.set('name', searchTerm);
    }
    return this.http.get<Page<Category>>(`${this.baseUrl}/search`, { params, withCredentials: true });
  }

  createCategory(name: String): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/add`, name, {
      withCredentials: true, responseType: 'text' as 'json'
    });
  }

  updateCategory(id: number, name: String): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/update/${id}`, name, {
      withCredentials: true, responseType: 'text' as 'json'
    });
  }

  deleteCategory(id: number): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/delete/${id}`, null, {
      withCredentials: true, responseType: 'text' as 'json'
    });
  }
}
