import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Branch } from 'src/app/models/branch';
import { Observable } from 'rxjs/internal/Observable';
import { Page } from 'src/app/interfaces/page';
import { environment } from 'src/app/environment/environment';
import { BranchDTO } from 'src/app/models/branch-dto';
import { ApiResponse } from 'src/app/interfaces/response';

@Injectable({
  providedIn: 'root'
})
export class BranchService {

  private apiUrl = environment.baseUrl + '/branches';


  constructor(private http: HttpClient) { }

  getBranches(page: number, name?: string, address?: string): Observable<Page<Branch>> {
    return this.http.get<Page<Branch>>(`${this.apiUrl}/search`, {
      params: { page: page.toString(), name: name || '', address: address || '' },
      withCredentials: true,
    });
  }

  getBranchesByUserId(page: number): Observable<Page<Branch>> {
    return this.http.get<Page<Branch>>(`${this.apiUrl}/mybranches`, {
      params: { page: page.toString() },
      withCredentials: true,
    });
  }

  addBranch(branch: BranchDTO): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/add`, branch, {
      withCredentials: true, responseType: 'text' as 'json'
    });
  }

  updateBranch(id: number, branch: Branch): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, branch, {
      withCredentials: true, responseType: 'text' as 'json'
    });
  }

  deleteBranch(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`, {
      withCredentials: true, responseType: 'text' as 'json'
    });
  }

}
