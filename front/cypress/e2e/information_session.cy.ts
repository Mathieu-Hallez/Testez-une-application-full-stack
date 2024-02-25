import '../support/commands';

describe('Information session spec', () => {
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

        cy.intercept(
            {
                method: 'GET',
                url: 'api/user/1'
            },
            {
                id: 1,
                email: 'yoga@studio.com',
                lastName: 'lastname',
                firstName: 'firstname',
                admin: false,
                password: 'test!1234',
                createdAt: new Date('2024-02-02'),
                updatedAt: new Date('2024-02-02'),
            }
        ).as('user');

        cy.login('yoga@studio.com', 'test!1234', 'firstname', 'lastname', false);
        cy.get('span').contains('Account').click();
    });

    it('Display session informations', () => {
        cy.contains('User information');
        cy.contains('Name: firstname LASTNAME');
        cy.contains('Email: yoga@studio.com');
        cy.contains('Name: firstname LASTNAME');
    });

    it('Display delete button', () => {
        cy.get('button').contains('Delete');
    });
})