import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should logIn a new SessionInformation', () => {
    service.$isLogged().subscribe(isLogged => expect(isLogged).toBe(service.isLogged));

    service.logIn({
      token: 'token',
      type: 'userSession',
      id: 1,
      username: 'username',
      firstName: 'firstname',
      lastName: 'lastname',
      admin: false
    });

    expect(service.sessionInformation?.username).toBe('username');
  });

  it('should logOut a SessionInformation', () => {
    service.isLogged = true;
    service.$isLogged().subscribe(isLogged => expect(isLogged).toBe(service.isLogged));

    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
  });
});
