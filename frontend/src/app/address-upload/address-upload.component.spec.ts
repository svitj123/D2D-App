import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressUploadComponent } from './address-upload.component';

describe('AddressUploadComponent', () => {
  let component: AddressUploadComponent;
  let fixture: ComponentFixture<AddressUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressUploadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
