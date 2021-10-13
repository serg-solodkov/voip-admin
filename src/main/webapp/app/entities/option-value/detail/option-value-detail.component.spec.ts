import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OptionValueDetailComponent } from './option-value-detail.component';

describe('Component Tests', () => {
  describe('OptionValue Management Detail Component', () => {
    let comp: OptionValueDetailComponent;
    let fixture: ComponentFixture<OptionValueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OptionValueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ optionValue: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OptionValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OptionValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load optionValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.optionValue).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
