import { Component} from '@angular/core';
import { Layout } from '../layout/layout';
import { FeedTemplate } from '../feed-template/feed-template';


@Component({
  selector: 'app-home',
  imports: [Layout,
    FeedTemplate
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home {
  baseUrl = 'http://localhost:8080/api/users/me/feed';
  noContentMessage = "There's nothing to see here.";
  emptyHintMessage = "Follow some accounts to fill your feed!";
}
