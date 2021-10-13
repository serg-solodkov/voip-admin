import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AsteriskAccountDetailComponent } from './asterisk-account-detail.component';

describe('Component Tests', () => {
  describe('AsteriskAccount Management Detail Component', () => {
    let comp: AsteriskAccountDetailComponent;
    let fixture: ComponentFixture<AsteriskAccountDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AsteriskAccountDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ asteriskAccount: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AsteriskAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AsteriskAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load asteriskAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.asteriskAccount).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
