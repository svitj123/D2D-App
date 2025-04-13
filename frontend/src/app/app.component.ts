import { Component } from '@angular/core';
import { AddressUploadComponent } from './address-upload/address-upload.component';
import { MapViewComponent } from './map-view/map-view.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AddressUploadComponent, MapViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}