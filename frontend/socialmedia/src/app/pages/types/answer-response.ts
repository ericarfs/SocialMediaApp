import { QuestionResponse } from "./question-response";
import { UserBasic } from "./user-response";

export type LikeResponse = {
  likesCount: number;
  hasUserLiked: boolean;
}

export type ShareResponse = {
  sharesCount: number;
  hasUserShared: boolean;
}

export type AnswerBasic = {
  id: number;
  question: QuestionResponse;
  body: string;
  author: UserBasic;
  timeCreation: string;
  inResponseTo: AnswerBasic | null;
}

export type AnswerResponse = {
  id: number;
  question: QuestionResponse;
  body: string;
  author: UserBasic;
  timeCreation: string;
  likesInfo: LikeResponse;
  sharesInfo: ShareResponse;
  inResponseTo: AnswerBasic | null;
}
