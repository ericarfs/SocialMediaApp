import { QuestionResponse } from "./question-response";

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
  author: string;
  timeCreation: string;
  inResponseTo: AnswerBasic | null;
}

export type AnswerResponse = {
  id: number;
  question: QuestionResponse;
  body: string;
  author: string;
  timeCreation: string;
  likesInfo: LikeResponse;
  sharesInfo: ShareResponse;
  inResponseTo: AnswerBasic | null;
}
