import { WebConsolePage } from './app.po';

describe('web-console App', () => {
  let page: WebConsolePage;

  beforeEach(() => {
    page = new WebConsolePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('of works!');
  });
});
