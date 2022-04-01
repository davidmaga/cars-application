import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { HistoricService } from '../service/historic.service';

import { HistoricComponent } from './historic.component';

describe('Historic Management Component', () => {
  let comp: HistoricComponent;
  let fixture: ComponentFixture<HistoricComponent>;
  let service: HistoricService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HistoricComponent],
    })
      .overrideTemplate(HistoricComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoricComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HistoricService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.historics?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
