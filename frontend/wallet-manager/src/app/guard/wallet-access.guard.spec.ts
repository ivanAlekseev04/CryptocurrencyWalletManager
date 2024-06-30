import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { walletAccessGuard } from './wallet-access.guard';

describe('walletAccessGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => walletAccessGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
