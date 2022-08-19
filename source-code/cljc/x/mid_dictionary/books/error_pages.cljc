

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.error-pages)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:page-is-not-available
           {:en "Sorry, this page is not available"
            :hu "A keresett oldal nem található"}
           :page-is-under-construction
           {:en "Sorry, this page is under construction"
            :hu "A keresett oldal fejlesztés alatt áll"}
           :page-is-under-maintenance
           {:en "Sorry, this page is under maintenance"
            :hu "A keresett oldal pillanatnyilag nem elérhető"}
           :page-not-found
           {:en "Page not found"
            :hu "Az oldal nem található"}
           :please-check-back-soon...
           {:en "Please check back soon ..."
            :hu "Kérlek nézz vissza később ..."}
           :the-content-you-opened-may-be-broken
           {:en "The content you want to open may be broken or removed"
            :hu "Előfordulhat, hogy a tartalom sérült vagy már nem elérhető"}
           :the-link-you-followed-may-be-broken
           ; Always place a comma before or when it begins an independent clause!
           {:en "The link you followed may be broken, or the page may have been removed"
            :hu "Előfordulhat, hogy a megadott hivatkozás nem megfelelő, vagy az oldalt áthelyeztük"}
           :the-item-you-opened-may-be-broken
           {:en "The item you want to open may be broken or removed"
            :hu "Előfordulhat, hogy az elem sérült vagy már nem elérhető"}
           :you-do-not-have-permission-to-view-this-page
           {:en "You do not have permission to view this page!"
            :hu "Nincs jogosultságod az oldal megtekintéséhez!"}})
