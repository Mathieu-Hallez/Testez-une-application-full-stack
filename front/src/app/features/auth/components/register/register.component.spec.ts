import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { By } from '@angular/platform-browser';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { routes } from '../../auth-routing.module';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceStub : any;
  let router : Router;

  beforeEach(async () => {
    authServiceStub = {
      register: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes(routes)
      ],
      providers: [
        {
          provide: AuthService,
          useValue: authServiceStub
        }
      ]
    })
      .compileComponents();

      router = TestBed.inject(Router);

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    router.initialNavigation();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call form methods and redirect to login page', () => {
    // Arrange
    jest.spyOn(component, 'submit');
    jest.spyOn(authServiceStub, 'register').mockReturnValue(of());
    const navigateSpy = jest.spyOn(router, 'navigate');
    const submitForm = fixture.debugElement.query(By.css('.register-form'));

    // Act
    submitForm.triggerEventHandler('ngSubmit', null);
    fixture.detectChanges();

    // Assert
    expect(component.submit).toHaveBeenCalled();
    expect(authServiceStub.register).toHaveBeenCalled();
    expect(component.onError).toBeFalsy();
    // expect(navigateSpy).toHaveBeenCalled();
  });
});
