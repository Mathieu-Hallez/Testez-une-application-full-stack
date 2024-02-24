import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let httpTestingController : HttpTestingController;
  let matSnackBar : MatSnackBar;
  let router : Router;
  let sessionApiService : SessionApiService;

  let deleteButton : DebugElement;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  const session : Session = {
    id: 1,
    name: 'session',
    description: 'description',
    date: new Date('1995-12-17'),
    teacher_id: 1,
    users: [1],
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  const teacher : Teacher = {
    id: 1,
    lastName: 'lastname',
    firstName: 'firstname',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        HttpClientTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();
    
    service = TestBed.inject(SessionService);
    httpTestingController = TestBed.inject(HttpTestingController);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.sessionId = '1';
    fixture.detectChanges();

    httpTestingController.expectOne('api/session/1').flush(session);
    httpTestingController.expectOne('api/teacher/1').flush(teacher);

    fixture.detectChanges();

    deleteButton = fixture.debugElement.query(By.css('button[id="deleteBtn"]'));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display delete button and call delete methods successfully', async () => {
    jest.spyOn(matSnackBar, 'open').mockImplementation();
    jest.spyOn(router, 'navigate').mockImplementation();

    expect(component.isAdmin).toBeTruthy();
    expect(component.session?.name).toBe('session');
    expect(component.teacher?.firstName).toBe('firstname');
    expect(deleteButton).toBeTruthy();

    deleteButton.triggerEventHandler('click', null);
    await fixture.whenStable();
    fixture.detectChanges();

    const deleteReq = httpTestingController.expectOne('api/session/1');
    deleteReq.flush(null);

    expect(deleteReq.request.method).toBe('DELETE');
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !',"Close", {"duration": 3000});
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate to the session', async() => {

    component.isAdmin = false;
    component.isParticipate = false;
    fixture.detectChanges();

    const participationButton = fixture.debugElement.query(By.css('#participationBtn'));
    participationButton.triggerEventHandler('click', null);
    
    await fixture.whenStable();
    fixture.detectChanges();

    const participateReq = httpTestingController.expectOne('api/session/1/participate/1');
    participateReq.flush(null);

    expect(participateReq.request.method).toBe('POST');

    httpTestingController.expectOne('api/session/1').flush(session);
    httpTestingController.expectOne('api/teacher/1').flush(teacher);

    expect(component.isParticipate).toBeTruthy();
  });

  it('should unparticipate to the session', async() => {
    component.isAdmin = false;
    component.isParticipate = true;
    fixture.detectChanges();

    const participationButton = fixture.debugElement.query(By.css('#participationBtn'));
    participationButton.triggerEventHandler('click', null);
    
    await fixture.whenStable();
    fixture.detectChanges();

    const participateReq = httpTestingController.expectOne('api/session/1/participate/1');
    participateReq.flush(null);

    expect(participateReq.request.method).toBe('DELETE');

    httpTestingController.expectOne('api/session/1').flush({...session, users:[]});
    httpTestingController.expectOne('api/teacher/1').flush(teacher);

    expect(component.isParticipate).toBeFalsy();

  });
});

