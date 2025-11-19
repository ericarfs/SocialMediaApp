import { HttpClient } from "@angular/common/http";
import { computed, Injectable, signal } from "@angular/core";
import { UserProfile, UserResponse } from "../../types/user-response";
import { tap } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService{
  private baseUrl = 'http://localhost:8080/api';

  loadedUser = signal<UserProfile | null > (null);
  readonly loadedUserSignal = this.loadedUser.asReadonly();

  private followingList = signal<UserResponse[]>([]);
  private followersList = signal<UserResponse[]>([]);

  followingList$ = computed(() => this.followingList());
  followersList$ = computed(() => this.followersList());

  constructor(private http: HttpClient) {}

  getUserInfo(username: string){
    return this.http.get<UserProfile>(`${this.baseUrl}/users/${username}/profile`).pipe(
      tap((res) => {
        this.loadedUser.set(res);
      })
    )
  }

  getFollowersList(username: string){

  }

  unfollow(username: string){
    return this.http.delete(`${this.baseUrl}/users/me/follows/${username}`).pipe(
      tap(() => {
        this.loadedUser.update(current => {
          if (!current) return null;

          const newCount = Math.max(0, current.followersCount - 1);

          return {
            ...current,
            isFollowing: false,
            followersCount: newCount
          }
        })
      })
    )
  }

  follow(username: string){
    return this.http.post(`${this.baseUrl}/users/me/follows/${username}`, {}).pipe(
      tap(() => {
        this.loadedUser.update(current => {
          if (!current) return null;

          const newCount = current.followersCount + 1;

          return {
            ...current,
            isFollowing: true,
            followersCount: newCount
          }
        })
      })
    )
  }

  sendQuestion(username: string, body:string, anon: boolean){
    return this.http.post(`${this.baseUrl}/questions/${username}`, { body, anon });
  }
}
