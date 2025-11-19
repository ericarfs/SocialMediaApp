import { Component } from '@angular/core';
import { Layout } from '../../layout/layout';
import { UserInfo } from '../user-info/user-info';
import { Posts } from '../posts/posts';

@Component({
  selector: 'app-profile-layout',
  imports: [Layout, UserInfo, Posts],
  templateUrl: './profile-layout.html',
  styleUrl: './profile-layout.scss'
})
export class ProfileLayout {

}
