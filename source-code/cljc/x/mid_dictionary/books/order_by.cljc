
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.order-by)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:by-created-at
           {:en "By created at"
            :hu "Létrehozás ideje szerint"}
           :by-created-at-ascending
           {:en "By created at (ascending)"
            :hu "Létrehozás ideje szerint (növekvő)"}
           :by-created-at-descending
           {:en "By created at (descending)"
            :hu "Létrehozás ideje szerint (csökkenő)"}
           :by-date
           {:en "By date"
            :hu "Dátum szerint"}
           :by-date-ascending
           {:en "By date (ascending)"
            :hu "Dátum szerint (növekvő)"}
           :by-date-descending
           {:en "By date (descending)"
            :hu "Dátum szerint (csökkenő)"}
           :by-modified-at
           {:en "By date"
            :hu "Dátum szerint"}
           :by-modified-at-ascending
           {:en "By date (ascending)"
            :hu "Dátum szerint (növekvő)"}
           :by-modified-at-descending
           {:en "By date (descending)"
            :hu "Dátum szerint (csökkenő)"}
           :by-name
           {:en "By name"
            :hu "Név szerint"}
           :by-name-ascending
           {:en "By name (ascending)"
            :hu "Név szerint (növekvő)"}
           :by-name-descending
           {:en "By name (descending)"
            :hu "Név szerint (csökkenő)"}
           :by-order
           {:en "By order"
            :hu "Sorrend szerint"}
           :by-order-ascending
           {:en "By order (ascending)"
            :hu "Sorrend szerint (növekvő)"}
           :by-order-descending
           {:en "By order (descending)"
            :hu "Sorrend szerint (csökkenő)"}
           :by-size
           {:en "By size"
            :hu "Méret szerint"}
           :by-size-ascending
           {:en "By size (ascending)"
            :hu "Méret szerint (növekvő)"}
           :by-size-descending
           {:en "By size (descending)"
            :hu "Méret szerint (csökkenő)"}
           :by-uploaded-at
           {:en "By uploaded at"
            :hu "Feltöltés ideje szerint"}
           :by-uploaded-at-ascending
           {:en "By uploaded at (ascending)"
            :hu "Feltöltés ideje szerint (növekvő)"}
           :by-uploaded-at-descending
           {:en "By uploaded at (descending)"
            :hu "Feltöltés ideje szerint (csökkenő)"}})
