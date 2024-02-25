import '../support/commands';

describe('Sessions spec', () => {

    beforeEach(() => {
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            [
                {
                    id: 1,
                    name: 'session 1',
                    description: 'description 1',
                    date: new Date('2024-25-01'),
                    teacher_id: 1,
                    users: [],
                    createdAt: new Date('2024-25-01'),
                    updatedAt: new Date('2024-25-01'),
                },
                {
                    id: 2,
                    name: 'session 2',
                    description: 'description 2',
                    date: new Date('2024-25-02'),
                    teacher_id: 1,
                    users: [],
                    createdAt: new Date('2024-25-02'),
                    updatedAt: new Date('2024-25-02'),
                }
            ]
        ).as('session');

        cy.login('yoga@studio.com', 'test!1234', 'firstname', 'lastname', true);
    });

    it('Display session list', () => {
        cy.url().should('include', '/sessions');
        cy.contains('session 1');
        cy.contains('session 2');

        cy.get('button').contains('Detail');
    });

    it('Display admin buttons', () => {
        cy.get('button').contains('Edit');
        cy.get('button').contains('Create');
    })
});