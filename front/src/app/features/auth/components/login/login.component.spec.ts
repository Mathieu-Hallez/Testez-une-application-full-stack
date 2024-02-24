import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInput, MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixtureLogin: ComponentFixture<LoginComponent>;

  let router : Router;
  let sessionService : SessionService;
  let authService : AuthService;

  let emailInput : HTMLInputElement;
  let passwordInput : HTMLInputElement;
  let loginForm : DebugElement;

  let httpTestingController: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);

    fixtureLogin = TestBed.createComponent(LoginComponent);
    component = fixtureLogin.componentInstance;
    fixtureLogin.detectChanges();

    emailInput = fixtureLogin.nativeElement.querySelector('#email');
    passwordInput = fixtureLogin.nativeElement.querySelector('#password');
    loginForm = fixtureLogin.debugElement.query(By.css('.login-form'));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should successfull login a session and navigate to sessions route', async () => {
    jest.spyOn(router, 'navigate').mockImplementation();

    emailInput.value = 'test@test.com';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = 'test!123';
    passwordInput.dispatchEvent(new Event('input'));

    loginForm.triggerEventHandler('ngSubmit', null);
    await fixtureLogin.whenStable();
    fixtureLogin.detectChanges();

    const req = httpTestingController.expectOne('api/auth/login');

    req.flush({
      token: 'token',
      type: 'userSession',
      id: 1,
      username: 'username',
      firstName: 'firstname',
      lastName: 'lastname',
      admin: false
    });

    expect(component.form.valid).toBeTruthy();
    expect(sessionService.isLogged).toBeTruthy();
    expect(sessionService.sessionInformation?.token).toBe('token');
    expect(component.onError).toBeFalsy();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);

    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should unsuccessfull login due to invalid inputs', async () => {
    emailInput.value = 'testtest.com';
    emailInput.dispatchEvent(new Event('input'));
    passwordInput.value = '1';
    passwordInput.dispatchEvent(new Event('input'));

    loginForm.triggerEventHandler('ngSubmit', null);
    await fixtureLogin.whenStable();
    fixtureLogin.detectChanges();

    expect(component.form.controls['email'].valid).toBeFalsy();
    expect(component.form.controls['password'].valid).toBeFalsy();
  });

  it('should throw an error for empty mandatory input', () => {
    component.form.setValue({email: 'test@test.com', password: '123456'});

    component.submit();
    fixtureLogin.detectChanges();
    
    const req = httpTestingController.expectOne('api/auth/login');

    req.error(new ProgressEvent('Login error'));

    expect(component.onError).toBeTruthy();

    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });
});
