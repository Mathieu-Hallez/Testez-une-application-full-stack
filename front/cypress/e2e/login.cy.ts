import '../support/commands';

describe('Login spec', () => {

  beforeEach(() => {
    cy.visit('login');
  });

  afterEach(() => {});

  it('Login successfull', () => {

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

      cy.visit('login');

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'username',
          firstName: 'firstname',
          lastName: 'lastname',
          admin: true
        },
      });
  
      cy.get('input[formControlName=email]').type('yoga@studio.com');
      cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);

    cy.url().should('include', '/sessions');
  });

  it('Login failed', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        "path": "/api/auth/login",
        "error": "Unauthorized",
        "message": "Bad credentials",
        "status": 401
      }
    });

    
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

    cy.contains('An error occurred');
  });

  it('Empty Input', () => {
    const emailInput = cy.get('input[formControlName=email]');
    emailInput.type("{enter}");
    
    cy.get('input[formControlName=password]').type(`${"test!12345"}`);
    cy.get('button').should('be.disabled');
  });
});