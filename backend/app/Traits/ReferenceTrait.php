<?php

namespace App\Traits;

trait ReferenceTrait
{

    /**
     * @param $sizeNumber
     * @param $sizeString
     * @return string
     */
    public function generateReferralCode($sizeNumber, $sizeString)
    {
        $characterSetArray = [];
        $characterSetArray[] = ['count' => $sizeString, 'characters' => 'ABCDEFGHIJKLMNPQRSTUVWXYZ'];
        $characterSetArray[] = ['count' => $sizeNumber, 'characters' => '123456789'];
        $generatedCode = [];
        foreach ($characterSetArray as $character_set) {
            for ($i = 0; $i < $character_set['count']; $i++) {
                $generatedCode[] = $character_set['characters'][rand(0, strlen($character_set['characters']) - 1)];
            }
        }
        shuffle($generatedCode);
        return implode('', $generatedCode);
    }

    /**
     * @param string $prefix
     * @param integer $id
     * @param $prefixDigitCount
     * @return string
     */
    public function generateReference($prefix, $id, $prefixDigitCount)
    {
        $characterSetArray = [];
        $characterSetArray[] = ['count' => $prefixDigitCount, 'characters' => '123456789'];
        $generatedCode = [];
        foreach ($characterSetArray as $character_set) {
            for ($i = 0; $i < $character_set['count']; $i++) {
                $generatedCode[] = $character_set['characters'][rand(0, strlen($character_set['characters']) - 1)];
            }
        }
        shuffle($generatedCode);

        return $prefix . implode('', $generatedCode) . '0' . $id;
    }
}
