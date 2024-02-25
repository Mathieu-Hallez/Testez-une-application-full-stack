import '../support/commands';

describe('Edit session spec', () => {
    const session = {
        id: 1,
        name: 'session 1',
        description: 'description 1',
        date: new Date('2024-02-02'),
        teacher_id: 1,
        users: [],
        createdAt: new Date('2024-01-01'),
        updatedAt: new Date('2024-01-01'),
    };

    beforeEach(() => {
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            [session]
        ).as('session');
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
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
            },
            session
        );

        cy.login('yoga@studio.com', 'test!1234', 'firstname', 'lastname', true);

        cy.get('button').contains('Edit').click();
        cy.url().should('include','update');
    });

    it('Edit session name and description', () => {
        cy.intercept(
            {
                method: 'PUT',
                url: '/api/session/1'
            },
            {
                ...session,
                name: 'Session rename',
                description: 'new description'
            }
        ).as('updateSubmit');

        cy.get('h1').contains('Update session');

        cy.get('input[formControlName=name]').clear().type('Session rename');
        cy.get('textarea[formControlName=description]').clear().type(`new description`);
        cy.get('button').contains('Save').click();

        cy.wait('@updateSubmit').then((interception) => {
            cy.log(JSON.stringify(interception.request.body));
            expect(interception.request.body.name).to.eq('Session rename');
            expect(interception.request.body.description).to.eq('new description');
            expect(interception.request.body.teacher_id).to.eq(1);
        })

        cy.url().should('include', '/sessions');
        cy.contains('Session updated !');
    });

    it('Disable submit button when a input is empty', () => {
        cy.get('input[formControlName=name]').clear().type('{enter}');

        cy.contains('Save').should('be.disabled');
    });
});