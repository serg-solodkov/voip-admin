import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VendorDetailComponent } from './vendor-detail.component';

describe('Component Tests', () => {
  describe('Vendor Management Detail Component', () => {
    let comp: VendorDetailComponent;
    let fixture: ComponentFixture<VendorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VendorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ vendor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VendorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VendorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vendor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vendor).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
