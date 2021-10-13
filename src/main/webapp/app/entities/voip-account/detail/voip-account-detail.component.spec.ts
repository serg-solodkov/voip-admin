import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VoipAccountDetailComponent } from './voip-account-detail.component';

describe('Component Tests', () => {
  describe('VoipAccount Management Detail Component', () => {
    let comp: VoipAccountDetailComponent;
    let fixture: ComponentFixture<VoipAccountDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VoipAccountDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ voipAccount: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VoipAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VoipAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load voipAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.voipAccount).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
