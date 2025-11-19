export type UserResponse = {
  id: number;
  displayName: string;
  username: string;
  bio: string;
  questionHelper: string;
  allowAnonQuestions: boolean;
}

export type UserProfile = {
  id: number;
  icon: string;
  displayName: string;
  username: string;
  bio: string;
  questionHelper: string;
  createdAt: string;
  followingCount: number;
  followersCount: number;
  answersCount: number;
  allowAnonQuestions: boolean;
  isFollowing: boolean;
}

export type UserBasic = {
  id: number;
  displayName: string;
  username: string;
}
