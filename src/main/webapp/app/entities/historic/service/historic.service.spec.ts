import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHistoric, Historic } from '../historic.model';

import { HistoricService } from './historic.service';

describe('Historic Service', () => {
  let service: HistoricService;
  let httpMock: HttpTestingController;
  let elemDefault: IHistoric;
  let expectedResult: IHistoric | IHistoric[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HistoricService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      kms: 0,
      price: 0,
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

    it('should create a Historic', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Historic()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Historic', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          kms: 1,
          price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Historic', () => {
      const patchObject = Object.assign(
        {
          price: 1,
        },
        new Historic()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Historic', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          kms: 1,
          price: 1,
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

    it('should delete a Historic', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHistoricToCollectionIfMissing', () => {
      it('should add a Historic to an empty array', () => {
        const historic: IHistoric = { id: 123 };
        expectedResult = service.addHistoricToCollectionIfMissing([], historic);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historic);
      });

      it('should not add a Historic to an array that contains it', () => {
        const historic: IHistoric = { id: 123 };
        const historicCollection: IHistoric[] = [
          {
            ...historic,
          },
          { id: 456 },
        ];
        expectedResult = service.addHistoricToCollectionIfMissing(historicCollection, historic);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Historic to an array that doesn't contain it", () => {
        const historic: IHistoric = { id: 123 };
        const historicCollection: IHistoric[] = [{ id: 456 }];
        expectedResult = service.addHistoricToCollectionIfMissing(historicCollection, historic);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historic);
      });

      it('should add only unique Historic to an array', () => {
        const historicArray: IHistoric[] = [{ id: 123 }, { id: 456 }, { id: 93465 }];
        const historicCollection: IHistoric[] = [{ id: 123 }];
        expectedResult = service.addHistoricToCollectionIfMissing(historicCollection, ...historicArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historic: IHistoric = { id: 123 };
        const historic2: IHistoric = { id: 456 };
        expectedResult = service.addHistoricToCollectionIfMissing([], historic, historic2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historic);
        expect(expectedResult).toContain(historic2);
      });

      it('should accept null and undefined values', () => {
        const historic: IHistoric = { id: 123 };
        expectedResult = service.addHistoricToCollectionIfMissing([], null, historic, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historic);
      });

      it('should return initial array if no Historic is added', () => {
        const historicCollection: IHistoric[] = [{ id: 123 }];
        expectedResult = service.addHistoricToCollectionIfMissing(historicCollection, undefined, null);
        expect(expectedResult).toEqual(historicCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
