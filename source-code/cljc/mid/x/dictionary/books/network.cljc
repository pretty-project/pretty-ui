
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.dictionary.books.network)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:communication-error
           {:en "Commnication error"
            :hu "Kommunikációs hiba"}
           :network-error
           {:en "Network error"
            :hu "Hálózati hiba"}
           :no-internet-connection
           {:en "No internet connection"
            :hu "Nincs internet kapcsolat"}
           :please-check-your-internet-connection
           {:en "Please check your internet connection!"
            :hu "Kérlek ellenőrizd az internet kapcsolatot!"}
           :you-do-not-have-internet-connection
           {:en "You do not have internet connection"
            :hu "Nincs internet kapcsolatod"}})
