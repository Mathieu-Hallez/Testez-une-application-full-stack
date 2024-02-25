import '../support/commands';

describe('Session detail spec', () => {
    beforeEach(() => {
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
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            [
                session
            ]
        ).as('session');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
            },
            session
        );

        cy.intercept(
            {
                method: 'GET',
                url: '/api/teacher/1'
            },
            {
                id: 1,
                lastName: 'lastname',
                firstName: 'firstname',
                createdAt: new Date('2024-01-01'),
                updatedAt: new Date('2024-01-01'),
            }
        );

        cy.login('yoga@studio.com', 'test!1234', 'firstname', 'lastname', true);
        cy.get('button').contains('Detail').click();

        cy.url().should('include','detail/1');
    });

    it('Display session detail', () => {
        cy.get('h1').contains('Session 1');
        cy.contains('firstname LASTNAME');
        cy.contains('0 attendees');
        cy.contains('description');
    });

    it('Display session button and delete session', () => {
        cy.intercept(
            {
                method: 'DELETE',
                url: '/api/session/1'
            },
            {
                statusCode: 200
            }
        );
        cy.get('button').contains('Delete').click();

        cy.url().should('include', '/sessions');
        cy.contains('Session deleted !');
    });
});