import { TestBed } from '@angular/core/testing';

import { sellerGuard } from 'src/app/security/seller.guard';

describe('SellerGuard', () => {
  let guard = sellerGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(sellerGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
