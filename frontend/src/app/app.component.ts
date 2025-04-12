import { Component } from '@angular/core';
import { AddressUploadComponent } from './address-upload/address-upload.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AddressUploadComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}