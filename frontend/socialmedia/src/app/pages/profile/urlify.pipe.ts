import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({ name: 'urlify' })
export class UrlifyPipe implements PipeTransform {
  constructor(private sanitizer: DomSanitizer) {}

  transform(text: string | null | undefined): SafeHtml {
    if (!text) return '';

    const urlRegex = /(https?:\/\/[^\s]+)/g;

    const html = text.replace(urlRegex, (url) => {
      return `<a class="text-blue-600 hover:text-blue-800 hover:underline" href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>`;
    });

    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
