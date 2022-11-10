
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.errors)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:an-error-occured
           {:en "An error occured"
            :hu "Hiba történt"}
           :error-occured
           {:en "Error occured"
            :hu "Hiba történt"}
           :download-error
           {:en "Download error"
            :hu "Letöltési hiba"}
           :downloading-error
           {:en "Downloading error"
            :hu "Letöltési hiba"}
           :service-not-available
           {:en "Service not available"
            :hu "A szolgáltatás nem elérhető"}})