import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface AddressDto {
  ulica: string;
  hisnaStevilka: string;
  dodatek: string;
  obcina: string;
  tehnologija: string;
}

@Component({
  selector: 'app-address-upload',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './address-upload.component.html',
  styleUrl: './address-upload.component.css'
})
export class AddressUploadComponent {
  selectedFile: File | null = null;
  addresses: AddressDto[] = [];
  uploadInProgress = false;
  uploadSuccess = false;
  uploadError = '';

  constructor(private http: HttpClient) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.uploadSuccess = false;
      this.uploadError = '';
    }
  }

  onUpload(): void {
    if (!this.selectedFile) return;

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.uploadInProgress = true;
    this.http.post<AddressDto[]>('http://localhost:8080/api/upload', formData).subscribe({
      next: (data) => {
        this.addresses = data;
        this.uploadSuccess = true;
        this.uploadError = '';
      },
      error: (err) => {
        console.error('Napaka pri nalaganju:', err);
        this.uploadError = 'Napaka pri nalaganju datoteke.';
        this.uploadSuccess = false;
      },
      complete: () => {
        this.uploadInProgress = false;
      }
    });
  }
}