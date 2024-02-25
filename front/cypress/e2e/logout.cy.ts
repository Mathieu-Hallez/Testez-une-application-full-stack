import '../support/commands';

describe('Logout spec', () => {
    it('Logout user session', () => {
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            []
        );
        cy.login('yoga@studio.com', 'test!1234', 'firstname', 'lastname', true);

        cy.contains('Logout').click();
        cy.url().should('include', '/');
        cy.contains('Login');
        cy.contains('Register');
    });
});