import { TestBed, fakeAsync } from "@angular/core/testing";
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from "./auth.service";
import { HttpClient } from "@angular/common/http";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";
import { expect } from '@jest/globals';

describe('AuthService', () => {
    const pathService = 'api/auth';

    let service: AuthService;
    let httpClient: HttpClient;
    let httpTestingController: HttpTestingController;
  
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports:[
          HttpClientTestingModule
        ]
      });
      service = TestBed.inject(AuthService);
      httpClient = TestBed.inject(HttpClient);
      httpTestingController = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        // After every test, assert that there are no more pending requests.
        httpTestingController.verify();
    });
  
    it('should be created', () => {
      expect(service).toBeTruthy();
    });

    it('should call register endpoint', () => {
        service.register({
            email: 'test@test.com',
            firstName: 'firstname',
            lastName: 'lastname',
            password: '123456'
        }).subscribe();

        const req = httpTestingController.expectOne(pathService + '/register');

        req.flush(null);
        expect(req.request.method).toBe('POST');
    });

    it('should call login endpoint and return SessionInformation', () => {
        service.login({
            email: 'test@test.com',
            password: '123456'
        }).subscribe(sessionInformation => {
            expect(sessionInformation.firstName).toBe('firstname');
        });

        const req = httpTestingController.expectOne(pathService + '/login');

        req.flush({
            token: 'token',
            type: 'userSession',
            id: 1,
            username: 'username',
            firstName: 'firstname',
            lastName: 'lastname',
            admin: false
        });
        expect(req.request.method).toBe('POST');
    });
  });