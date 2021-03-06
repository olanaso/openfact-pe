/*******************************************************************************
 * Copyright 2016 Sistcoop, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openfact.pe.utils.finance.internal.languages.polish;

import org.openfact.pe.utils.finance.internal.BaseValues;
import org.openfact.pe.utils.finance.internal.languages.GenderForms;
import org.openfact.pe.utils.finance.internal.languages.PluralForms;
import org.openfact.pe.utils.finance.internal.languages.SlavonicPluralForms;
import org.openfact.pe.utils.finance.internal.support.BaseNumbersBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PolishValues implements BaseValues {

    @Override
    public Map<Integer, GenderForms> baseNumbers() {
        return BaseNumbersBuilder.baseNumbersBuilder()
                .put(0, "zero")
                .put(1, "jeden")
                .put(2, "dwa")
                .put(3, "trzy")
                .put(4, "cztery")
                .put(5, "pięć")
                .put(6, "sześć")
                .put(7, "siedem")
                .put(8, "osiem")
                .put(9, "dziewięć")
                .put(10, "dziesięć")
                .put(11, "jedenaście")
                .put(12, "dwanaście")
                .put(13, "trzynaście")
                .put(14, "czternaście")
                .put(15, "piętnaście")
                .put(16, "szesnaście")
                .put(17, "siedemnaście")
                .put(18, "osiemnaście")
                .put(19, "dziewiętnaście")
                .put(20, "dwadzieścia")
                .put(30, "trzydzieści")
                .put(40, "czterdzieści")
                .put(50, "pięćdziesiąt")
                .put(60, "sześćdziesiąt")
                .put(70, "siedemdziesiąt")
                .put(80, "osiemdziesiąt")
                .put(90, "dziewięćdziesiąt")
                .put(100, "sto")
                .put(200, "dwieście")
                .put(300, "trzysta")
                .put(400, "czterysta")
                .put(500, "pięćset")
                .put(600, "sześćset")
                .put(700, "siedemset")
                .put(800, "osiemset")
                .put(900, "dziewięćset")
                .build();
    }

    @Override
    public List<PluralForms> pluralForms() {
        return Arrays.<PluralForms>asList(
                new SlavonicPluralForms("", "", ""),
                new SlavonicPluralForms("tysiąc", "tysiące", "tysięcy"),
                new SlavonicPluralForms("milion", "miliony", "milionów"),
                new SlavonicPluralForms("miliard", "miliardy", "miliardów"));
    }

    @Override
    public String connector() {
        return "PLN";
    }

    @Override
    public String twoDigitsNumberSeparator() {
        return " ";
    }
}
