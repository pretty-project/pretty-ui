
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.units)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:age
           {:en "Age"
            :hu "Kor"}
           :age-n
           {:en "Age: %"
            :hu "Kor: %"}
           :cm
           {:en "cm"
            :hu "cm"}
           :centimeter
           {:en "Centimeter"
            :hu "Centiméter"}
           :dag
           {:en "dag"
            :hu "dkg"}
           :day
           {:en "Day"
            :hu "Nap"}
           :day-nth
           {:en "Day %"
            :hu "% nap"}
           :day-unit
           {:en "day(s)"
            :hu "nap"}
           :decagram
           {:en "Decagram"
            :hu "Dekagramm"}
           :decimeter
           {:en "Decimeter"
            :hu "Deciméter"}
           :dm
           {:en "dm"
            :hu "dm"}
           :gr
           {:en "gr"
            :hu "gr"}
           :gram
           {:en "Gram"
            :hu "Gramm"}
           :height
           {:en "Height"
            :hu "Magasság"}
           :height-n
           {:en "Height: %"
            :hu "Magasság: %"}
           :hour
           {:en "Hour"
            :hu "Óra"}
           :hour-nth
           {:en "% hour"
            :hu "% óra"}
           :hour-unit
           {:en "hour(s)"
            :hu "óra"}
           :inner-height
           {:en "Inner height"
            :hu "Belső magasság"}
           :inner-height-n
           {:en "Inner height: %"
            :hu "Belső magasság: %"}
           :inner-length
           {:en "Inner length"
            :hu "Belső hossz"}
           :inner-length-n
           {:en "Inner length: %"
            :hu "Belső hossz: %"}
           :inner-width
           {:en "Inner width"
            :hu "Belső szélesség"}
           :inner-width-n
           {:en "Inner width: %"
            :hu "Belső szélesség: %"}
           :kg
           {:en "kg"
            :hu "kg"}
           :km
           {:en "km"
            :hu "km"}
           :kilogram
           {:en "Kilogram"
            :hu "Kilogramm"}
           :kilometer
           {:en "Kilometer"
            :hu "Kilométer"}
           :length
           {:en "Length"
            :hu "Hossz"}
           :length-n
           {:en "Length: %"
            :hu "Hossz: %"}
           :m
           {:en "m"
            :hu "m"}
           :meter
           {:en "Meter"
            :hu "Méter"}
           :metric-unit
           {:en "Metric unit"
            :hu "Metrikus mértékegység"}
           :metric-units
           {:en "Metric units"
            :hu "Metrikus mértékegységek"}
           :mg
           {:en "mg"
            :hu "mg"}
           :milligram
           {:en "Milligram"
            :hu "Milligramm"}
           :millimeter
           {:en "Millimeter"
            :hu "Milliméter"}
           :millisecond
           {:en "Millisecond"
            :hu "Milliszekundum"}
           :millisecond-nth
           {:en "% millisecond"
            :hu "% milliszekundum"}
           :minute
           {:en "Minute"
            :hu "Perc"}
           :minute-nth
           {:en "% minute"
            :hu "% perc"}
           :minute-unit
           {:en "minute(s)"
            :hu "perc"}
           :mm
           {:en "mm"
            :hu "mm"}
           :month
           {:en "Month"
            :hu "Hónap"}
           :month-nth
           {:en "% month"
            :hu "% hónap"}
           :month-unit
           {:en "month(s)"
            :hu "hónap"}
           :n
           {:en "%"
            :hu "%"}
           :ms
           {:en "ms"
            :hu "ms"}
           :n-cm
           {:en "% cm"
            :hu "% cm"}
           :n-dag
           {:en "% dag"
            :hu "% dkg"}
           :n-days
           {:en "% day(s)"
            :hu "% nap"}
           :n-dm
           {:en "% dm"
            :hu "% dm"}
           :n-gr
           {:en "% gr"
            :hu "% gr"}
           :n-h
           {:en "% h"
            :hu "% ó"}
           :n-hours
           {:en "% hour(s)"
            :hu "% óra"}
           :n-kg
           {:en "% kg"
            :hu "% kg"}
           :n-km
           {:en "% km"
            :hu "% km"}
           :n-m
           {:en "% m"
            :hu "% m"}
           :n-mg
           {:en "% mg"
            :hu "% mg"}
           :n-miles
           {:en "% mile(s)"
            :hu "% mérföld"}
           :n-milliseconds
           {:en "% millisecond(s)"
            :hu "% milliszekundum"}
           :n-min
           {:en "% min(s)"
            :hu "% perc"}
           :n-minutes
           {:en "% minute(s)"
            :hu "% perc"}
           :n-mm
           {:en "% mm"
            :hu "% mm"}
           :n-months
           {:en "% month(s)"
            :hu "% hónap"}
           :n-pieces
           {:en "% piece(s)"
            :hu "% darab"}
           :n-seconds
           {:en "% second(s)"
            :hu "% másodperc"}
           :n-secs
           {:en "% sec(s)"
            :hu "% mp"}
           :n-t
           {:en "% T"
            :hu "% T"}
           :n-units
           {:en "% unit(s)"
            :hu "% egység"}
           :n-weeks
           {:en "% week(s)"
            :hu "% hét"}
           :n-years
           {:en "% year(s)"
            :hu "% év"}
           :outer-height
           {:en "Outer height"
            :hu "Külső magasság"}
           :outer-height-n
           {:en "Outer height: %"
            :hu "Külső magasság: %"}
           :outer-length
           {:en "Outer length"
            :hu "Külső hossz"}
           :outer-length-n
           {:en "Outer length: %"
            :hu "Külső hossz: %"}
           :outer-width
           {:en "Outer width"
            :hu "Külső szélesség"}
           :outer-width-n
           {:en "Outer width: %"
            :hu "Külső szélesség: %"}
           :piece
           {:en "Piece"
            :hu "Darab"}
           :sec
           {:en "sec"
            :hu "mp"}
           :second
           {:en "Second"
            :hu "Másodperc"}
           :second-nth
           {:en "% second"
            :hu "% másodperc"}
           :second-unit
           {:en "second(s)"
            :hu "másodperc"}
           :sec-unit
           {:en "sec(s)"
            :hu "mp"}
           :t
           {:en "T"
            :hu "T"}
           :today
           {:en "Today"
            :hu "Ma"}
           :tomorrow
           {:en "Tomorrow"
            :hu "Holnap"}
           :ton
           {:en "Ton"
            :hu "Tonna"}
           :unit
           {:en "Unit"
            :hu "Egység"}
           :unit-label
           {:en "Unit"
            :hu "Mértékegység"}
           :unit-price
           {:en "Unit price"
            :hu "Egységár"}
           :quantity-unit
           {:en "Quantity unit"
            :hu "Mennyiségi egység"}
           :us-unit
           {:en "US unit"
            :hu "Angolszász mértékegység"}
           :us-units
           {:en "US units"
            :hu "Angolszász mértékegységek"}
           :weight
           {:en "Weight"
            :hu "Tömeg"}
           :weight-n
           {:en "Weight: %"
            :hu "Tömeg: %"}
           :width
           {:en "Width"
            :hu "Szélesség"}
           :width-n
           {:en "Width: %"
            :hu "Szélesség: %"}
           :year
           {:en "Year"
            :hu "Év"}
           :year-nth
           {:en "% year"
            :hu "% év"}
           :year-unit
           {:en "year(s)"
            :hu "év"}
           :yesterday
           {:en "Yesterday"
            :hu "Tegnap"}})
