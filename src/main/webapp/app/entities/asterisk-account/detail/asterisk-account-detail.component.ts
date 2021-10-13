import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAsteriskAccount } from '../asterisk-account.model';

@Component({
  selector: 'jhi-asterisk-account-detail',
  templateUrl: './asterisk-account-detail.component.html',
})
export class AsteriskAccountDetailComponent implements OnInit {
  asteriskAccount: IAsteriskAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asteriskAccount }) => {
      this.asteriskAccount = asteriskAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
