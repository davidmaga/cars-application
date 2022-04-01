import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGearbox, Gearbox } from '../gearbox.model';

import { GearboxService } from './gearbox.service';

describe('Gearbox Service', () => {
  let service: GearboxService;
  let httpMock: HttpTestingController;
  let elemDefault: IGearbox;
  let expectedResult: IGearbox | IGearbox[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GearboxService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Gearbox', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Gearbox()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gearbox', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Gearbox', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Gearbox()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Gearbox', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Gearbox', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGearboxToCollectionIfMissing', () => {
      it('should add a Gearbox to an empty array', () => {
        const gearbox: IGearbox = { id: 123 };
        expectedResult = service.addGearboxToCollectionIfMissing([], gearbox);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gearbox);
      });

      it('should not add a Gearbox to an array that contains it', () => {
        const gearbox: IGearbox = { id: 123 };
        const gearboxCollection: IGearbox[] = [
          {
            ...gearbox,
          },
          { id: 456 },
        ];
        expectedResult = service.addGearboxToCollectionIfMissing(gearboxCollection, gearbox);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gearbox to an array that doesn't contain it", () => {
        const gearbox: IGearbox = { id: 123 };
        const gearboxCollection: IGearbox[] = [{ id: 456 }];
        expectedResult = service.addGearboxToCollectionIfMissing(gearboxCollection, gearbox);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gearbox);
      });

      it('should add only unique Gearbox to an array', () => {
        const gearboxArray: IGearbox[] = [{ id: 123 }, { id: 456 }, { id: 32301 }];
        const gearboxCollection: IGearbox[] = [{ id: 123 }];
        expectedResult = service.addGearboxToCollectionIfMissing(gearboxCollection, ...gearboxArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gearbox: IGearbox = { id: 123 };
        const gearbox2: IGearbox = { id: 456 };
        expectedResult = service.addGearboxToCollectionIfMissing([], gearbox, gearbox2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gearbox);
        expect(expectedResult).toContain(gearbox2);
      });

      it('should accept null and undefined values', () => {
        const gearbox: IGearbox = { id: 123 };
        expectedResult = service.addGearboxToCollectionIfMissing([], null, gearbox, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gearbox);
      });

      it('should return initial array if no Gearbox is added', () => {
        const gearboxCollection: IGearbox[] = [{ id: 123 }];
        expectedResult = service.addGearboxToCollectionIfMissing(gearboxCollection, undefined, null);
        expect(expectedResult).toEqual(gearboxCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
