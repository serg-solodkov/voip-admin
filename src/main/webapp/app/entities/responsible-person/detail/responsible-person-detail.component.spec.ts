import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResponsiblePersonDetailComponent } from './responsible-person-detail.component';

describe('Component Tests', () => {
  describe('ResponsiblePerson Management Detail Component', () => {
    let comp: ResponsiblePersonDetailComponent;
    let fixture: ComponentFixture<ResponsiblePersonDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResponsiblePersonDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ responsiblePerson: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResponsiblePersonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResponsiblePersonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load responsiblePerson on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.responsiblePerson).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
