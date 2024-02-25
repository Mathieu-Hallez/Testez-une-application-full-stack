import '../support/commands';

describe('Session creation spec', () => {

    const session = {
        "id": 1,
        "name": "Session 1",
        "date": "2024-02-25",
        "teacher_id": 1,
        "description": "description",
        "users": [],
        "createdAt": "2024-02-25",
        "updatedAt": "2024-02-25"
    }

    beforeEach(() => {
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            []
        );
        cy.intercept(
            {
                method: 'GET',
                url: '/api/teacher'
            },
            [
                {
                    id: 1,
                    lastName: 'lastname',
                    firstName: 'firstname',
                    createdAt: new Date('2024-01-01'),
                    updatedAt: new Date('2024-01-01'),
                }
            ]
        );

        cy.login('yoga@studio.com', 'test!1234', 'firstname', 'lastname', true);

        cy.get('button').contains('Create').click();
        cy.url().should('include','create');
    });

    it('Create a session', () => {
        cy.intercept(
            {
                method: 'POST',
                url: '/api/session'
            },
            {
                statusCode: 200,
                body: session
            }
        );

        cy.get('h1').contains('Create session');

        cy.get('input[formControlName=name]').type(session.name);
        cy.get('input[formControlName=date]').type(session.date);
        cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('firstname lastname').click();
        cy.get('textarea[formControlName=description]').type(`${session.description}`);
        cy.get('button').contains('Save').click();

        cy.url().should('include', '/sessions');
        cy.contains('Session created !');
    });

    it('Disable submit button when a input is empty', () => {
        cy.get('input[formControlName=name]').type('{enter}');
        cy.get('input[formControlName=date]').type(session.date);
        cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('firstname lastname').click();
        cy.get('textarea[formControlName=description]').type(`${session.description}`);

        cy.contains('Save').should('be.disabled');
    });
});