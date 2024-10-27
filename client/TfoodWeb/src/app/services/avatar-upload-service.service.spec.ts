import { TestBed } from '@angular/core/testing';

import { AvatarUploadServiceService } from './avatar-upload-service.service';

describe('AvatarUploadServiceService', () => {
  let service: AvatarUploadServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AvatarUploadServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
