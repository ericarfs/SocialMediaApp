export type UserResponse = {
  id: number;
  displayName: string;
  username: string;
  bio: string;
  questionHelper: string;
  allowAnonQuestions: boolean;
}

export type UserBasic = {
  id: number;
  displayName: string;
  username: string;
}
