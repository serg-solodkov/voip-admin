jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AsteriskAccountService } from '../service/asterisk-account.service';

import { AsteriskAccountDeleteDialogComponent } from './asterisk-account-delete-dialog.component';

describe('Component Tests', () => {
  describe('AsteriskAccount Management Delete Component', () => {
    let comp: AsteriskAccountDeleteDialogComponent;
    let fixture: ComponentFixture<AsteriskAccountDeleteDialogComponent>;
    let service: AsteriskAccountService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AsteriskAccountDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AsteriskAccountDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AsteriskAccountDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AsteriskAccountService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
