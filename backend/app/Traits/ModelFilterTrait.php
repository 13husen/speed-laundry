<?php

namespace App\Traits;

use Carbon\Carbon;
use Exception;
use Grimzy\LaravelMysqlSpatial\Types\Geometry;
use Illuminate\Support\Facades\Storage;

trait ModelFilterTrait
{
    /**
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function whereEmpty(string $field, array $params, $query)
    {
        if (array_key_exists($field, $params)) {
            if (empty($params[$field]) && $params[$field] != 0) {
                $query->where($field, '');
            } else {
                $values = explode('|', $params[$field]);
                $query->where(function ($subQuery) use ($field, $values) {
                    foreach ($values as $value) {
                        $subQuery->orWhere($field, $value);
                    }
                    return $subQuery;
                });
            }
        }

        return $query;
    }

    /**
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function whereNull(string $field, array $params, $query)
    {
        if (array_key_exists($field, $params)) {
            if (empty($params[$field]) && $params[$field] !== 0) {
                $query->whereNull($field);
            } else {
                $values = explode('|', $params[$field]);
                $query->where(function ($subQuery) use ($field, $values) {
                    foreach ($values as $value) {
                        $subQuery->orWhere($field, $value);
                    }
                    return $subQuery;
                });
            }
        }

        return $query;
    }

    /**
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function likeEmpty(string $field, array $params, $query)
    {
        if (array_key_exists($field, $params)) {
            if (empty($params[$field]) && $params[$field] !== 0) {
                $query->where($field, 'LIKE', '');
            } else {
                $values = explode('|', $params[$field]);
                $query->where(function ($subQuery) use ($field, $values) {
                    foreach ($values as $value) {
                        $subQuery->orWhere($field, 'LIKE', $this->regex($value));
                    }
                    return $subQuery;
                });
            }
        } elseif (array_key_exists('exactmatch_' . $field, $params)) {
            $params[$field] = $params['exactmatch_' . $field];
            $query = $this->whereEmpty($field, $params, $query);
        }

        return $query;
    }

    /**
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function likeNull(string $field, array $params, $query)
    {
        if (array_key_exists($field, $params)) {
            if (empty($params[$field]) && $params[$field] != 0) {
                $query->whereNull($field);
            } else {
                $values = explode('|', $params[$field]);
                $query->where(function ($subQuery) use ($field, $values) {
                    foreach ($values as $value) {
                        $subQuery->orWhere($field, 'LIKE', $this->regex($value));
                    }
                    return $subQuery;
                });
            }
        } elseif (array_key_exists('exactmatch_' . $field, $params)) {
            $params[$field] = $params['exactmatch_' . $field];
            $query = $this->whereNull($field, $params, $query);
        }

        return $query;
    }

    /**
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function generateSort(array $params, $query)
    {
        if (array_key_exists('sort_by', $params)) {
            $sortData = explode(',', $params['sort_by']);
            $index = 0;
            while (isset($sortData[$index]) && isset($sortData[$index + 1])) {
                $field = $sortData[$index++];
                $sort = $sortData[$index++];
                $query->orderBy($field, $sort);
            }
        }

        return $query;
    }

    /**
     * @param string $relation
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function whereEmptyRelation(string $relation, string $field, array $params, $query)
    {
        $fieldName = $relation . '_' . $field;
        if (array_key_exists($fieldName, $params)) {
            if (empty($params[$field]) && $params[$field] != 0) {
                $query->whereHas($relation, function ($query) use ($field) {
                    $query->where($field, '');
                });
            } else {
                $query->whereHas($relation, function ($query) use ($params, $field) {
                    $query->where($field, $params[$field]);
                });
            }
        }

        return $query;
    }

    /**
     * @param string $relation
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function whereNullRelation(string $relation, string $field, array $params, $query)
    {
        $fieldName = $relation . '_' . $field;
        if (array_key_exists($fieldName, $params)) {
            if (empty($params[$field]) && $params[$field] != 0) {
                $query->whereHas($relation, function ($query) use ($field) {
                    $query->whereNull($field);
                });
            } else {
                $query->whereHas($relation, function ($query) use ($params, $field) {
                    $query->where($field, $params[$field]);
                });
            }
        }

        return $query;
    }


    /**
     * @param string $relation
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function likeEmptyRelation(string $relation, string $field, array $params, $query)
    {
        $fieldName = $relation . '_' . $field;
        if (array_key_exists($fieldName, $params)) {
            if (empty($params[$fieldName])) {
                $query->whereHas($relation, function ($query) use ($field) {
                    $query->where($field, 'LIKE', '');
                });
            } else {
                $query->whereHas($relation, function ($query) use ($field, $params, $fieldName) {
                    $query->where($field, 'LIKE', $this->regex($params[$fieldName]));
                });
            }
        }

        return $query;
    }

    /**
     * @param string $relation
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function likeNullRelation(string $relation, string $field, array $params, $query)
    {
        $fieldName = $relation . '_' . $field;
        if (array_key_exists($fieldName, $params)) {
            if (empty($params[$fieldName])) {
                $query->whereHas($relation, function ($query) use ($field) {
                    $query->whereNull($field);
                });
            } else {
                $query->whereHas($relation, function ($query) use ($field, $params, $fieldName) {
                    $query->where($field, 'LIKE', $this->regex($params[$fieldName]));
                });
            }
        }

        return $query;
    }

    /**
     * @param $path
     * @param string $filename
     * @return string|null
     */
    public function returnAttributeValue($filename = '')
    {
        if (!empty($filename)) {

            $disk = Storage::disk('local');
            return config('app.url').$disk->url($filename);
        }

        return null;
    }

    /**
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function generateDateRange(array $params, $query)
    {
        try {
            if (array_key_exists('date_range', $params)) {
                $dateRangeData = explode(',', $params['date_range']);
                $index = 0;
                while (isset($dateRangeData[$index]) && isset($dateRangeData[$index + 1]) &&
                    isset($dateRangeData[$index + 2])) {
                    $field = $dateRangeData[$index++];
                    $from = $dateRangeData[$index++];
                    $to = $dateRangeData[$index++];

                    if (Carbon::createFromFormat('Y-m-d H:i:s', $from) === false ||
                        Carbon::createFromFormat('Y-m-d H:i:s', $to) === false) {
                        return $query;
                    }

                    $query->where(function ($subQuery) use ($field, $from, $to) {
                        return $subQuery->where($field, '>=', $from)
                            ->where($field, '<=', $to);
                    });
                }
            }
        } catch (Exception $e) {
            // avoid wrong date format
        } finally {
            return $query;
        }
    }

    public function extractCoordinate($value)
    {
        $data = [];
        if (empty($value[0])) {
            return $value[0];
        }

        foreach ($value[0] as $coordinate) {
            $location = Geometry::fromJson(json_encode($coordinate, JSON_UNESCAPED_SLASHES));
            $data[] = [
                'latitude' => $location->getLat(),
                'longitude' => $location->getLng(),
            ];
        }

        return $data;
    }

    private function regex($value)
    {
        return '%' . implode('%', str_split($value)) . '%';
    }

    /**
     * Replaces spaces with full text search wildcards
     *
     * @param string $term
     * @return string
     */
    protected function fullTextWildcards($term)
    {
        // removing symbols used by MySQL
        $reservedSymbols = ['-', '+', '<', '>', '@', '(', ')', '~'];
        $term = str_replace($reservedSymbols, '', $term);

        $words = explode(' ', $term);

        foreach ($words as $key => $word) {
            /*
             * applying + operator (required word) only big words
             * because smaller ones are not indexed by mysql
             */
            if (strlen($word) >= 3) {
                $words[$key] = '+' . $word . '*';
            }
        }

        $searchTerm = implode(' ', $words);

        return $searchTerm;
    }


    /**
     * Scope a query that matches a full text search of term.
     *
     * @param \Illuminate\Database\Eloquent\Builder $query
     * @param string $term
     * @return \Illuminate\Database\Eloquent\Builder
     */
    public function generateFullSearch($query, $term)
    {
        $columns = implode(',', $this->searchable);
        $query->whereRaw("MATCH ({$columns}) AGAINST (? IN BOOLEAN MODE)", $this->fullTextWildcards($term));

        return $query;
    }

    /**
     * @param array $params
     * @param $query
     * @param array $available
     * @param $relation
     * @return mixed
     */
    public function universalSearch(array $params, $query, array $available, $relation = null)
    {
        if (empty($params['universal'] ?? '')) {
            return $query;
        }

        $universalFields = explode(',', ($params['uni_fields'] ?? ''));
        $universalSearch = $params['universal'];

        if (!empty($relation)) {
            $available = array_map(function ($value) use ($relation) {
                return $relation . '_' . $value;
            }, $available);
        }

        foreach ($universalFields as $field) {
            if (!in_array($field, $available)) {
                continue;
            }

            if (!empty($relation)) {
                $field = substr($field, strlen($relation) + 1);
                $query->orWhereHas($relation, function ($query) use ($field, $universalSearch) {
                    $query->where($field, 'LIKE', '%' . $universalSearch . '%');
                });
            } else {
                $query->orWhere($field, 'LIKE', '%' . $universalSearch . '%');
            }
        }

        return $query;
    }

    /**
     * @param string $relation
     * @param string $relation2
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function whereNullRelation2(string $relation, string $relation2, string $field, array $params, $query)
    {
        $fieldName = $relation . '_' . $relation2 . '_' . $field;
        if (array_key_exists($fieldName, $params)) {
            if (empty($params[$field]) && $params[$field] != 0) {
                $query->whereHas($relation, function ($query) use ($field, $relation2) {
                    $query->whereHas($relation2, function ($query2) use ($field) {
                        $query2->whereNull($field);
                    });
                });
            } else {
                $query->whereHas($relation, function ($query) use ($field, $relation2, $params) {
                    $query->whereHas($relation2, function ($query2) use ($field, $params) {
                        $query2->where($field, $params[$field]);
                    });
                });
            }
        }

        return $query;
    }

    /**
     * @param string $relation
     * @param string $relation2
     * @param string $field
     * @param array $params
     * @param $query
     * @return mixed
     */
    public function likeEmptyRelation2(string $relation, string $relation2, string $field, array $params, $query)
    {
        $fieldName = $relation . '_' . $relation2 . '_' . $field;
        if (array_key_exists($fieldName, $params)) {
            if (empty($params[$fieldName]) && $params[$fieldName] != 0) {
                $query->whereHas($relation, function ($query) use ($field, $relation2) {
                    $query->whereHas($relation2, function ($query2) use ($field) {
                        $query2->where($field, 'LIKE', '');
                    });
                });
            } else {
                $query->whereHas($relation, function ($query) use ($field, $relation2, $params, $fieldName) {
                    $query->whereHas($relation2, function ($query2) use ($field, $params, $fieldName) {
                        $query2->where($field, 'LIKE', $this->regex($params[$fieldName]));
                    });
                });
            }
        }

        return $query;
    }
}
