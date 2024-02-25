describe('Register spec', () => {

  beforeEach(() => {
    cy.visit('/register');
  });

  it('Register successfull', () => {

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        "message": "User registered successfully!"
      },
    });

    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type('toto3@toto.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);

    cy.url().should('include', '/login');
  });

  it('Register failed', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        "path": "/api/auth/register",
        "error": "Bad request",
        "message": "Bad credentials",
        "status": 400
      }
    });

    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

    cy.contains('An error occurred');
  });

  it('Empty Input', () => {
    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    const emailInput = cy.get('input[formControlName=email]');
    emailInput.type("{enter}");
    
    cy.get('input[formControlName=password]').type(`${"test!1234"}`);
    cy.get('button').should('be.disabled');
  });
})