// cypress/support/index.d.ts file
// extends Cypress assertion Chainer interface with
// the new assertion methods

/// <reference types="cypress" />

declare namespace Cypress {
    interface Chainable<Subject> {
      /**
       * Login a user in the app
      * */
      login(email : string, password : string, firstname : string, lastname : string, isAdmin : boolean): void;
    }
  }