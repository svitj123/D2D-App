import { Component } from '@angular/core';
import * as L from 'leaflet';
import * as XLSX from 'xlsx';

interface Address {
  ulica: string;
  hisnaStevilka: string;
  dodatek: string;
  obcina: string;
  tehnologija: string;
  postnaStevilka?: string;
  lat?: number;
  lng?: number;
}

@Component({
  selector: 'app-map-view',
  standalone: true,
  imports: [],
  templateUrl: './map-view.component.html',
  styleUrl: './map-view.component.css'
})
export class MapViewComponent {
  map!: L.Map;
  selectedFile: File | null = null;
  addresses: Address[] = [];

  ngAfterViewInit(): void {
    this.map = L.map('map').setView([46.0569, 14.5058], 11); // Ljubljana
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onUpload(): void {
    if (!this.selectedFile) return;
  
    const reader = new FileReader();
    reader.onload = async (e: any) => {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });
      const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
      const jsonData = XLSX.utils.sheet_to_json<any>(firstSheet);
  
      this.addresses = jsonData.map((row: any) => {
        const obcinaNaziv = (row.OBCINA_NAZIV || '').toString().trim();
        const match = obcinaNaziv.match(/^(\d{4})\s+(.*)$/);
        const postnaStevilka = match ? match[1] : '';
        const obcina = match ? match[2] : obcinaNaziv;
  
        return {
          ulica: (row.ULICA || '').toString().trim(),
          hisnaStevilka: (row.HS_STEVILKA || '').toString().trim(),
          dodatek: (row.HS_DODATEK || '').toString().trim(),
          obcina: obcina.trim(),
          tehnologija: (row.Tehnologija || '').toString().trim(),
          postnaStevilka
        };
      });
  
      console.log('Parsed addresses:', this.addresses);
      await this.geocodeAndAddMarkers();
    };
  
    reader.readAsArrayBuffer(this.selectedFile);
  }

  async geocodeAndAddMarkers(): Promise<void> {
    for (const [index, address] of this.addresses.entries()) {
      const postnaStevilka = address.postnaStevilka ?? '';
      const fullAddress = `${address.ulica} ${address.hisnaStevilka}${address.dodatek}, ${postnaStevilka} ${address.obcina}, Slovenija`;
  
      console.log(`[${index}] Geokodiram: ${fullAddress}`);
  
      const location = await this.geocodeAddress(fullAddress);
  
      if (location) {
        address.lat = location.lat;
        address.lng = location.lon;
  
        const color = this.getMarkerColor(address.tehnologija);
        const marker = L.circleMarker([address.lat, address.lng], {
          radius: 8,
          color: color,
          fillColor: color,
          fillOpacity: 0.8
        }).addTo(this.map);
  
        marker.bindPopup(`
          <b>${address.ulica} ${address.hisnaStevilka}${address.dodatek}</b><br>
          ${address.postnaStevilka} ${address.obcina}<br>
          ${address.tehnologija}
        `);
      } else {
        console.warn(`[${index}] Geokodiranje ni našlo rezultata za: "${fullAddress}"`);
      }
  
      await this.sleep(1000); // delay med poizvedbami
    }
  }
  
  async geocodeAddress(address: string): Promise<{ lat: number, lon: number } | null> {
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}&limit=1`;
  
    try {
      const res = await fetch(url, {
        headers: {
          'User-Agent': 'TelemachTrznikApp/1.0 (kontakt@example.com)' // <- zamenjaj z realnim emailom za odgovorno rabo
        }
      });
      const data = await res.json();
  
      if (data.length > 0) {
        return {
          lat: parseFloat(data[0].lat),
          lon: parseFloat(data[0].lon)
        };
      } else {
        console.warn('Geokodiranje ni našlo rezultata za:', address);
        return null;
      }
    } catch (err) {
      console.error('Napaka pri fetch geokodiranju:', err);
      return null;
    }
  }

  sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  getMarkerColor(tehnologija: string): string {
    const tech = tehnologija?.toLowerCase() || '';
    if (tech.includes('potrebna izgradnja')) return 'red';
    if (tech.includes('wla vula')) return 'green';
    return 'blue';
  }
}