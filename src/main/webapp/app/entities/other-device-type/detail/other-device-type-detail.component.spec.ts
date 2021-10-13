import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtherDeviceTypeDetailComponent } from './other-device-type-detail.component';

describe('Component Tests', () => {
  describe('OtherDeviceType Management Detail Component', () => {
    let comp: OtherDeviceTypeDetailComponent;
    let fixture: ComponentFixture<OtherDeviceTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OtherDeviceTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ otherDeviceType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OtherDeviceTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OtherDeviceTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load otherDeviceType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.otherDeviceType).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
