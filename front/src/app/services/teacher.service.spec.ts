import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

describe('TeacherService', () => {
  const pathService = 'api/teacher';

  let service: TeacherService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
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

  it('should call detail and return Teacher', () => {
    service.detail('1').subscribe(teacher => {
      expect(teacher.firstName).toBe('firstname');
    });

    const req = httpTestingController.expectOne(pathService+'/1');

    req.flush({
      id: 1,
      lastName: 'lastname',
      firstName: 'firstname',
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    expect(req.request.method).toBe('GET');
  });
});
