import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVoipAccount } from '../voip-account.model';

@Component({
  selector: 'jhi-voip-account-detail',
  templateUrl: './voip-account-detail.component.html',
})
export class VoipAccountDetailComponent implements OnInit {
  voipAccount: IVoipAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voipAccount }) => {
      this.voipAccount = voipAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
