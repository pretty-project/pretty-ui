
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.layout)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:body
           {:en "Body"
            :hu "Törzs"}
           :body-content
           {:en "Body content"
            :hu "Törzs tartalma"}
           :footer
           {:en "Footer"
            :hu "Lábléc"}
           :footer-content
           {:en "Footer content"
            :hu "Lábléc tartalma"}
           :header
           {:en "Header"
            :hu "Fejléc"}
           :header-content
           {:en "Header content"
            :hu "Fejléc tartalma"}})
