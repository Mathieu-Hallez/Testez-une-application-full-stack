import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  const pathService = 'api/session'

  let service: SessionApiService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  let sessionMock : Session;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);

    sessionMock = {
      id: 1,
      name: 'sessionName',
      description: 'description',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
  });

  afterEach(() => {
      // After every test, assert that there are no more pending requests.
      httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call delete endpoint', () => {
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne(pathService+'/1');

    req.flush(null);

    expect(req.request.method).toBe('DELETE');
  });

  it('should call create and return a Session create', () => {
    service.create(sessionMock).subscribe(session => {
      expect(session.name).toBe('sessionName');
    });

    const req = httpTestingController.expectOne(pathService);

    req.flush(sessionMock);
    expect(req.request.method).toBe('POST');
  });

  it('should call update and return Session update', () => {
    service.update('1', sessionMock).subscribe(session => {
      expect(session.name).toBe('sessionName');
    });

    const req = httpTestingController.expectOne(pathService+'/1');
    
    req.flush(sessionMock);

    expect(req.request.method).toBe('PUT');
  });

  it('should call participate', () => {
    service.participate('1', '1').subscribe();

    const req = httpTestingController.expectOne(pathService+'/1/participate/1');

    req.flush(null);

    expect(req.request.method).toBe('POST');
  });

  it('should call unparticipate', () => {
    service.unParticipate('1', '1').subscribe();

    const req = httpTestingController.expectOne(pathService+'/1/participate/1');

    req.flush(null);

    expect(req.request.method).toBe('DELETE');
  });

});
